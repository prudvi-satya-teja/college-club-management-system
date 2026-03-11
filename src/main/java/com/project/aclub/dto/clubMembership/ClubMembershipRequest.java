package com.project.aclub.dto.clubMembership;

import com.project.aclub.Enum.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClubMembershipRequest {
    @NotNull(message = "userid should not be empty")
    private Long userId;

    @NotNull(message = "clubid should not be empty")
    private Long clubId;

    @NotNull(message = "role should not be empty")
    private Role role;
}