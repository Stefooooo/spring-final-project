package com.example.spring_final_project.web.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequest {

    @Size(min = 6, message = "Password must be at least 6 symbols")
    private String username;

    @Size(min = 6, message = "Password must be at least 6 symbols")
    private String password;

}
