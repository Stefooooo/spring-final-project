package com.example.spring_final_project.User.repository;

import com.example.spring_final_project.Doctor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
