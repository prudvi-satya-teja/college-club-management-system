package com.project.aclub.dto.participation;

import com.project.aclub.Enum.Role;
import com.project.aclub.dto.club.ClubResponse;
import com.project.aclub.dto.user.UserResponse;
import com.project.aclub.entity.Club;
import com.project.aclub.entity.Participation;
import com.project.aclub.entity.User;
import lombok.Data;

@Data
public class ParticipationResponse {
    private Long participationId;
    private UserResponse userResponse;
    private ClubResponse clubResponse;
    private Role role;

    public static ParticipationResponse toDto(Participation participation) {
        ParticipationResponse participationResponse = new ParticipationResponse();
        participationResponse.setParticipationId(participation.getId());
        participationResponse.setUserResponse(UserResponse.toResponse(participation.getUser()));
        participationResponse.setClubResponse(ClubResponse.toDTO(participation.getClub()));
        participationResponse.setRole(participation.getRole());
        return participationResponse;
    }
}
