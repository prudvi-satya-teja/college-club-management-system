package com.project.aclub.service;

import com.project.aclub.entity.Club;
import com.project.aclub.repository.ClubRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {
    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public void save(Club club) {
        clubRepository.save(club);
    }

    public List<Club> find() {
        return clubRepository.findAll();
    }

    public Club findById(Long id) {
        return clubRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Club not found"));
    }

    public void deleteById(Long id) {
        clubRepository.deleteById(id);
    }

    public void updateClubById(Long id, Club club) {
        Club existingClub = clubRepository.findById(id).orElseThrow(()->
            new RuntimeException("Club not found"));
        existingClub.setClubName(club.getClubName());
        existingClub.setClubImage(club.getClubImage());
        clubRepository.save(existingClub);
    }
}
