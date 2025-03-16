package com.example.spring_final_project.Doctor.service;

import com.example.spring_final_project.Doctor.model.Doctor;
import com.example.spring_final_project.Doctor.repository.DoctorRepository;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.model.UserRole;
import com.example.spring_final_project.exception.DomainException;
import com.example.spring_final_project.security.UserAuthenticationData;
import com.example.spring_final_project.web.dto.DoctorRegisterRequest;
import com.example.spring_final_project.web.dto.UserRegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Doctor> getAllDoctorsByDepartment(String department) {

        return doctorRepository.findAllByDepartment(department);

    }

    public List<Doctor> getAllDoctors() {
        return  doctorRepository.findAll();
    }


//    private Doctor initializeDoctor(DoctorRegisterRequest doctorRegisterRequest) {
//
//        LocalDateTime now = LocalDateTime.now();
//
//        return Doctor.builder()
//                .email(doctorRegisterRequest.getEmail())
//                .role(UserRole.DOCTOR)
//                .isActive(true)
//                .createdOn(now)
//                .updatedOn(now)
//                .department(doctorRegisterRequest.getDepartament())
//                .password(passwordEncoder.encode(doctorRegisterRequest.getPassword()))
//                .build();
//    }

    public Doctor getById(UUID id) {
        return doctorRepository.findById(id).orElseThrow(() -> new DomainException("There is no doctor found with id [%s]!"));
    }


}
