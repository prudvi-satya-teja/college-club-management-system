package com.project.aclub.service;

import com.project.aclub.dto.PageResponse;
import com.project.aclub.dto.clubMembership.ClubMembershipRequest;
import com.project.aclub.dto.clubMembership.ClubMembershipResponse;
import com.project.aclub.entity.Club;
import com.project.aclub.entity.ClubMembership;
import com.project.aclub.entity.User;
import com.project.aclub.exception.ConflictException;
import com.project.aclub.exception.ResourceNotFoundException;
import com.project.aclub.repository.ClubRepository;
import com.project.aclub.repository.ClubMembershipRepository;
import com.project.aclub.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubMembershipService {
    private final ClubMembershipRepository clubMembershipRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    public ClubMembershipService(ClubMembershipRepository clubMembershipRepository,
                                 UserRepository userRepository,
                                 ClubRepository clubRepository) {
        this.clubMembershipRepository = clubMembershipRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
    }

    public ClubMembershipResponse createClubMembership(ClubMembershipRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() ->
                new ResourceNotFoundException("User not exists with this id"));
        Club club = clubRepository.findById(request.getClubId()).orElseThrow(() ->
                new ResourceNotFoundException("Club not exists with this id"));

        if (clubMembershipRepository.existsByUserAndClub(user, club)) {
            throw new ConflictException("ClubMembership found with user and club");
        }

        ClubMembership clubMembership = new ClubMembership();
        clubMembership.setUser(user);
        clubMembership.setClub(club);
        clubMembership.setRole(request.getRole());
        ClubMembership savedClubMembership = clubMembershipRepository.save(clubMembership);
        return ClubMembershipResponse.toDto(savedClubMembership);
    }

    public ClubMembershipResponse getClubMembershipById(Long id) {
        ClubMembership clubMembership = clubMembershipRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("ClubMembership not exists with this id"));

        return ClubMembershipResponse.toDto(clubMembership);
    }

    public PageResponse<ClubMembershipResponse> getAllClubMemberships(Long clubId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ClubMembership> ClubMembershipsPage;
        if (clubId == null) {
            ClubMembershipsPage = clubMembershipRepository.findAll(pageable);
        } else {
            Club club = clubRepository.findById(clubId).orElseThrow(() ->
                    new ResourceNotFoundException("Club not exists with this id"));
            ClubMembershipsPage = clubMembershipRepository.findClubMembershipByClub(club, pageable);
        }

        List<ClubMembershipResponse> clubMembershipRespons = ClubMembershipsPage.getContent().stream()
                .map(clubMembership -> ClubMembershipResponse.toDto(clubMembership))
                .toList();

        PageResponse<ClubMembershipResponse> pageResponse = new PageResponse<>(
                clubMembershipRespons,
                ClubMembershipsPage.getNumber(),
                ClubMembershipsPage.getSize(),
                ClubMembershipsPage.getTotalPages(),
                ClubMembershipsPage.getTotalElements());

        return pageResponse;
    }

    public ClubMembershipResponse updateClubMembershipById(Long id, ClubMembershipRequest clubMembershipRequest) {
        ClubMembership clubMembership = clubMembershipRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("ClubMembership not exists with this id"));
        User user = userRepository.findById(clubMembershipRequest.getUserId()).orElseThrow(() ->
                new ResourceNotFoundException("User not exists with this id"));
        Club club = clubRepository.findById(clubMembershipRequest.getClubId()).orElseThrow(() ->
                new ResourceNotFoundException("Club not exists with this id"));
        if (clubMembershipRepository.existsByUserAndClubAndRole(user, club, clubMembershipRequest.getRole())) {
            throw new ConflictException("ClubMembership found with user and club");
        }

        clubMembership.setClub(club);
        clubMembership.setUser(user);
        clubMembership.setRole(clubMembershipRequest.getRole());
        ClubMembership updatedClubMembership = clubMembershipRepository.save(clubMembership);

        return ClubMembershipResponse.toDto(updatedClubMembership);
    }

    public void deleteClubMembershipById(Long id) {
        ClubMembership clubMembership = clubMembershipRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("ClubMembership not exists with this id"));

        clubMembershipRepository.delete(clubMembership);
    }
}
