package com.example.spring_final_project.notification.service;


import com.example.spring_final_project.notification.client.NotificationClient;
import com.example.spring_final_project.notification.client.dto.UpsertNotificationPreference;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationClient notificationClient;

//    @Value("${notification_msvc.failure-message.clear-history}")
    private String clearHistoryFailedMessage;

    @Autowired
    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void saveNotificationPreference(UUID id, boolean isEnabled, String email) {

        UpsertNotificationPreference upsertNotificationPreference = UpsertNotificationPreference.builder()
                .type("EMAIL")
                .userId(id)
                .notificationEnabled(isEnabled)
                .contactInfo(email)
                .build();


        ResponseEntity<Void> httpResponse = notificationClient.upsertNotificationPreference(upsertNotificationPreference);

    }
}
