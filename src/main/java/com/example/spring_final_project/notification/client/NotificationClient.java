package com.example.spring_final_project.notification.client;

import com.example.spring_final_project.notification.client.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "notification-msvc", url = "${notification-msvc.base-url}")
public interface NotificationClient {

    @PostMapping("/preferences")
    ResponseEntity<Void> upsertNotificationPreference(@RequestBody UpsertNotificationPreference notificationPreference);

    @GetMapping("/preferences")
    ResponseEntity<NotificationPreference> getUserPreference(@RequestParam(name = "userId") UUID userId);

    @PostMapping("/preferences/schedule")
    ResponseEntity<Void> addUserPreferenceToAddIfNotExistent(@RequestBody UserWithNoPreference userWithNoPreference);

    @GetMapping
    ResponseEntity<List<Notification>> getNotificationHistory(@RequestParam(name = "userId")UUID userId);

    @PostMapping
    ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest notificationRequest);

    @PutMapping("/preferences")
    ResponseEntity<Void> updateNotificationPreference(@RequestParam("userId") UUID userId, @RequestParam("enabled") boolean enabled);

    @DeleteMapping
    ResponseEntity<Void> clearHistory(@RequestParam(name = "userId") UUID userId);

    @PutMapping
    ResponseEntity<Void> retryFailedNotifications(@RequestParam(name = "userId") UUID userId);

}
