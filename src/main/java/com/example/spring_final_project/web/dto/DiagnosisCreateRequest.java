package com.example.spring_final_project.web.dto;

import com.example.spring_final_project.Condition.model.Condition;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosisCreateRequest {

    @NotNull(message = "Please select a condition!")
    private Condition condition;

    private String description;

    @Size(min = 3, message = "Treatment must be at least 3 characters long!")
    private String treatment;

}
