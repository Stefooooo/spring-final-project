package com.example.spring_final_project.user;

import com.example.spring_final_project.Appointment.model.Appointment;
import com.example.spring_final_project.Condition.model.Condition;
import com.example.spring_final_project.Diagnosis.model.Diagnosis;
import com.example.spring_final_project.PatientCard.model.PatientCard;
import com.example.spring_final_project.PatientCard.service.PatientCardService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.model.UserRole;
import com.example.spring_final_project.User.repository.UserRepository;
import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.exception.DomainException;
import com.example.spring_final_project.exception.UsernameAlreadyExistException;
import com.example.spring_final_project.notification.service.NotificationService;
import com.example.spring_final_project.security.UserAuthenticationData;
import com.example.spring_final_project.web.dto.EditUserProfileRequest;
import com.example.spring_final_project.web.dto.UserRegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PatientCardService patientCardService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private UserService userService;

    @Test
    void whenTryingToRegisterExistingUserInDatabase_throwUsernameAlreadyExist(){
        // Given
        UserRegisterRequest registerRequest = UserRegisterRequest.builder()
                .username("stefi")
                .password("123123")
                .build();
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

        // When & Then
        assertThrows(UsernameAlreadyExistException.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(any());
        verify(patientCardService, never()).createPatientCard(any());
        verify(notificationService, never()).saveNotificationPreference(any(UUID.class), anyBoolean(), anyString());
    }

    @Test
    void givenHappyPath_whenRegister(){
        // Given
        UserRegisterRequest registerRequest = UserRegisterRequest.builder()
                .username("Stefi")
                .password("123123")
                .build();
        User user = User.builder()
                .id(UUID.randomUUID())
                .build();
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        when(patientCardService.createPatientCard(user)).thenReturn(new PatientCard());

        // When
        User registeredUser = userService.register(registerRequest);

        // Then
        assertThat(registeredUser.getPatientCard()).isNotNull();
        verify(notificationService, times(1)).saveNotificationPreference(user.getId(), false, null);
    }

    @Test
    void givenMissingUserFromDatabase_whenLoadUserByUsername_thenExceptionIsThrown() {

        // Given
        String username = "stefi";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DomainException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    void givenHappyPath_whenLoadUserByUsername() {

        // Given
        String username = "Stefo";
        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(true)
                .pass("123123")
                .role(UserRole.ADMIN)
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        UserDetails authenticationMetadata = userService.loadUserByUsername(username);

        // Then
        assertInstanceOf(UserAuthenticationData.class, authenticationMetadata);
        UserAuthenticationData result = (UserAuthenticationData) authenticationMetadata;
        assertEquals(user.getId(), result.getUserId());
        assertEquals(username, result.getUsername());
        assertEquals(user.getPass(), result.getPassword());
        assertEquals(user.isActive(), result.isActive());
        assertEquals(user.getRole(), result.getRole());
        assertThat(result.getAuthorities()).hasSize(1);
        assertTrue(result.isEnabled());
        assertTrue(result.isCredentialsNonExpired());
        assertTrue(result.isAccountNonLocked());
        assertTrue(result.isAccountNonExpired());
        assertEquals("ROLE_ADMIN", result.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void givenMissingUserFromDatabase_whenGetById_thenExceptionIsThrown() {

        // Given
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DomainException.class, () -> userService.getById(UUID.randomUUID()));
    }

    @Test
    void givenHappyPath_whenGetById() {

        // Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(true)
                .pass("123123")
                .build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // When & Then
        User result = userService.getById(UUID.randomUUID());
        assertEquals(user.getId(), result.getId());
        assertEquals(user.isActive(), result.isActive());
        assertEquals(user.getPass(), result.getPass());
        assertEquals(user.getPass(), result.getPass());
    }

    @Test
    void givenMissingUserFromDatabase_whenEditUserDetails_thenExceptionIsThrown() {

        UUID userId = UUID.randomUUID();
        EditUserProfileRequest dto = EditUserProfileRequest.builder().build();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> userService.editUserDetails(userId, dto));
    }

    @Test
    void givenExistingUser_whenEditTheirProfileWithActualEmail_thenChangeTheirDetailsSaveNotificationPreferenceAndSaveToDatabase() {

        // Given
        UUID userId = UUID.randomUUID();
        EditUserProfileRequest dto = EditUserProfileRequest.builder()
                .email("stefo@gmail.bg")
                .profilePicture("www.image.com")
                .build();
        User user = User.builder().build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userService.editUserDetails(userId, dto);

        // Then
        assertEquals("stefo@gmail.bg", user.getEmail());
        assertEquals("www.image.com", user.getProfilePicture());
//        verify(notificationService, times(1)).saveNotificationPreference(userId,  dto.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenExistingUser_whenEditTheirProfileWithEmptyEmail_thenChangeTheirDetailsSaveNotificationPreferenceAndSaveToDatabase() {

        // Given
        UUID userId = UUID.randomUUID();
        EditUserProfileRequest dto = EditUserProfileRequest.builder()
                .email("")
                .profilePicture("www.image.com")
                .build();
        User user = User.builder().build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userService.editUserDetails(userId, dto);

        // Then
        assertEquals("", user.getEmail());
        assertEquals("www.image.com", user.getProfilePicture());
//        verify(notificationService, times(1)).saveNotificationPreference(userId,  null);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenInactiveUserSwitchToActive_whenSwitchStatus(){
        // When
        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(false)
                .build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // When & Then
        userService.switchStatus(user.getId());
        assertTrue(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenActiveUserSwitchToInActive_whenSwitchStatus(){
        // When
        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(true)
                .build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // When & Then
        userService.switchStatus(user.getId());
        assertFalse(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserSwitchRoleToAdmin_whenSwitchRole(){
        // When
        User user = User.builder()
                .id(UUID.randomUUID())
                .role(UserRole.USER)
                .build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // When & Then
        userService.switchRole(user.getId());
        assertEquals(UserRole.ADMIN,user.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenAdminSwitchRoleToUser_whenSwitchRole(){
        // When
        User user = User.builder()
                .id(UUID.randomUUID())
                .role(UserRole.ADMIN)
                .build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // When & Then
        userService.switchRole(user.getId());
        assertEquals(UserRole.USER,user.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenExistingUsersInDatabase_whenGetAllUsers_thenReturnThemAll() {

        // Give
        List<User> userList = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(userList);

        // When
        List<User> users = userService.getAllUsers();

        // Then
        assertThat(users).hasSize(2);
    }

    @Test
    void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        Set<Appointment> appointment = Set.of(new Appointment(), new Appointment());

        User user = new User();
        user.setAppointments(appointment);
        user.setCreatedOn(now);
        user.setUpdatedOn(now);


        assertEquals(appointment, user.getAppointments());
        assertEquals(now, user.getUpdatedOn());
        assertEquals(now, user.getCreatedOn());
    }
}