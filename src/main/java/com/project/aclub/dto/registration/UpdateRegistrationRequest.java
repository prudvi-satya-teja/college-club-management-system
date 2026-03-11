package com.project.aclub.dto.registration;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRegistrationRequest {
    @NotNull(message = "user id must not be null")
    private Long userId;

    @NotNull(message = "user id must not be null")
    private Long eventId;

    private String feedback;
    private int rating;
}
