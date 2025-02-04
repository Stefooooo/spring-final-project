package com.example.spring_final_project.Diagnosis.model;

import com.example.spring_final_project.PatientCard.model.PatientCard;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private LocalDate dateDiagnosis;

    @Column(nullable = false)
    private String treatment;

    @ManyToMany
    private List<PatientCard> patientCards = new ArrayList<>();

    public Diagnosis(String name, LocalDate dateDiagnosis, String treatment) {
        this.name = name;
        this.dateDiagnosis = dateDiagnosis;
        this.treatment = treatment;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDiagnosis() {
        return dateDiagnosis;
    }

    public void setDateDiagnosis(LocalDate dateDiagnosis) {
        this.dateDiagnosis = dateDiagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public List<PatientCard> getPatientCards() {
        return patientCards;
    }

    public void setPatientCards(List<PatientCard> patientCards) {
        this.patientCards = patientCards;
    }
}
