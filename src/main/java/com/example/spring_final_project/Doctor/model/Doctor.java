package com.example.spring_final_project.Doctor.model;

import com.example.spring_final_project.Appointment.model.Appointment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String password;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    private String email;

    @Column(name = "years_of_expertise")
    private int yearsOfExpertise;

    @Transient
    private String role = "DOCTOR";

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "doctor")
    private Set<Appointment> appointments = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Departament specialty;

    @Column
    private String description;

    @Column
    private LocalDateTime createdOn;

    @Column
    private LocalDateTime updatedOn;


}
