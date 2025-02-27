package com.example.spring_final_project.web.dto;

import com.example.spring_final_project.PatientCard.model.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Data
public class PatientCardDetailsEditRequest {

    @Size(min = 2, max = 20, message = "First Name must be at least 2 characters!")
    private String firstName;

    @Size(min = 2, max = 20, message = "Last Name must be at least 2 characters!")
    private String lastName;

    @URL(message = "Enter a valid URL!")
    private String profilePicture;

    @NotNull(message = "You must select an age of birth!")
    private LocalDate date;

    @NotNull(message = "You must select a valid gender!")
    private Gender gender;

}
