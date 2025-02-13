package com.example.spring_final_project.web.controllers;

import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.web.dto.UserLoginRequest;
import com.example.spring_final_project.web.dto.UserRegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
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
    public ModelAndView getLoginPage(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("userLoginRequest", new UserLoginRequest());

        return modelAndView;
    }

}
