package com.example.spring_final_project.web.dto;

import com.example.spring_final_project.Doctor.model.Departament;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConditionAddRequest {

    @NotNull(message = "You must enter a name!")
    private String name;

    @NotNull
    private Departament departament;
}
