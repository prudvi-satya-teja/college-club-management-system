package com.project.aclub.controller;

import com.project.aclub.dto.club.ClubResponse;
import com.project.aclub.dto.club.CreateClubRequest;
import com.project.aclub.dto.club.UpdateClubRequest;
import com.project.aclub.service.ClubService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clubs")
public class ClubController {
    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ClubResponse> registerClub(@ModelAttribute @Valid CreateClubRequest clubRequest) {
        ClubResponse clubResponse = clubService.registerClub(clubRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(clubResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubResponse> getClubById(@PathVariable Long id) {
        ClubResponse clubResponse = clubService.getClubById(id);
        return ResponseEntity.ok(clubResponse);
    }

    @GetMapping
    public ResponseEntity<List<ClubResponse>> getAllClubs() {
        List<ClubResponse> clubs = clubService.getAllClubs();
        return ResponseEntity.ok(clubs);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ClubResponse> updateClubById(@PathVariable Long id,
                                                       @ModelAttribute @Valid UpdateClubRequest clubRequest) {
        ClubResponse clubResponse = clubService.updateClubById(id, clubRequest);
        return ResponseEntity.ok(clubResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteClubById(@PathVariable Long id) {
        clubService.deleteClubById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
