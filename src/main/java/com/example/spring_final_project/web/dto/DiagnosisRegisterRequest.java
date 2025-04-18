package com.example.spring_final_project.web.dto;

import com.example.spring_final_project.Condition.model.Condition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiagnosisRegisterRequest {

    private String description;

    private String treatment;

    private Condition condition;

}
