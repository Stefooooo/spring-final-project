package com.example.spring_final_project.scheduler;

import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.User.service.UserService;
import com.example.spring_final_project.notification.client.dto.UserWithNoPreference;
import com.example.spring_final_project.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationPreferenceCreatorSchedulerTest {

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationPreferenceCreatorScheduler notificationPreferenceCreatorScheduler;

    @Test
    void testCheckAndAddMissingUserPreferences() {
        // Given
        UUID user1Id = UUID.randomUUID();
        User user1 = User.builder()
                .id(user1Id)
                .email("test1@example.com")
                .build();

        UUID user2Id = UUID.randomUUID();
        User user2 = User.builder()
                .id(user2Id)
                .email("test2@example.com")
                .build();

        List<User> users = List.of(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);

        // When
        notificationPreferenceCreatorScheduler.renewSubscriptions();

        // Then
        verify(notificationService).addUserPreferenceToAddIfNotExistent(
                UserWithNoPreference.builder().id(user1Id).email("test1@example.com").build());
        verify(notificationService).addUserPreferenceToAddIfNotExistent(
                UserWithNoPreference.builder().id(user2Id).email("test2@example.com").build());
    }
}