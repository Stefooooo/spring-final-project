package com.example.spring_final_project.User.service;

import com.example.spring_final_project.PatientCard.service.PatientCardService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.model.UserRole;
import com.example.spring_final_project.User.repository.UserRepository;
import com.example.spring_final_project.exception.DomainException;
import com.example.spring_final_project.web.dto.UserLoginRequest;
import com.example.spring_final_project.web.dto.UserRegisterRequest;
import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientCardService patientCardService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PatientCardService patientCardService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.patientCardService = patientCardService;
    }

    @Transactional
    public User register(UserRegisterRequest userRegisterRequest){

        Optional<User> userOptional = userRepository.findByUsername(userRegisterRequest.getUsername());

        if(userOptional.isPresent()){
            throw new DomainException("A user with a username [%s] already exists.".formatted(userRegisterRequest.getUsername()));
        }

        User user = userRepository.save(initializeUser(userRegisterRequest));

        user.setPatientCard(patientCardService.createPatientCard(user));

        return user;
    }

    private User initializeUser(UserRegisterRequest userRegisterRequest) {

        LocalDateTime now = LocalDateTime.now();

        return User.builder()
                .userRole(UserRole.USER)
                .isActive(true)
                .createdOn(now)
                .updatedOn(now)
                .username(userRegisterRequest.getUsername())
                .pass(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .build();
    }

    public User login(UserLoginRequest userLoginRequest){

        Optional<User> userOptional = userRepository.findByUsername(userLoginRequest.getUsername());
        if(userOptional.isEmpty()){
            throw  new DomainException("Invalid information");
        }
        User user = userOptional.get();
        if(!user.getPass().equals(userLoginRequest.getPassword())){
            throw new DomainException("Invalid information");
        }

        return user;
    }


}
