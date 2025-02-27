package com.example.spring_final_project.User.model;

import com.example.spring_final_project.Appointment.model.Appointment;
import com.example.spring_final_project.PatientCard.model.PatientCard;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;

    @Column
    private String pass;

    @Column
    private String profilePicture;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Appointment> appointments = new HashSet<>();

    @Column
    private String email;

    @OneToOne(mappedBy = "user")
    private PatientCard patientCard;

    @Column
    private LocalDateTime createdOn;

    @Column
    private LocalDateTime updatedOn;


}
