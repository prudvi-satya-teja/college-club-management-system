package com.project.aclub.controller;

import com.project.aclub.dto.PageResponse;
import com.project.aclub.dto.participation.ParticipationRequest;
import com.project.aclub.dto.participation.ParticipationResponse;
import com.project.aclub.service.ParticipationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/participations")
public class ParticipationController {
    private final ParticipationService participationService;

    public ParticipationController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @PostMapping
    public ResponseEntity<ParticipationResponse> createParticipation(@Valid @RequestBody
                                                                     ParticipationRequest request) {
        ParticipationResponse participationResponse = participationService.createParticipation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(participationResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipationResponse> getParticipationById(@PathVariable Long id) {
        ParticipationResponse participationResponse = participationService.getParticipationById(id);
        return ResponseEntity.ok(participationResponse);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ParticipationResponse>> getAllParticipations(
            @RequestParam(required = false) Long clubId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "role") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        PageResponse<ParticipationResponse> participationResponses =
                participationService.getAllParticipations(clubId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(participationResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParticipationResponse> updateParticipationById(
            @PathVariable Long id,
            @RequestBody ParticipationRequest participationRequest) {
        ParticipationResponse participationResponse =
                participationService.updateParticipationById(id, participationRequest);
        return ResponseEntity.ok(participationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipationById(@PathVariable Long id) {
        participationService.deleteParticipationById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
