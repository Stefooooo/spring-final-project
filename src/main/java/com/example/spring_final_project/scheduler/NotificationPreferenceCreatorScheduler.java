package com.example.spring_final_project.scheduler;

import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.notification.client.dto.NotificationPreference;
import com.example.spring_final_project.notification.client.dto.UserWithNoPreference;
import com.example.spring_final_project.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class NotificationPreferenceCreatorScheduler {

    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public NotificationPreferenceCreatorScheduler(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

//    @Scheduled(cron = "* 0 2 * * *")
    @Scheduled(fixedDelay = 200000)
    public void renewSubscriptions() {

        List<User> users = userService.getAllUsers();

        for (User user : users) {

            UserWithNoPreference userWithNoPreference = UserWithNoPreference.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .build();

            notificationService.addUserPreferenceToAddIfNotExistent(userWithNoPreference);

        }

    }
}
