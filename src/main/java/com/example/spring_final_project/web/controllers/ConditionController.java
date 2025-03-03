package com.example.spring_final_project.web.controllers;

import com.example.spring_final_project.Condition.model.Condition;
import com.example.spring_final_project.Condition.service.ConditionService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.security.UserAuthenticationData;
import com.example.spring_final_project.web.dto.ConditionAddRequest;
import com.example.spring_final_project.web.dto.PatientCardDetailsEditRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/conditions")
public class ConditionController {

    private final ConditionService conditionService;
    private final UserService userService;

    @Autowired
    public ConditionController(ConditionService conditionService, UserService userService) {
        this.conditionService = conditionService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getConditionsPage(@AuthenticationPrincipal UserAuthenticationData userAuthenticationData){

        User user = userService.getById(userAuthenticationData.getUserId());
        List<Condition> conditions = conditionService.getAllActiveConditions();

        ModelAndView modelAndView = new ModelAndView("conditions");
        modelAndView.addObject("conditions", conditions);
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/add-condition")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getConditionsAddPage(@AuthenticationPrincipal UserAuthenticationData userAuthenticationData){

        User user = userService.getById(userAuthenticationData.getUserId());

        ModelAndView modelAndView = new ModelAndView("add-condition");
        modelAndView.addObject("conditionAddRequest", new ConditionAddRequest());
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @PutMapping("/add-condition")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addCondition(@AuthenticationPrincipal UserAuthenticationData userAuthenticationData, @Valid ConditionAddRequest conditionAddRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            User user = userService.getById(userAuthenticationData.getUserId());
            ModelAndView modelAndView = new ModelAndView("add-condition");
            modelAndView.addObject("conditionAddRequest", conditionAddRequest);
            modelAndView.addObject("user", user);

            return modelAndView;
        }

        conditionService.saveCondition(conditionAddRequest);

        return new ModelAndView("redirect:/conditions");
    }

    @DeleteMapping("/{id}/delete-condition")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteCondition(@AuthenticationPrincipal UserAuthenticationData userAuthenticationData, @PathVariable UUID id){

        conditionService.deleteCondition(id);

        return new ModelAndView("redirect:/conditions");
    }


}
