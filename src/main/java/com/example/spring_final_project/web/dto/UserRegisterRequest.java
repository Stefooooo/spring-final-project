package com.example.spring_final_project.web.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    @Size(min = 6, message = "The username must be at least 6 characters long.")
    private String username;

    @Size(min = 6, message = "The password must be at least 6 characters long.")
    private String password;

}
