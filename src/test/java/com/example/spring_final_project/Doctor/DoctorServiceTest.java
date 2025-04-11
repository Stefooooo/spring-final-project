package com.example.spring_final_project.Doctor;

import com.example.spring_final_project.Appointment.model.Appointment;
import com.example.spring_final_project.Diagnosis.model.Diagnosis;
import com.example.spring_final_project.Doctor.model.Departament;
import com.example.spring_final_project.Doctor.model.Doctor;
import com.example.spring_final_project.Doctor.repository.DoctorRepository;
import com.example.spring_final_project.Doctor.service.DoctorService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.model.UserRole;
import com.example.spring_final_project.exception.DomainException;
import com.example.spring_final_project.exception.UsernameAlreadyExistException;
import com.example.spring_final_project.web.dto.DoctorRegisterRequest;
import com.example.spring_final_project.web.dto.EditUserProfileRequest;
import com.example.spring_final_project.web.dto.UserRegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    void whenTryingToRegisterExistingDoctorInDatabase_throwUsernameAlreadyExist(){
        // Given
        DoctorRegisterRequest doctorRegisterRequest = DoctorRegisterRequest.builder()
                .email("stefi@gmail.com")
                .password("123123")
                .build();
        when(doctorRepository.findByEmail(any())).thenReturn(Optional.of(new Doctor()));

        // When & Then
        assertThrows(UsernameAlreadyExistException.class, () -> doctorService.registerDoctor(doctorRegisterRequest));
        verify(doctorRepository, never()).save(any());
    }

    @Test
    void givenHappyPath_whenRegisterDoctor(){
        DoctorRegisterRequest doctorRegisterRequest = DoctorRegisterRequest.builder()
                .email("stefi@gmail.com")
                .password("123123")
                .build();
        when(doctorRepository.findByEmail(any())).thenReturn(Optional.empty());

        Doctor doctor = doctorService.registerDoctor(doctorRegisterRequest);
        verify(doctorRepository, times(1)).save(doctor);
    }

    @Test
    void givenExistingDoctorsInDatabase_whenGetAllDoctors_thenReturnThemAll() {
        List<Doctor> doctors = List.of(new Doctor(), new Doctor());
        when(doctorRepository.findAll()).thenReturn(doctors);

        List<Doctor> result = doctorService.getAllDoctors();
        assertThat(result).hasSize(2);
    }

    @Test
    void givenNoExistingDoctors_whenGetAllDoctors_thenReturnZero() {
        List<Doctor> doctors = new ArrayList<>();
        when(doctorRepository.findAll()).thenReturn(doctors);

        List<Doctor> result = doctorService.getAllDoctors();
        assertThat(result).hasSize(0);
    }

    @Test
    void givenHappyPath_whenGetById() {

        // Given
        Set<Appointment> appointments = Set.of(new Appointment(), new Appointment());
        LocalDateTime now = LocalDateTime.now();
        Doctor doctor = Doctor.builder().build();
        doctor.setId(UUID.randomUUID());
        doctor.setActive(true);
        doctor.setPassword("123123");
        doctor.setUpdatedOn(now);
        doctor.setCreatedOn(now);
        doctor.setEmail("stefi@gmail.com");
        doctor.setRole(UserRole.DOCTOR);
        doctor.setAppointments(appointments);
        doctor.setDescription("doc");
        doctor.setFirstName("doc");
        doctor.setLastName("tor");
        doctor.setDepartment(Departament.ENT);
        doctor.setPhoneNumber("1234");
        doctor.setYearsOfExpertise(12);
        doctor.setProfilePicture("ww.image.com");

        when(doctorRepository.findById(any())).thenReturn(Optional.of(doctor));

        // When & Then
        Doctor result = doctorService.getById(UUID.randomUUID());
        assertEquals(doctor.getId(), result.getId());
        assertTrue(doctor.isActive());
        assertEquals(doctor.getPassword(), result.getPassword());
        assertEquals(doctor.getCreatedOn(), result.getCreatedOn());
        assertEquals(doctor.getUpdatedOn(), result.getUpdatedOn());
        assertEquals(doctor.getEmail(), result.getEmail());
        assertEquals(doctor.getRole(), result.getRole());
        assertEquals(doctor.getAppointments(), result.getAppointments());
        assertEquals(doctor.getDescription(), result.getDescription());
        assertEquals(doctor.getFirstName(), result.getFirstName());
        assertEquals(doctor.getLastName(), result.getLastName());
        assertEquals(doctor.getProfilePicture(), result.getProfilePicture());
        assertEquals(doctor.getDepartment(), result.getDepartment());
        assertEquals(doctor.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(doctor.getYearsOfExpertise(), result.getYearsOfExpertise());
    }

    @Test
    void givenMissingDoctorsFromDatabase_whenGetById_thenExceptionIsThrown() {

        Doctor.builder()
                .id(UUID.randomUUID())
                .build();
        when(doctorRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> doctorService.getById(UUID.randomUUID()));
    }

}