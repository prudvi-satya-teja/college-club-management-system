package com.project.aclub.dto.registration;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrationRequest {
    @NotNull(message = "user id must not be null")
    private Long userId;
    @NotNull(message = "user id must not be null")
    private Long eventId;
}