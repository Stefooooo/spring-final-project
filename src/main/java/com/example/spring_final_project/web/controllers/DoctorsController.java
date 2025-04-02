package com.example.spring_final_project.web.controllers;

import com.example.spring_final_project.Doctor.model.Doctor;
import com.example.spring_final_project.Doctor.service.DoctorService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.security.UserAuthenticationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/doctors")
public class DoctorsController {

    private final DoctorService doctorService;
    private final UserService userService;

    @Autowired
    public DoctorsController(DoctorService doctorService, UserService userService) {
        this.doctorService = doctorService;
        this.userService = userService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ModelAndView getAllDoctorsPage(@AuthenticationPrincipal UserAuthenticationData userAuthenticationData){

        List<Doctor> doctors = doctorService.getAllDoctors();
        User user = userService.getById(userAuthenticationData.getUserId());

        ModelAndView modelAndView = new ModelAndView("doctors");
        modelAndView.addObject("doctors", doctors);
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView getDoctorPage(@AuthenticationPrincipal UserAuthenticationData userAuthenticationData, @PathVariable UUID id){

        Doctor doctor = doctorService.getById(id);
        User user = userService.getById(userAuthenticationData.getUserId());

        ModelAndView modelAndView = new ModelAndView("doctor-details");
        modelAndView.addObject("doctor", doctor);
        modelAndView.addObject("user", user);

        return modelAndView;
    }
}
