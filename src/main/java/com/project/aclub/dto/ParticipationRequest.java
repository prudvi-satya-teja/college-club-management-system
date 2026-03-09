package com.project.aclub.dto;

import com.project.aclub.Enum.Role;
import lombok.Data;

@Data
public class ParticipationRequest {
    private Long userId;
    private Long clubId;
    private Role role;
}