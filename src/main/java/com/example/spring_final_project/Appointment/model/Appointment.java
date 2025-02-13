package com.example.spring_final_project.Appointment.model;

import com.example.spring_final_project.Doctor.model.Doctor;
import com.example.spring_final_project.User.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column
    private LocalDateTime createdOn;

    @Column
    private LocalDateTime updatedOn;


}
