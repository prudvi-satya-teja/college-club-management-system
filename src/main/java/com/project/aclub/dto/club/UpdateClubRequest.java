package com.project.aclub.dto.club;

import com.project.aclub.entity.Club;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateClubRequest {
    @NotBlank
    String clubName;

    @NotBlank
    String clubCode;

    MultipartFile clubImageFile;

    public Club toEntity() {
        Club club = new Club();
        club.setClubName(this.clubName);
        club.setClubCode(this.clubCode);
        return club;
    }
}
