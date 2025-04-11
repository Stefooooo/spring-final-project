package com.example.spring_final_project.notification.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPreference {

    private String type;

    private boolean enabled;

    private String contactInfo;

}
