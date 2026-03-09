package com.project.aclub.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;
}
