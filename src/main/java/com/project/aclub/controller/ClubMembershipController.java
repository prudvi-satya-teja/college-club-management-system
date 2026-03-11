package com.project.aclub.controller;

import com.project.aclub.dto.PageResponse;
import com.project.aclub.dto.clubMembership.ClubMembershipRequest;
import com.project.aclub.dto.clubMembership.ClubMembershipResponse;
import com.project.aclub.service.ClubMembershipService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/club-memberships")
public class ClubMembershipController {
    private final ClubMembershipService clubMembershipService;

    public ClubMembershipController(ClubMembershipService clubMembershipService) {
        this.clubMembershipService = clubMembershipService;
    }

    @PostMapping
    public ResponseEntity<ClubMembershipResponse> createClubMembership(@Valid @RequestBody
                                                                      ClubMembershipRequest request) {
        ClubMembershipResponse clubMembershipResponse = clubMembershipService.createClubMembership(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(clubMembershipResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubMembershipResponse> getClubMembershipById(@PathVariable Long id) {
        ClubMembershipResponse clubMembershipResponse = clubMembershipService.getClubMembershipById(id);
        return ResponseEntity.ok(clubMembershipResponse);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ClubMembershipResponse>> getAllClubMemberships(
            @RequestParam(required = false) Long clubId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "role") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        PageResponse<ClubMembershipResponse> ClubMembershipResponses =
                clubMembershipService.getAllClubMemberships(clubId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ClubMembershipResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubMembershipResponse> updateClubMembershipById(
            @PathVariable Long id,
            @RequestBody ClubMembershipRequest clubMembershipRequest) {
        ClubMembershipResponse clubMembershipResponse =
                clubMembershipService.updateClubMembershipById(id, clubMembershipRequest);
        return ResponseEntity.ok(clubMembershipResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClubMembershipById(@PathVariable Long id) {
        clubMembershipService.deleteClubMembershipById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
