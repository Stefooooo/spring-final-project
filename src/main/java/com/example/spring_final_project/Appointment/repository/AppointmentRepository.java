package com.example.spring_final_project.Appointment.repository;

import com.example.spring_final_project.Appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Optional<List<Appointment>> findAllByUserId(UUID userId);
}
