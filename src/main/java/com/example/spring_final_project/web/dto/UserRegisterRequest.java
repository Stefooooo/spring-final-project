package com.example.spring_final_project.web.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UserRegisterRequest {

    @Size(min = 6, message = "The username must be at least 6 characters long.")
    private String username;

    @Size(min = 6, message = "The password must be at least 6 characters long.")
    private String password;

}
