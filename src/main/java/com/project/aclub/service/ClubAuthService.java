package com.project.aclub.service;

import com.project.aclub.Enum.Role;
import com.project.aclub.entity.Club;
import com.project.aclub.entity.Event;
import com.project.aclub.entity.User;
import com.project.aclub.exception.ResourceNotFoundException;
import com.project.aclub.repository.ClubMembershipRepository;
import com.project.aclub.repository.ClubRepository;
import com.project.aclub.repository.EventRepository;
import com.project.aclub.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ClubAuthService {
    private final ClubMembershipRepository clubMembershipRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public ClubAuthService(ClubMembershipRepository clubMembershipRepository, ClubRepository clubRepository,
                           UserRepository userRepository, EventRepository eventRepository) {
        this.clubMembershipRepository = clubMembershipRepository;
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public boolean isClubAdmin(Authentication authentication, Long clubId) {
        System.out.println(authentication.getPrincipal().toString());
        User user = userRepository.findByEmail(authentication.getPrincipal().toString()).orElseThrow(()->
                new ResourceNotFoundException("user not found with this principal"));

        Club club = clubRepository.findById(clubId).orElseThrow(() ->
                new ResourceNotFoundException("club not exist with this id"));

        return clubMembershipRepository
                .findByUserAndClub(user, club)
                .map(clubMembership -> clubMembership.getRole() == Role.ADMIN)
                .orElse(false);
    }

    public boolean isClubAdminByAuthentication(Authentication authentication) {
        System.out.println(authentication.getPrincipal().toString());
        User user = userRepository.findByEmail(authentication.getPrincipal().toString()).orElseThrow(()->
                new ResourceNotFoundException("user not found with this principal"));


        return clubMembershipRepository
                        .findByUser(user)
                .stream()
                .anyMatch(m -> m.getRole() == Role.ADMIN);
    }

    public boolean isClubCoordinator(Authentication authentication, Long clubId) {
        User user = (User) authentication.getPrincipal();

        Club club = clubRepository.findById(clubId).orElseThrow(() ->
                new ResourceNotFoundException("club not exist with this id"));

        return clubMembershipRepository
                .findByUserAndClub(user, club)
                .map(clubMembership -> clubMembership.getRole() == Role.COORDINATOR)
                .orElse(false);
    }

    public boolean isClubMember(Authentication authentication, Long clubId) {
        User user = (User) authentication.getPrincipal();

        Club club = clubRepository.findById(clubId).orElseThrow(() ->
                new ResourceNotFoundException("club not exist with this id"));

        return clubMembershipRepository
                .findByUserAndClub(user, club)
                .map(clubMembership -> clubMembership.getRole() == Role.MEMBER)
                .orElse(false);
    }

    public boolean isClubAdminByEventId(Authentication authentication, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(()->
                new ResourceNotFoundException("event not exists with this id"));

        return isClubAdmin(authentication, event.getClub().getClubId());
    }
}
