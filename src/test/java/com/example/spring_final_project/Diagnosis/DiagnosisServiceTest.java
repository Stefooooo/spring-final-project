package com.example.spring_final_project.Diagnosis;

import com.example.spring_final_project.Condition.model.Condition;
import com.example.spring_final_project.Diagnosis.model.Diagnosis;
import com.example.spring_final_project.Diagnosis.repository.DiagnosisRepository;
import com.example.spring_final_project.Diagnosis.service.DiagnosisService;
import com.example.spring_final_project.PatientCard.model.PatientCard;
import com.example.spring_final_project.PatientCard.service.PatientCardService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.exception.DomainException;
import com.example.spring_final_project.notification.service.NotificationService;
import com.example.spring_final_project.web.dto.DiagnosisCreateRequest;
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
class DiagnosisServiceTest {

    @Mock
    private DiagnosisRepository diagnosisRepository;

    @Mock
    private PatientCardService patientCardService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private DiagnosisService diagnosisService;

    @Test
    void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        Condition condition = Condition.builder()
                .id(UUID.randomUUID())
                .build();

        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDateDiagnosis(LocalDate.now());
        diagnosis.setDescription("nastinka");
        diagnosis.setTreatment("pochivka");
        diagnosis.setConditions(condition);
        diagnosis.setCreatedOn(now);
        diagnosis.setUpdatedOn(now);

        assertEquals("nastinka", diagnosis.getDescription());
        assertEquals("pochivka", diagnosis.getTreatment());
        assertEquals(condition, diagnosis.getConditions());
        assertEquals(LocalDate.now(), diagnosis.getDateDiagnosis());
        assertEquals(now, diagnosis.getCreatedOn());
        assertEquals(now, diagnosis.getUpdatedOn());
    }

    @Test
    void givenHappyPath_whenRegisterDiagnosis(){

        UUID patientCardId = UUID.randomUUID();
        User user = User.builder()
                .id(UUID.randomUUID())
                .build();
        PatientCard patientCard = PatientCard.builder()
                .id(patientCardId)
                .user(user)
                .build();
        Condition condition = Condition.builder()
                .id(UUID.randomUUID())
                .build();
        DiagnosisCreateRequest diagnosisCreateRequest = DiagnosisCreateRequest.builder()
                .description("nastinka")
                .treatment("rest")
                .condition(condition)
                .build();
        when(patientCardService.getById(any())).thenReturn(patientCard);

        Diagnosis diagnosis = diagnosisService.registerDiagnosis(diagnosisCreateRequest, patientCardId);
        assertThat(diagnosis.getPatientCard()).isNotNull();
        assertThat(diagnosis.getConditions()).isNotNull();
        verify(notificationService, times(1)).sendNotification(any(), any(), any());
        verify(patientCardService, times(1)).cardWasUpdated(patientCard);
    }

    @Test
    void givenNoActualDiagnosis_throwException_whenGetDiagnosisById() {
        // Given
        when(diagnosisRepository.findById(any())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(DomainException.class, () -> diagnosisService.getById(UUID.randomUUID()));
    }

    @Test
    void givenHappyPath_whenGetDiagnosisById() {

        // Given
        Diagnosis diagnosis = Diagnosis.builder()
                .id(UUID.randomUUID())
                .build();
        when(diagnosisRepository.findById(any())).thenReturn(Optional.of(diagnosis));

        //When
        Diagnosis result = diagnosisService.getById(UUID.randomUUID());

        assertEquals(diagnosis.getId(), result.getId());
    }

    @Test
    void givenExistingDiagnosisInDatabase_whenGetAllDiagnosis_thenReturnThemAll() {
        List<Diagnosis> diagnoses = List.of(new Diagnosis(), new Diagnosis());
        when(diagnosisRepository.findAll()).thenReturn(diagnoses);

        List<Diagnosis> result = diagnosisService.getAllDiagnoses();
        assertThat(result).hasSize(2);
    }

    @Test
    void givenNoExistingDiagnosisInDatabase_whenGetAllDiagnosis_thenReturnZero() {
        List<Diagnosis> diagnoses = new ArrayList<>();
        when(diagnosisRepository.findAll()).thenReturn(diagnoses);

        List<Diagnosis> result = diagnosisService.getAllDiagnoses();
        assertThat(result).hasSize(0);
    }

    @Test
    void givenExistingDiagnosisInDatabase_whenGetAllDiagnosisForPatient_thenReturnThemAll() {
        List<Diagnosis> diagnoses = List.of(new Diagnosis(), new Diagnosis());
        when(diagnosisRepository.findAllByPatientCard(any())).thenReturn(Optional.of(diagnoses));

        List<Diagnosis> result = diagnosisService.getAllDiagnosesForPatient(UUID.randomUUID());
        assertThat(result).hasSize(2);
    }

    @Test
    void givenNoExistingDiagnosisInDatabase_whenGetAllDiagnosisForPatient_thenReturnZero() {
        List<Diagnosis> diagnoses = new ArrayList<>();
        when(diagnosisRepository.findAllByPatientCard(any())).thenReturn(Optional.of(diagnoses));

        List<Diagnosis> result = diagnosisService.getAllDiagnosesForPatient(UUID.randomUUID());
        assertThat(result).hasSize(0);
    }
}