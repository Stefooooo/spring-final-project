package com.example.spring_final_project.config;

import com.example.spring_final_project.Doctor.model.Doctor;
import com.example.spring_final_project.Doctor.service.DoctorService;
import com.example.spring_final_project.web.dto.DoctorRegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultDoctorsInitializerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DefaultDoctorsInitializer runner;

    @Test
    void testRun_WhenNoDoctorsExist_ShouldRegisterFiveDoctors() throws Exception {
        // Arrange
        when(doctorService.getAllDoctors()).thenReturn(Collections.emptyList());

        // Act
        runner.run();

        // Assert - verify 5 calls to registerDoctor
        verify(doctorService, times(5)).registerDoctor(any(DoctorRegisterRequest.class));
    }

    @Test
    void testRun_WhenDoctorsExist_ShouldNotRegisterDoctors() throws Exception {
        // Arrange
        when(doctorService.getAllDoctors()).thenReturn(List.of(new Doctor()));

        // Act
        runner.run();

        // Assert - should not register any doctors
        verify(doctorService, never()).registerDoctor(any());
    }

}