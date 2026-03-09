package com.project.aclub.controller;

import com.project.aclub.dto.ParticipationRequest;
import com.project.aclub.service.ParticipationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/participation")
public class ParticipationController {
    private final ParticipationService participationService;

    public ParticipationController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @PostMapping("/")
    public ResponseEntity<String> createParticipation(@RequestBody ParticipationRequest request) {
        boolean success = participationService.createParticipation(request);
        if (success) {
            return ResponseEntity.ok("Participation created successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid user or club ID");
        }
    }

}
