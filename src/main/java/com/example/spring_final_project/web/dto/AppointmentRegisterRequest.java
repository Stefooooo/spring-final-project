package com.example.spring_final_project.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentRegisterRequest {

    @NotNull(message = "Time must not be empty!")
    private LocalDate time;


}
