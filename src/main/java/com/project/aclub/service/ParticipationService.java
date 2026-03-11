package com.project.aclub.service;

import com.project.aclub.dto.PageResponse;
import com.project.aclub.dto.participation.ParticipationRequest;
import com.project.aclub.dto.participation.ParticipationResponse;
import com.project.aclub.entity.Club;
import com.project.aclub.entity.Participation;
import com.project.aclub.entity.User;
import com.project.aclub.exception.ConflictException;
import com.project.aclub.exception.ResourceNotFoundException;
import com.project.aclub.repository.ClubRepository;
import com.project.aclub.repository.ParticipationRepository;
import com.project.aclub.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    public ParticipationService(ParticipationRepository participationRepository,
                                UserRepository userRepository,
                                ClubRepository clubRepository) {
        this.participationRepository = participationRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
    }

    public ParticipationResponse createParticipation(ParticipationRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() ->
                new ResourceNotFoundException("User not exists with this id"));
        Club club = clubRepository.findById(request.getClubId()).orElseThrow(() ->
                new ResourceNotFoundException("Club not exists with this id"));

        if (participationRepository.existsByUserAndClub(user, club)) {
            throw new ConflictException("Participation found with user and club");
        }

        Participation participation = new Participation();
        participation.setUser(user);
        participation.setClub(club);
        participation.setRole(request.getRole());
        Participation savedParticipation = participationRepository.save(participation);
        return ParticipationResponse.toDto(savedParticipation);
    }

    public ParticipationResponse getParticipationById(Long id) {
        Participation participation = participationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Participation not exists with this id"));

        return ParticipationResponse.toDto(participation);
    }

    public PageResponse<ParticipationResponse> getAllParticipations(Long clubId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Participation> participationsPage;
        if (clubId == null) {
            participationsPage = participationRepository.findAll(pageable);
        } else {
            Club club = clubRepository.findById(clubId).orElseThrow(() ->
                    new ResourceNotFoundException("Club not exists with this id"));
            participationsPage = participationRepository.findParticipationByClub(club, pageable);
        }

        List<ParticipationResponse> participationResponses = participationsPage.getContent().stream()
                .map(participation -> ParticipationResponse.toDto(participation))
                .toList();

        PageResponse<ParticipationResponse> pageResponse = new PageResponse<>(
                participationResponses,
                participationsPage.getNumber(),
                participationsPage.getSize(),
                participationsPage.getTotalPages(),
                participationsPage.getTotalElements());

        return pageResponse;
    }

    public ParticipationResponse updateParticipationById(Long id, ParticipationRequest participationRequest) {
        Participation participation = participationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Participation not exists with this id"));
        User user = userRepository.findById(participationRequest.getUserId()).orElseThrow(() ->
                new ResourceNotFoundException("User not exists with this id"));
        Club club = clubRepository.findById(participationRequest.getClubId()).orElseThrow(() ->
                new ResourceNotFoundException("Club not exists with this id"));
        if (participationRepository.existsByUserAndClubAndRole(user, club, participationRequest.getRole())) {
            throw new ConflictException("Participation found with user and club");
        }

        participation.setClub(club);
        participation.setUser(user);
        participation.setRole(participationRequest.getRole());
        Participation updatedParticipation = participationRepository.save(participation);

        return ParticipationResponse.toDto(updatedParticipation);
    }

    public void deleteParticipationById(Long id) {
        Participation participation = participationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Participation not exists with this id"));

        participationRepository.delete(participation);
    }
}
