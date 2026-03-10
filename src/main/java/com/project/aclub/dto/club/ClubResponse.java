package com.project.aclub.dto.club;

import com.project.aclub.entity.Club;
import lombok.Data;

@Data
public class ClubResponse {
    Long clubId;
    String clubName;
    String clubCode;
    String clubImage;

    public static ClubResponse toDTO(Club club) {
        ClubResponse clubResponse = new ClubResponse();
        clubResponse.setClubId(club.getClubId());
        clubResponse.setClubCode(club.getClubCode());
        clubResponse.setClubName(club.getClubName());
        clubResponse.setClubImage(club.getClubImage());
        return clubResponse;
    }
}
