package com.example.spring_final_project.Doctor.repository;

import com.example.spring_final_project.Doctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    List<Doctor> findAllByDepartment(String department);

    Optional<Doctor> findByEmail(String email);
}
