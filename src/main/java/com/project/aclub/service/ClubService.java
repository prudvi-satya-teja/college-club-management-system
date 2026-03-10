package com.project.aclub.service;

import com.project.aclub.dto.club.ClubResponse;
import com.project.aclub.dto.club.CreateClubRequest;
import com.project.aclub.dto.club.UpdateClubRequest;
import com.project.aclub.entity.Club;
import com.project.aclub.exception.ConflictException;
import com.project.aclub.exception.ResourceNotFoundException;
import com.project.aclub.repository.ClubRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {
    private final ClubRepository clubRepository;
    private final CloudinaryService cloudinaryService;

    public ClubService(ClubRepository clubRepository, CloudinaryService cloudinaryService) {
        this.clubRepository = clubRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public ClubResponse registerClub(CreateClubRequest clubRequest) {
        if (clubRepository.existsByClubCodeOrClubName(clubRequest.getClubCode(), clubRequest.getClubName())) {
            throw new ConflictException("Club exists with same name or code");
        }

        String imageUrl = cloudinaryService.uploadImage(clubRequest.getClubImageFile());

        Club club = clubRequest.toEntity();
        club.setClubImage(imageUrl);
        Club createdClub = clubRepository.save(club);

        return ClubResponse.toDTO(createdClub);
    }

    public List<ClubResponse> getAllClubs() {
        List<Club> clubs = clubRepository.findAll();

        return clubs.stream()
                .map(club -> ClubResponse.toDTO(club))
                .toList();
    }

    public ClubResponse getClubById(Long id) {
        Club club = clubRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("club not exists with id : " + id));

        return ClubResponse.toDTO(club);
    }


    public ClubResponse updateClubById(Long id, UpdateClubRequest clubRequest) {
        Club existingClub = clubRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Club not exists with id : " + id));

        if (clubRepository.existsByClubCodeOrClubNameAndClubIdNot(clubRequest.getClubCode(),
                clubRequest.getClubName(), id)) {
            throw new ConflictException("Club exists with same name or code");
        }

        if (clubRequest.getClubImageFile() != null && !clubRequest.getClubImageFile().isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(clubRequest.getClubImageFile());
            existingClub.setClubImage(imageUrl);
        }

        existingClub.setClubName(clubRequest.getClubName());
        existingClub.setClubCode(clubRequest.getClubCode());
        Club updatedClub = clubRepository.save(existingClub);

        return ClubResponse.toDTO(updatedClub);
    }

    public void deleteClubById(Long id) {
        Club club = clubRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Club not exists with this id : " + id));

        clubRepository.delete(club);
    }
}
