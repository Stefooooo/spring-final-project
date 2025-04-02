package com.example.spring_final_project.web.dto;

import com.example.spring_final_project.Doctor.model.Departament;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorRegisterRequest {

    @Email(message = "Enter a valid email!")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 symbols")
    private String password;

    @NotNull
    private Departament departament;

}
