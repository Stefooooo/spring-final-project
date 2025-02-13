package com.example.spring_final_project.PatientCard.model;

import com.example.spring_final_project.Diagnosis.model.Diagnosis;
import com.example.spring_final_project.User.model.User;
import jakarta.persistence.*;
import lombok.*;

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
@Table(name = "patient_cards")
public class PatientCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToMany
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @OneToOne
    private User user;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private int age;

    @Column
    private LocalDateTime createdOn;

    @Column
    private LocalDateTime updatedOn;

}
