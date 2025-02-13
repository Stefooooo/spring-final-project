package com.example.spring_final_project.PatientCard.service;

import com.example.spring_final_project.PatientCard.model.Gender;
import com.example.spring_final_project.PatientCard.model.PatientCard;
import com.example.spring_final_project.PatientCard.repository.PatientCardRepository;
import com.example.spring_final_project.User.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
                .age(18)
                .gender(Gender.NOT_SET)
                .firstName("Unknown")
                .lastName("User")
                .user(user)
                .createdOn(now)
                .updatedOn(now)
                .build();
    }

}
