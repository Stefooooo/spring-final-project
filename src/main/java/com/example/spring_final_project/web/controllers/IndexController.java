package com.example.spring_final_project.web.controllers;

import com.example.spring_final_project.Doctor.model.Doctor;
import com.example.spring_final_project.Doctor.service.DoctorService;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.security.UserAuthenticationData;
//import com.example.spring_final_project.security.authentication.CustomAuthentication;
import com.example.spring_final_project.web.dto.DoctorRegisterRequest;
import com.example.spring_final_project.web.dto.UserLoginRequest;
import com.example.spring_final_project.web.dto.UserRegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    private final UserService userService;
    private final DoctorService doctorService;

    @Autowired
    public IndexController(UserService userService, DoctorService doctorService) {
        this.userService = userService;
        this.doctorService = doctorService;
    }

    @GetMapping("/")
    public String getIndexPage(){

        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("userRegisterRequest", new UserRegisterRequest());

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerNewUser(@Valid UserRegisterRequest userRegisterRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ModelAndView("register");
        }

        userService.register(userRegisterRequest);

        return new ModelAndView("redirect:/login");

    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(value = "error", required = false) String errorParam) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("userLoginRequest", new UserLoginRequest());

        if (errorParam != null) {
            modelAndView.addObject("errorMessage", "Incorrect username or password!");
        }

        return modelAndView;
    }

//    @GetMapping("/register-doctor")
//    public ModelAndView getRegisterDoctorPage(){
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("doctors-register");
//        modelAndView.addObject("doctorRegisterRequest", new DoctorRegisterRequest());
//
//        return modelAndView;
//    }

//    @PostMapping("/register-doctor")
//    public ModelAndView registerNewDoctor(@Valid DoctorRegisterRequest doctorRegisterRequest, BindingResult bindingResult){
//
//        if(bindingResult.hasErrors()){
//            return new ModelAndView("doctors-register");
//        }
//
//        doctorService.register(doctorRegisterRequest);
//
//        return new ModelAndView("redirect:/login-doctor");
//
//    }

    @GetMapping("/home")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ModelAndView getHomePage(@AuthenticationPrincipal UserAuthenticationData userAuthenticationData){

        User user = userService.getById(userAuthenticationData.getUserId());

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("user", user);
        modelAndView.addObject("patientCard", user.getPatientCard());

        return modelAndView;
    }

    @GetMapping("/home-doctor")
    @PreAuthorize("hasRole('DOCTOR')")
    public ModelAndView getDoctorHomePage(@AuthenticationPrincipal UserAuthenticationData doctorAuthenticationData){

        Doctor doctor = doctorService.getById(doctorAuthenticationData.getUserId());

        ModelAndView modelAndView = new ModelAndView("home-doctor");
        modelAndView.addObject("doctor", doctor);

        return modelAndView;
    }

}
