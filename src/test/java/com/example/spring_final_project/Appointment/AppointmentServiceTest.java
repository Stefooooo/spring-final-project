package com.example.spring_final_project.Appointment;

import com.example.spring_final_project.Appointment.model.Appointment;
import com.example.spring_final_project.Appointment.model.AppointmentStatus;
import com.example.spring_final_project.Appointment.repository.AppointmentRepository;
import com.example.spring_final_project.Appointment.service.AppointmentService;
import com.example.spring_final_project.Doctor.model.Doctor;
import com.example.spring_final_project.Doctor.service.DoctorService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.exception.DomainException;
import com.example.spring_final_project.notification.service.NotificationService;
import com.example.spring_final_project.web.dto.AppointmentRegisterRequest;
import com.example.spring_final_project.web.dto.EditUserProfileRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserService userService;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void givenHappyPath_whenRegister(){
        // Given
        LocalDate now = LocalDate.now();

        User user = User.builder()
                .id(UUID.randomUUID())
                .build();
        Doctor doctor = Doctor.builder()
                .id(UUID.randomUUID())
                .build();
        Appointment appointment = Appointment.builder()
                .id(UUID.randomUUID())
                .build();
        AppointmentRegisterRequest build = AppointmentRegisterRequest.builder()
                .time(now)
                .build();
        when(userService.getById(any())).thenReturn(user);
        when(doctorService.getById(any())).thenReturn(doctor);
        when(appointmentRepository.save(any())).thenReturn(appointment);

        // When
        Appointment result = appointmentService.registerAppointment(build, UUID.randomUUID(), UUID.randomUUID());

        // Then
        verify(appointmentRepository, times(1)).save(result);
        verify(notificationService, times(1)).sendNotification(any(), any(), any());
    }

    @Test
    void givenExistingAppointmentsInDatabase_whenGetAllAppointments_thenReturnThemAll() {

        // Give
        List<Appointment> appointmentList = List.of(new Appointment(), new Appointment());
        when(appointmentRepository.findAll()).thenReturn(appointmentList);

        // When
        List<Appointment> appointments = appointmentService.getAllAppointments();

        // Then
        assertThat(appointments).hasSize(2);
    }

    @Test
    void givenExistingAppointmentsInDatabaseForUser_whenGetAllAppointmentsForUser_thenReturnThemAll() {

        // Give
        List<Appointment> appointmentList = List.of(new Appointment(), new Appointment());
        when(appointmentRepository.findAllByUserId(any())).thenReturn(Optional.of(appointmentList));

        // When
        List<Appointment> appointments = appointmentService.getAllAppointmentsForUser(UUID.randomUUID());

        // Then
        assertThat(appointments).hasSize(2);
    }

    @Test
    void givenNoAppointmentsInDatabaseForUser_whenGetAllAppointmentsForUser_thenReturnNewList() {

        // Give
        List<Appointment> appointmentList = new ArrayList<>();
        when(appointmentRepository.findAllByUserId(any())).thenReturn(Optional.of(appointmentList));

        // When
        List<Appointment> appointments = appointmentService.getAllAppointmentsForUser(UUID.randomUUID());

        // Then
        assertThat(appointments).hasSize(0);
    }

    @Test
    void givenExistingAppointmentsInDatabaseForUser_whenGetAllActiveAppointmentsForUser_thenReturnThemAll() {

        // Give
        List<Appointment> appointmentList = List.of(new Appointment(), new Appointment());
        when(appointmentRepository.findAllByUserIdAndStatusInOrderByTime(any(), any())).thenReturn(Optional.of(appointmentList));

        // When
        List<Appointment> appointments = appointmentService.getAllActiveAppointmentsForUser(UUID.randomUUID());

        // Then
        assertThat(appointments).hasSize(2);
    }

    @Test
    void givenNoAppointmentsInDatabaseForUser_whenGetAllActiveAppointmentsForUser_thenReturnNewList() {

        // Give
        List<Appointment> appointmentList = new ArrayList<>();
        when(appointmentRepository.findAllByUserIdAndStatusInOrderByTime(any(), any())).thenReturn(Optional.of(appointmentList));

        // When
        List<Appointment> appointments = appointmentService.getAllActiveAppointmentsForUser(UUID.randomUUID());

        // Then
        assertThat(appointments).hasSize(0);
    }

    @Test
    void testGettersAndSetters() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .build();
        Doctor doctor = Doctor.builder()
                .id(UUID.randomUUID())
                .build();

        LocalDateTime now = LocalDateTime.now();

        Appointment appointment = new Appointment();
        appointment.setTime(LocalDate.now());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setUser(user);
        appointment.setDoctor(doctor);
        appointment.setCreatedOn(now);
        appointment.setUpdatedOn(now);

        assertEquals(LocalDate.now(), appointment.getTime());
        assertEquals(AppointmentStatus.PENDING, appointment.getStatus());
        assertEquals(user, appointment.getUser());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(now, appointment.getCreatedOn());
        assertEquals(now, appointment.getUpdatedOn());
    }

}