package com.example.spring_final_project.web.controllers;

import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.security.UserAuthenticationData;
import com.example.spring_final_project.web.dto.EditUserProfileRequest;
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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/edit-profile")
    public ModelAndView getUserUpdatePage(@PathVariable UUID id){

        User user = userService.getById(id);

        ModelAndView modelAndView = new ModelAndView("edit-user-profile");
        modelAndView.addObject("editUserProfileRequest", new EditUserProfileRequest());
        modelAndView.addObject("user", user);


        return modelAndView;

    }

    @PutMapping("/{id}/edit-profile")
    public ModelAndView updateUserProfile(@PathVariable UUID id, @Valid EditUserProfileRequest editUserProfileRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            User user = userService.getById(id);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("edit-user-profile");
            modelAndView.addObject("user", user);
            modelAndView.addObject("editUserProfileRequest", editUserProfileRequest);
            return modelAndView;
        }

        userService.editUserDetails(id, editUserProfileRequest);

        return new ModelAndView("redirect:/home");
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public String switchUserStatus(@PathVariable UUID id) {

        userService.switchStatus(id);

        return "redirect:/users";
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public String switchUserRole(@PathVariable UUID id) {

        userService.switchRole(id);

        return "redirect:/users";
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAllUsersPage(@AuthenticationPrincipal UserAuthenticationData userAuthenticationData){

        User user = userService.getById(userAuthenticationData.getUserId());
        List<User> users = userService.getAllUsers();

        ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("user", user);
        modelAndView.addObject("users", users);

        return modelAndView;
    }


}
