package com.example.spring_final_project.PatientCard.service;

import com.example.spring_final_project.PatientCard.model.Gender;
import com.example.spring_final_project.PatientCard.model.PatientCard;
import com.example.spring_final_project.PatientCard.repository.PatientCardRepository;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.exception.DomainException;
import com.example.spring_final_project.web.dto.PatientCardDetailsEditRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class PatientCardService {

    private final PatientCardRepository patientCardRepository;

    @Autowired
    public PatientCardService(PatientCardRepository patientCardRepository) {
        this.patientCardRepository = patientCardRepository;
    }

    public PatientCard createPatientCard(User user) {
         PatientCard patientCard = patientCardRepository.save(initializePatientCard(user));

         return patientCard;
    }

    private PatientCard initializePatientCard(User user) {

        LocalDateTime now = LocalDateTime.now();

        return PatientCard.builder()
                .gender(Gender.NOT_SET)
                .user(user)
                .createdOn(now)
                .updatedOn(now)
                .build();
    }

    public PatientCard getById(UUID id) {
        return patientCardRepository.findById(id).orElseThrow(() -> new DomainException("Patient card with id [%s] cannot be found".formatted(id)));
    }

    public void editCardDetails(UUID id, @Valid PatientCardDetailsEditRequest patientCardEditRequest) {
        PatientCard patientCard = getById(id);

        patientCard.setFirstName(patientCardEditRequest.getFirstName());
        patientCard.setLastName(patientCardEditRequest.getLastName());
        patientCard.setDateOfBirth(patientCardEditRequest.getDate());
        patientCard.setGender(patientCardEditRequest.getGender());
        patientCard.setProfilePicture(patientCardEditRequest.getProfilePicture());
        patientCard.setUpdatedOn(LocalDateTime.now());

        patientCardRepository.save(patientCard);

        log.info("Successfully updated patient card with id [%s]!".formatted(id));
    }
}
