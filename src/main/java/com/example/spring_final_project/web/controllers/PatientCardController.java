package com.example.spring_final_project.web.controllers;

import com.example.spring_final_project.PatientCard.model.PatientCard;
import com.example.spring_final_project.PatientCard.service.PatientCardService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.web.dto.PatientCardDetailsEditRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/patientCards")
public class PatientCardController {

    private final PatientCardService patientCardService;

    @Autowired
    public PatientCardController(PatientCardService patientCardService) {
        this.patientCardService = patientCardService;
    }

    @GetMapping("/{id}/edit-card")
    public ModelAndView getPatientCardEditPage(@PathVariable UUID id){

        PatientCard patientCard = patientCardService.getById(id);

        ModelAndView modelAndView = new ModelAndView("edit-patient-card-details");
        modelAndView.addObject("patientCardDetailsEditRequest", new PatientCardDetailsEditRequest());
        modelAndView.addObject("patientCard", patientCard);
        modelAndView.addObject("user", patientCard.getUser());

        return modelAndView;


    }

    @PutMapping("/{id}/edit-card")
    public ModelAndView getPatientCardEditPage(@PathVariable UUID id, @Valid PatientCardDetailsEditRequest patientCardDetailsEditRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            PatientCard patientCard = patientCardService.getById(id);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("edit-patient-card-details");
            modelAndView.addObject("patientCard", patientCard);
            modelAndView.addObject("user", patientCard.getUser());
            modelAndView.addObject("patientCardDetailsEditRequest", patientCardDetailsEditRequest);
            return modelAndView;
        }

        patientCardService.editCardDetails(id, patientCardDetailsEditRequest);

        return new ModelAndView("redirect:/home");

    }


    @GetMapping("/{id}")
    public ModelAndView getPatientCardPage(@PathVariable UUID id){

        PatientCard patientCard = patientCardService.getById(id);

        ModelAndView modelAndView = new ModelAndView("patient-card-details");
        modelAndView.addObject("patientCard", patientCard);
        modelAndView.addObject("user", patientCard.getUser());

        return modelAndView;


    }


}
