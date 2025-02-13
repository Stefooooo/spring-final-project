package com.example.spring_final_project.Diagnosis.service;

import com.example.spring_final_project.Diagnosis.repository.DiagnosisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;

    @Autowired
    public DiagnosisService(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }


}
