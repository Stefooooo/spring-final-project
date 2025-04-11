package com.example.spring_final_project.Diagnosis.repository;

import com.example.spring_final_project.Diagnosis.model.Diagnosis;
import com.example.spring_final_project.PatientCard.model.PatientCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, UUID> {
    Optional<List<Diagnosis>> findAllByPatientCard(PatientCard patientCardId);
}
