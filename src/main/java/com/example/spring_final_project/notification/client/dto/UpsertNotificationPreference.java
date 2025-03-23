package com.example.spring_final_project.notification.client.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UpsertNotificationPreference {


    private UUID userId;

    private boolean notificationEnabled;

    private String type;

    private String contactInfo;
}
