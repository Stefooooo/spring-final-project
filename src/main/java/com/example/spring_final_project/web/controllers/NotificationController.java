package com.example.spring_final_project.web.controllers;


import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.notification.client.dto.Notification;
import com.example.spring_final_project.notification.client.dto.NotificationPreference;
import com.example.spring_final_project.notification.service.NotificationService;
import com.example.spring_final_project.security.UserAuthenticationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(UserService userService, NotificationService notificationService) {

        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public ModelAndView getNotificationPage(@AuthenticationPrincipal UserAuthenticationData userAuthenticationData) {

        User user = userService.getById(userAuthenticationData.getUserId());

        NotificationPreference notificationPreference = notificationService.getUserPreference(user.getId());
        List<Notification> notificationHistory = notificationService.getNotificationHistory(user.getId());
        long succeededNotificationsNumber = notificationHistory.stream().filter(notification -> notification.getStatus().equals("SUCCEEDED")).count();
        long failedNotificationsNumber = notificationHistory.stream().filter(notification -> notification.getStatus().equals("FAILED")).count();
        notificationHistory = notificationHistory.stream().limit(5).toList();

        ModelAndView modelAndView = new ModelAndView("notifications");
        modelAndView.addObject("user", user);
        modelAndView.addObject("notificationPreference", notificationPreference);
        modelAndView.addObject("succeededNotificationsNumber", succeededNotificationsNumber);
        modelAndView.addObject("failedNotificationsNumber", failedNotificationsNumber);
        modelAndView.addObject("notificationHistory", notificationHistory);

        return modelAndView;
    }

    @PutMapping("/user-preference")
    public String updateUserPreference(@RequestParam(name = "enabled") boolean enabled, @AuthenticationPrincipal UserAuthenticationData authenticationMetadata) {

        notificationService.updateNotificationPreference(authenticationMetadata.getUserId(), enabled);

        return "redirect:/notifications";
    }

    @DeleteMapping
    public String deleteNotificationHistory(@AuthenticationPrincipal UserAuthenticationData authenticationMetadata) {

        UUID userId = authenticationMetadata.getUserId();

        notificationService.clearHistory(userId);

        return "redirect:/notifications";
    }

    @PutMapping
    public String retryFailedNotifications(@AuthenticationPrincipal UserAuthenticationData authenticationMetadata) {

        UUID userId = authenticationMetadata.getUserId();

        notificationService.retryFailedNotifications(userId);

        return "redirect:/notifications";
    }
}