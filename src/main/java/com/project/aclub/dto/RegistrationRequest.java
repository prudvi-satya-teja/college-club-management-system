package com.project.aclub.dto;

import lombok.Data;

@Data
public class RegistrationRequest {
    private Long userId;
    private Long eventId;
    private int rating;
    private String feedback;
}