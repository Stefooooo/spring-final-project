package com.example.spring_final_project.notification.service;


import com.example.spring_final_project.notification.client.NotificationClient;
import com.example.spring_final_project.notification.client.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class NotificationService {

    private final NotificationClient notificationClient;

//    @Value("${notification_msvc.failure-message.clear-history}")
    private String clearHistoryFailedMessage;

    @Autowired
    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    @CacheEvict(value = "notification_history", allEntries = true)
    public void saveNotificationPreference(UUID userId, boolean isEnabled, String email) {

        UpsertNotificationPreference upsertNotificationPreference = UpsertNotificationPreference.builder()
                .type("EMAIL")
                .userId(userId)
                .notificationEnabled(isEnabled)
                .contactInfo(email)
                .build();


        try {
            ResponseEntity<Void> httpResponse = notificationClient.upsertNotificationPreference(upsertNotificationPreference);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification_msvc failed] Can't save user preference for user with id = [%s]".formatted(userId));
            }
        } catch (Exception e) {
            log.error("Unable to call notification-msvc.");
        }

    }

    @CacheEvict(value = "notification_history", allEntries = true)
    public void saveNotificationPreference(UUID userId, String email) {

        UpsertNotificationPreference upsertNotificationPreference = UpsertNotificationPreference.builder()
                .type("EMAIL")
                .userId(userId)
                .contactInfo(email)
                .build();


        try {
            ResponseEntity<Void> httpResponse = notificationClient.upsertNotificationPreference(upsertNotificationPreference);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification_msvc failed] Can't save user preference for user with id = [%s]".formatted(userId));
            }
        } catch (Exception e) {
            log.error("Unable to call notification-msvc.");
        }

    }

    public NotificationPreference getUserPreference( UUID userId){

            ResponseEntity<NotificationPreference> httpResponse = notificationClient.getUserPreference(userId);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification_msvc failed] Can't get preference user with id = [%s]".formatted(userId));
            }
            return httpResponse.getBody();

    }


    public void addUserPreferenceToAddIfNotExistent(UserWithNoPreference userWithNoPreference){

            ResponseEntity<Void> httpResponse = notificationClient.addUserPreferenceToAddIfNotExistent(userWithNoPreference);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification_msvc failed] Can't add preference for user with id = [%s]".formatted(userWithNoPreference.getId()));
            }

    }

    @Cacheable("notification_history")
    public List<Notification> getNotificationHistory(UUID userId){
        ResponseEntity<List<Notification>> httpResponse = notificationClient.getNotificationHistory(userId);

        return httpResponse.getBody();
    }

    @CacheEvict(value = "notification_history", allEntries = true)
    public void sendNotification(UUID userId, String emailSubject, String emailBody) {

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(userId)
                .subject(emailSubject)
                .body(emailBody)
                .build();


        ResponseEntity<Void> httpResponse;
        try {
            httpResponse = notificationClient.sendNotification(notificationRequest);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification_msvc failed] Can't send email to user with id = [%s]".formatted(userId));
            }
        } catch (Exception e) {
            log.warn("Can't send email to user with id = [%s] due to 500 Internal Server Error.".formatted(userId));
        }
    }

    public void updateNotificationPreference( UUID userId, boolean enabled){
        try {
            notificationClient.updateNotificationPreference(userId, enabled);
        } catch (Exception e) {
            log.warn("Can't update notification preferences for user with id = [%s].".formatted(userId));
        }
    }

    @CacheEvict(value = "notification_history", allEntries = true)
    public void clearHistory(UUID userId){
        try {
            notificationClient.clearHistory(userId);
        } catch (Exception e) {
            log.warn("Can't clear history for user with id = [%s].".formatted(userId));
        }
    }

    @CacheEvict(value = "notification_history", allEntries = true)
    public void retryFailedNotifications( UUID userId){
        try {
            notificationClient.retryFailedNotifications(userId);
        } catch (Exception e) {
            log.warn("Can't retry failed notifications for user with id = [%s].".formatted(userId));
        }
    }

}
