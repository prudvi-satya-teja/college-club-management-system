package com.project.aclub.controller;

import com.project.aclub.entity.Club;
import com.project.aclub.service.ClubService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClubController {
    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping("/club")
    public ResponseEntity<Void> createClub(@RequestBody Club club) {
        clubService.save(club);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/clubs")
    public ResponseEntity<List<Club>> fetchAllClubs() {
        List<Club> clubs = clubService.find();
        return ResponseEntity.status(HttpStatus.OK).body(clubs);
    }

    @GetMapping("/club/{id}")
    public ResponseEntity<Club> findClubById(@PathVariable Long id) {
        Club club = clubService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(club);
    }

    @DeleteMapping("/club/{id}")
    public ResponseEntity<Void> deleteClubById(@PathVariable Long id) {
        clubService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/club/{id}")
    public ResponseEntity<Void> updateClubById(@PathVariable Long id, @RequestBody Club club) {
        clubService.updateClubById(id, club);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
