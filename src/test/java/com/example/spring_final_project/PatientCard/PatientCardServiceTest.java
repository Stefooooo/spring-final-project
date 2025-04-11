package com.example.spring_final_project.PatientCard;

import com.example.spring_final_project.Appointment.model.Appointment;
import com.example.spring_final_project.Diagnosis.model.Diagnosis;
import com.example.spring_final_project.Doctor.model.Departament;
import com.example.spring_final_project.Doctor.model.Doctor;
import com.example.spring_final_project.PatientCard.model.Gender;
import com.example.spring_final_project.PatientCard.model.PatientCard;
import com.example.spring_final_project.PatientCard.repository.PatientCardRepository;
import com.example.spring_final_project.PatientCard.service.PatientCardService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.model.UserRole;
import com.example.spring_final_project.exception.DomainException;
import com.example.spring_final_project.web.dto.PatientCardDetailsEditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientCardServiceTest {

    @Mock
    private PatientCardRepository patientCardRepository;

    @InjectMocks
    private PatientCardService patientCardService;

    @Test
    void givenHappyPath_whenCreatePatientCard(){
        //Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        // When
        PatientCard patientCard = patientCardService.createPatientCard(user);

        // Then
        verify(patientCardRepository, times(1)).save(patientCard);
        assertThat(patientCard.getUser()).isNotNull();
    }

    @Test
    void givenHappyPath_whenGetById() {

        // Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .build();
        PatientCard patientCard = PatientCard.builder()
                .id(UUID.randomUUID())
                .user(user)
                .build();
        when(patientCardRepository.findById(any())).thenReturn(Optional.of(patientCard));


        // When & Then
        PatientCard result = patientCardService.getById(UUID.randomUUID());
        assertEquals(patientCard.getId(), result.getId());
        assertThat(result.getUser()).isNotNull();
    }

    @Test
    void givenMissingPatientCardFromDatabase_whenGetById_thenExceptionIsThrown() {

        PatientCard.builder()
                .id(UUID.randomUUID())
                .build();
        when(patientCardRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> patientCardService.getById(UUID.randomUUID()));
    }

    @Test
    void givenHappyPath_whenCardWasUpdated() {

        // Given
        PatientCard patientCard = PatientCard.builder()
                .id(UUID.randomUUID())
                .build();

        // When & Then
        patientCardService.cardWasUpdated(patientCard);
        verify(patientCardRepository, times(1)).save(patientCard);
    }

    @Test
    void givenHappyPath_whenEditCardDetails() {

        // Given
        PatientCardDetailsEditRequest patientCardDetailsEditRequest = PatientCardDetailsEditRequest.builder()
                .date(LocalDate.now())
                .firstName("stefi")
                .firstName("stefi")
                .gender(Gender.MALE)
                .profilePicture("www.image.com")
                .build();
        PatientCard patientCard = PatientCard.builder().build();


        when(patientCardRepository.findById(any())).thenReturn(Optional.of(patientCard));

        // When & Then
        patientCardService.editCardDetails(UUID.randomUUID(), patientCardDetailsEditRequest);
        assertEquals(patientCard.getDateOfBirth(), patientCardDetailsEditRequest.getDate());
        assertEquals(patientCard.getFirstName(), patientCardDetailsEditRequest.getFirstName());
        assertEquals(patientCard.getLastName(), patientCardDetailsEditRequest.getLastName());
        assertEquals(patientCard.getGender(), patientCardDetailsEditRequest.getGender());
        assertEquals(patientCard.getProfilePicture(), patientCardDetailsEditRequest.getProfilePicture());
        verify(patientCardRepository, times(1)).save(patientCard);
    }

    @Test
    void testGettersAndSetters(){
        PatientCard patientCard = new PatientCard();
        List<Diagnosis> diagnoses = List.of(new Diagnosis());
        LocalDateTime now = LocalDateTime.now();
        patientCard.setDiagnoses(diagnoses);
        patientCard.setCreatedOn(now);
        patientCard.setUpdatedOn(now);

        when(patientCardRepository.findById(any())).thenReturn(Optional.of(patientCard));

        PatientCard result = patientCardService.getById(UUID.randomUUID());
        assertEquals(result.getDiagnoses(), patientCard.getDiagnoses());
        assertEquals(result.getCreatedOn(), patientCard.getCreatedOn());
        assertEquals(result.getUpdatedOn(), patientCard.getUpdatedOn());
    }
}