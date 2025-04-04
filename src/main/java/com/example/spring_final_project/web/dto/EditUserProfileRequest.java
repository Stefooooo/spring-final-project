package com.example.spring_final_project.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class EditUserProfileRequest {

    @URL(message = "Enter a valid URL!")
    @Size(max = 200, message = "Too long!")
    private String profilePicture;

    @Email(message = "Enter a valid email!")
    private String email;

}
