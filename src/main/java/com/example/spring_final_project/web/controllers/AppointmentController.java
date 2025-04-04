package com.example.spring_final_project.web.controllers;

import com.example.spring_final_project.Appointment.model.Appointment;
import com.example.spring_final_project.Appointment.service.AppointmentService;
import com.example.spring_final_project.Doctor.model.Doctor;
import com.example.spring_final_project.Doctor.service.DoctorService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.security.UserAuthenticationData;
import com.example.spring_final_project.web.dto.AppointmentRegisterRequest;
import com.example.spring_final_project.web.dto.ConditionAddRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final UserService userService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    @Autowired
    public AppointmentController(UserService userService, AppointmentService appointmentService, DoctorService doctorService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    @GetMapping("/add/{id}")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView getAppointmentCreatePage(@AuthenticationPrincipal UserAuthenticationData userAuthenticationData, @PathVariable UUID id){

        User user = userService.getById(userAuthenticationData.getUserId());
        Doctor doctor = doctorService.getById(id);

        ModelAndView modelAndView = new ModelAndView("add-appointment");
        modelAndView.addObject("user", user);
        modelAndView.addObject("doctor", doctor);
        modelAndView.addObject("appointmentRegisterRequest", new AppointmentRegisterRequest());

        return modelAndView;
    }

    @PutMapping("/add/{id}")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView createAppointment(@AuthenticationPrincipal UserAuthenticationData userAuthenticationData, @PathVariable UUID id,
                                    @Valid AppointmentRegisterRequest appointmentRegisterRequest, BindingResult bindingResult){

        User user = userService.getById(userAuthenticationData.getUserId());

        if(bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("add-appointment");
            modelAndView.addObject("appointmentRegisterRequest", appointmentRegisterRequest);
            modelAndView.addObject("user", user);

            return modelAndView;
        }

        appointmentService.registerAppointment(appointmentRegisterRequest, user.getId(), id);

        return new ModelAndView("redirect:/home");
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ModelAndView getAllAppointmentsForUser(@AuthenticationPrincipal UserAuthenticationData userAuthenticationData){

        User user = userService.getById(userAuthenticationData.getUserId());
        List<Appointment> appointments = appointmentService.getAllActiveAppointmentsForUser(user.getId());

        ModelAndView modelAndView = new ModelAndView("appointments");
        modelAndView.addObject("user", user);
        modelAndView.addObject("appointments", appointments);

        return modelAndView;
    }


}
