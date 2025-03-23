package com.example.spring_final_project.User.service;

import com.example.spring_final_project.PatientCard.service.PatientCardService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.model.UserRole;
import com.example.spring_final_project.User.repository.UserRepository;
import com.example.spring_final_project.exception.DomainException;
import com.example.spring_final_project.notification.service.NotificationService;
import com.example.spring_final_project.security.UserAuthenticationData;
import com.example.spring_final_project.web.dto.EditUserProfileRequest;
import com.example.spring_final_project.web.dto.UserLoginRequest;
import com.example.spring_final_project.web.dto.UserRegisterRequest;
import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientCardService patientCardService;
    private final NotificationService notificationService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PatientCardService patientCardService, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.patientCardService = patientCardService;
        this.notificationService = notificationService;
    }

    @Transactional
    public User register(UserRegisterRequest userRegisterRequest){

        Optional<User> userOptional = userRepository.findByUsername(userRegisterRequest.getUsername());

        if(userOptional.isPresent()){
            throw new DomainException("A user with a username [%s] already exists.".formatted(userRegisterRequest.getUsername()));
        }

        User user = userRepository.save(initializeUser(userRegisterRequest));

        user.setPatientCard(patientCardService.createPatientCard(user));

        notificationService.saveNotificationPreference(user.getId(), false, null);

        log.info("User with username [%s] and id [%s] has been created!".formatted(user.getUsername(), user.getId()));

        return user;
    }

    private User initializeUser(UserRegisterRequest userRegisterRequest) {

        LocalDateTime now = LocalDateTime.now();

        return User.builder()
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(now)
                .updatedOn(now)
                .username(userRegisterRequest.getUsername())
                .pass(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .build();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new DomainException("User with this username has not been found!"));

        return new UserAuthenticationData(user.getId(), username, user.getPass(), user.getRole(), user.isActive());
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new DomainException("User with id [%s] not found!".formatted(id)));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void editUserDetails(UUID id, @Valid EditUserProfileRequest editUserProfileRequest) {
        User user = getById(id);

        user.setEmail(editUserProfileRequest.getEmail());
        user.setProfilePicture(editUserProfileRequest.getProfilePicture());
        user.setUpdatedOn(LocalDateTime.now());

        userRepository.save(user);

        log.info("Successfully changed the details for user [%s]!".formatted(user.getUsername()));
    }

    public void switchStatus(UUID id) {
        User user = getById(id);

        user.setActive(!user.isActive());

        userRepository.save(user);

        log.info("Successfully changed the status for user [%s]!".formatted(user.getUsername()));
    }

    public void switchRole(UUID id) {
        User user = getById(id);

        if (user.getRole() == UserRole.USER){
            user.setRole(UserRole.ADMIN);
        } else {
            user.setRole(UserRole.USER);
        }

        userRepository.save(user);

        log.info("Successfully changed the role for user [%s] to [%s]!".formatted(user.getUsername(), user.getRole()));
    }
}
