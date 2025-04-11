package com.example.spring_final_project.Diagnosis.service;

import com.example.spring_final_project.Diagnosis.model.Diagnosis;
import com.example.spring_final_project.Diagnosis.repository.DiagnosisRepository;
import com.example.spring_final_project.PatientCard.model.PatientCard;
import com.example.spring_final_project.PatientCard.service.PatientCardService;
import com.example.spring_final_project.exception.DomainException;
import com.example.spring_final_project.notification.service.NotificationService;
import com.example.spring_final_project.web.dto.DiagnosisCreateRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final PatientCardService patientCardService;
    private final NotificationService notificationService;

    @Autowired
    public DiagnosisService(DiagnosisRepository diagnosisRepository, PatientCardService patientCardService, NotificationService notificationService) {
        this.diagnosisRepository = diagnosisRepository;
        this.patientCardService = patientCardService;
        this.notificationService = notificationService;
    }

    @Transactional
    @CacheEvict(value = {"diagnoses", "patient-diagnoses"}, allEntries = true)
    public Diagnosis registerDiagnosis(DiagnosisCreateRequest diagnosisCreateRequest, UUID patientCardId){

        PatientCard patientCard = patientCardService.getById(patientCardId);

        Diagnosis diagnosis = initializeDiagnosis(diagnosisCreateRequest, patientCard);

        diagnosisRepository.save(diagnosis);

        log.info("A diagnosis with id [%s] was created for patient card id [%s].".formatted(diagnosis.getId(), patientCard.getId()));
        patientCardService.cardWasUpdated(patientCard);
        notificationService.sendNotification(patientCard.getUser().getId(), "Diagnosis", "A new diagnosis was added to your Patient Card!");

        return diagnosis;
    }

    public Diagnosis initializeDiagnosis(DiagnosisCreateRequest diagnosisCreateRequest, PatientCard patientCard) {
        LocalDateTime now = LocalDateTime.now();

        return Diagnosis.builder()
                .updatedOn(now)
                .createdOn(now)
                .conditions(diagnosisCreateRequest.getCondition())
                .description(diagnosisCreateRequest.getDescription())
                .treatment(diagnosisCreateRequest.getTreatment())
                .dateDiagnosis(LocalDate.now())
                .patientCard(patientCard)
                .build();
    }

    public Diagnosis getById(UUID id){
        return diagnosisRepository.findById(id).orElseThrow(() -> new DomainException("Couldn't find a diagnosis with id [%s]".formatted(id)));
    }

    @Cacheable("diagnoses")
    public List<Diagnosis> getAllDiagnoses(){
        return diagnosisRepository.findAll();
    }

    @Cacheable("patient-diagnoses")
    public List<Diagnosis> getAllDiagnosesForPatient(UUID patientCardId){

        PatientCard patientCard = patientCardService.getById(patientCardId);

        return diagnosisRepository.findAllByPatientCard(patientCard).orElseGet(ArrayList::new);
    }
}
