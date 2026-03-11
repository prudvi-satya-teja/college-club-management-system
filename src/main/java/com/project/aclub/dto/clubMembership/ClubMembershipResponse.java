package com.project.aclub.dto.clubMembership;

import com.project.aclub.Enum.Role;
import com.project.aclub.dto.club.ClubResponse;
import com.project.aclub.dto.user.UserResponse;
import com.project.aclub.entity.ClubMembership;
import lombok.Data;

@Data
public class ClubMembershipResponse {
    private Long participationId;
    private UserResponse userResponse;
    private ClubResponse clubResponse;
    private Role role;

    public static ClubMembershipResponse toDto(ClubMembership clubMembership) {
        ClubMembershipResponse clubMembershipResponse = new ClubMembershipResponse();
        clubMembershipResponse.setParticipationId(clubMembership.getId());
        clubMembershipResponse.setUserResponse(UserResponse.toResponse(clubMembership.getUser()));
        clubMembershipResponse.setClubResponse(ClubResponse.toDTO(clubMembership.getClub()));
        clubMembershipResponse.setRole(clubMembership.getRole());
        return clubMembershipResponse;
    }
}
