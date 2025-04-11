package com.example.spring_final_project.Diagnosis.model;

import com.example.spring_final_project.Condition.model.Condition;
import com.example.spring_final_project.Doctor.model.Departament;
import com.example.spring_final_project.PatientCard.model.PatientCard;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String description;

    @Column(nullable = false)
    private LocalDate dateDiagnosis;

    @Column(nullable = false)
    private String treatment;

    @ManyToOne()
    private Condition conditions;

    @ManyToOne()
    private PatientCard patientCard;

    @Column
    private LocalDateTime createdOn;

    @Column
    private LocalDateTime updatedOn;

}