package com.example.spring_final_project.notification.client.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class NotificationPreference {

    private String type;

    private boolean enabled;

    private String contactInfo;

}
