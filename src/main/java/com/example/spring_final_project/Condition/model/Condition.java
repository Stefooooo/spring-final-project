package com.example.spring_final_project.Condition.model;

import com.example.spring_final_project.Doctor.model.Departament;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conditions")
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Departament departament;

    @Column
    private boolean isActive;

    @Column
    private LocalDateTime createdOn;

    @Column
    private LocalDateTime updatedOn;
}
