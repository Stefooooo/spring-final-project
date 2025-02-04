package com.example.spring_final_project.PatientCard.model;

import com.example.spring_final_project.Diagnosis.model.Diagnosis;
import com.example.spring_final_project.User.model.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "patient_cards")
public class PatientCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToMany
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @Column(nullable = false, unique = true)
    @OneToOne(mappedBy = "patientCard")
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender sex = Gender.NOT_SET;

    @Column(nullable = false)
    private int age = 18;

    public PatientCard(User user) {
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public List<Diagnosis> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(List<Diagnosis> diagnoses) {
        this.diagnoses = diagnoses;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Gender getSex() {
        return sex;
    }

    public void setSex(Gender sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
