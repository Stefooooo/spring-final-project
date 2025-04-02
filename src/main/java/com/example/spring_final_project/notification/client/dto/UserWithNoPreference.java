package com.example.spring_final_project.notification.client.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserWithNoPreference {

    private UUID id;

    private String email;

}
