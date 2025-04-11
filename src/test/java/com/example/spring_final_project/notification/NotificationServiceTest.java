package com.example.spring_final_project.notification;

import com.example.spring_final_project.notification.client.NotificationClient;
import com.example.spring_final_project.notification.client.dto.Notification;
import com.example.spring_final_project.notification.client.dto.NotificationPreference;
import com.example.spring_final_project.notification.client.dto.UpsertNotificationPreference;
import com.example.spring_final_project.notification.client.dto.UserWithNoPreference;
import com.example.spring_final_project.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    NotificationClient notificationClient;

    @InjectMocks
    NotificationService notificationService;

    @Test
    void testSaveNotificationPreference_Success() {
        UUID userId = UUID.randomUUID();
        String email = "test@example.com";

        UpsertNotificationPreference expectedRequest = UpsertNotificationPreference.builder()
                .type("EMAIL")
                .userId(userId)
                .notificationEnabled(true)
                .contactInfo(email)
                .build();

        when(notificationClient.upsertNotificationPreference(any()))
                .thenReturn(ResponseEntity.ok().build());

        notificationService.saveNotificationPreference(userId, true, email);

        verify(notificationClient).upsertNotificationPreference(argThat(actual ->
                actual.getUserId().equals(expectedRequest.getUserId()) &&
                        actual.getType().equals("EMAIL") &&
                        actual.isNotificationEnabled() == true &&
                        actual.getContactInfo().equals(email)
        ));
    }

    @Test
    void testSaveNotificationPreference_FailureResponse() {
        UUID userId = UUID.randomUUID();

        when(notificationClient.upsertNotificationPreference(any()))
                .thenReturn(ResponseEntity.status(500).build());

        notificationService.saveNotificationPreference(userId, false, "fail@example.com");

        // No exception thrown = success. Log error would be printed.
        verify(notificationClient).upsertNotificationPreference(any());
    }

    @Test
    void testSaveNotificationPreference_ExceptionThrown() {
        UUID userId = UUID.randomUUID();

        when(notificationClient.upsertNotificationPreference(any()))
                .thenThrow(new RuntimeException("Feign boom"));

        notificationService.saveNotificationPreference(userId, true, "oops@example.com");

        // Testing that it fails gracefully.
        verify(notificationClient).upsertNotificationPreference(any());
    }

    @Test
    void testSaveNotificationPreferenceWithoutIsEnabled_SuccessfulCall() {
        UUID userId = UUID.randomUUID();
        String email = "test@example.com";

        when(notificationClient.upsertNotificationPreference(any()))
                .thenReturn(ResponseEntity.ok().build());

        notificationService.saveNotificationPreference(userId, email);

        verify(notificationClient).upsertNotificationPreference(argThat(pref ->
                pref.getUserId().equals(userId) &&
                        pref.getContactInfo().equals(email) &&
                        "EMAIL".equals(pref.getType())
        ));
    }

    @Test
    void testSaveNotificationPreferenceWithoutIsEnabled_FailedResponse() {
        UUID userId = UUID.randomUUID();
        String email = "fail@example.com";

        when(notificationClient.upsertNotificationPreference(any()))
                .thenReturn(ResponseEntity.status(500).build());

        notificationService.saveNotificationPreference(userId, email);

        verify(notificationClient).upsertNotificationPreference(any());
    }

    @Test
    void testSaveNotificationPreferenceWithoutIsEnabled_ExceptionThrown() {
        UUID userId = UUID.randomUUID();
        String email = "exception@example.com";

        when(notificationClient.upsertNotificationPreference(any()))
                .thenThrow(new RuntimeException("Feign is down"));

        notificationService.saveNotificationPreference(userId, email);

        verify(notificationClient).upsertNotificationPreference(any());
    }

    @Test
    void testGetUserPreference_SuccessfulResponse() {
        UUID userId = UUID.randomUUID();
        NotificationPreference expectedPreference = new NotificationPreference();


        when(notificationClient.getUserPreference(userId))
                .thenReturn(ResponseEntity.ok(expectedPreference));

        NotificationPreference result = notificationService.getUserPreference(userId);

        assertNotNull(result);
        verify(notificationClient).getUserPreference(userId);
    }

    @Test
    void testGetUserPreference_Non2xxResponse() {
        UUID userId = UUID.randomUUID();
        NotificationPreference preference = new NotificationPreference();


        when(notificationClient.getUserPreference(userId))
                .thenReturn(ResponseEntity.status(500).body(preference));

        NotificationPreference result = notificationService.getUserPreference(userId);

        // Should still return body despite 500, per current logic
        assertNotNull(result);
        verify(notificationClient).getUserPreference(userId);
    }

    @Test
    void testGetUserPreference_ThrowsException() {
        UUID userId = UUID.randomUUID();

        when(notificationClient.getUserPreference(userId))
                .thenThrow(new RuntimeException("Feign down"));

        assertThrows(RuntimeException.class, () -> {
            notificationService.getUserPreference(userId);
        });

        verify(notificationClient).getUserPreference(userId);
    }

    @Test
    void testAddUserPreferenceToAddIfNotExistent_Success() {
        UserWithNoPreference user = UserWithNoPreference.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .build();

        when(notificationClient.addUserPreferenceToAddIfNotExistent(any()))
                .thenReturn(ResponseEntity.ok().build());

        notificationService.addUserPreferenceToAddIfNotExistent(user);

        verify(notificationClient).addUserPreferenceToAddIfNotExistent(user);
    }

    @Test
    void testAddUserPreferenceToAddIfNotExistent_Failure() {
        UserWithNoPreference user = UserWithNoPreference.builder()
                .id(UUID.randomUUID())
                .email("fail@example.com")
                .build();

        when(notificationClient.addUserPreferenceToAddIfNotExistent(any()))
                .thenReturn(ResponseEntity.status(500).build());

        notificationService.addUserPreferenceToAddIfNotExistent(user);

        verify(notificationClient).addUserPreferenceToAddIfNotExistent(user);
    }

    @Test
    void testGetNotificationHistory_ReturnsList() {
        UUID userId = UUID.randomUUID();
        List<Notification> expectedList = List.of(new Notification(), new Notification());

        when(notificationClient.getNotificationHistory(userId))
                .thenReturn(ResponseEntity.ok(expectedList));

        List<Notification> result = notificationService.getNotificationHistory(userId);

        assertEquals(expectedList, result);
        verify(notificationClient).getNotificationHistory(userId);
    }

    @Test
    void testSendNotification_Success() {
        UUID userId = UUID.randomUUID();

        when(notificationClient.sendNotification(any()))
                .thenReturn(ResponseEntity.ok().build());

        notificationService.sendNotification(userId, "Subject", "Body");

        verify(notificationClient).sendNotification(argThat(req ->
                req.getUserId().equals(userId) &&
                        req.getSubject().equals("Subject") &&
                        req.getBody().equals("Body")
        ));
    }

    @Test
    void testSendNotification_FailureResponse() {
        UUID userId = UUID.randomUUID();

        when(notificationClient.sendNotification(any()))
                .thenReturn(ResponseEntity.status(500).build());

        notificationService.sendNotification(userId, "Fail", "Fail Body");

        verify(notificationClient).sendNotification(any());
    }

    @Test
    void testSendNotification_ThrowsException() {
        UUID userId = UUID.randomUUID();

        when(notificationClient.sendNotification(any()))
                .thenThrow(new RuntimeException("Internal Error"));

        notificationService.sendNotification(userId, "Subject", "Body");

        verify(notificationClient).sendNotification(any());
    }

    @Test
    void testUpdateNotificationPreference_Success() {
        UUID userId = UUID.randomUUID();
        notificationService.updateNotificationPreference(userId, true);

        verify(notificationClient).updateNotificationPreference(userId, true);
    }

    @Test
    void testUpdateNotificationPreference_ThrowsException() {
        UUID userId = UUID.randomUUID();

        doThrow(new RuntimeException("Error")).when(notificationClient)
                .updateNotificationPreference(userId, false);

        notificationService.updateNotificationPreference(userId, false);

        verify(notificationClient).updateNotificationPreference(userId, false);
    }

    @Test
    void testClearHistory_Success() {
        UUID userId = UUID.randomUUID();
        notificationService.clearHistory(userId);

        verify(notificationClient).clearHistory(userId);
    }

    @Test
    void testClearHistory_ThrowsException() {
        UUID userId = UUID.randomUUID();

        doThrow(new RuntimeException("Boom")).when(notificationClient).clearHistory(userId);

        notificationService.clearHistory(userId);

        verify(notificationClient).clearHistory(userId);
    }

    @Test
    void testRetryFailedNotifications_Success() {
        UUID userId = UUID.randomUUID();
        notificationService.retryFailedNotifications(userId);

        verify(notificationClient).retryFailedNotifications(userId);
    }

    @Test
    void testRetryFailedNotifications_ThrowsException() {
        UUID userId = UUID.randomUUID();

        doThrow(new RuntimeException("Fail")).when(notificationClient).retryFailedNotifications(userId);

        notificationService.retryFailedNotifications(userId);

        verify(notificationClient).retryFailedNotifications(userId);
    }

}