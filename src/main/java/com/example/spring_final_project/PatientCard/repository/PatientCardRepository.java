package com.example.spring_final_project.PatientCard.repository;

import com.example.spring_final_project.PatientCard.model.PatientCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientCardRepository extends JpaRepository<PatientCard, UUID> {
}
