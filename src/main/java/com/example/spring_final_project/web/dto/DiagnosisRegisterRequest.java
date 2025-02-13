package com.example.spring_final_project.web.dto;

import com.example.spring_final_project.Condition.model.Condition;
import lombok.Data;

@Data
public class DiagnosisRegisterRequest {

    private String description;

    private String treatment;

    private Condition condition;

}
