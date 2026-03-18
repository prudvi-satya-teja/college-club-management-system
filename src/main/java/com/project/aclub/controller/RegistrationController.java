package com.project.aclub.controller;

import com.project.aclub.dto.PageResponse;
import com.project.aclub.dto.registration.*;
import com.project.aclub.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<RegistrationResponse> createRegistration(@RequestBody RegistrationRequest request) {
        RegistrationResponse registrationResponse = registrationService.createRegistration(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistrationResponse> getRegistrationById(@PathVariable Long id) {
        RegistrationResponse registrationResponse = registrationService.getRegistrationById(id);
        return ResponseEntity.ok(registrationResponse);
    }

    @GetMapping
    public ResponseEntity<PageResponse<RegistrationResponse>> getAllRegistrations(
            @RequestParam(required=false) Long userId,
            @RequestParam(required = false) Long eventId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "registrationId") String sortBy,
            @RequestParam(defaultValue = "des") String sortDir

    ) {
        PageResponse<RegistrationResponse> registrationsPageResponse =
                registrationService.getAllRegistrations(userId, eventId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(registrationsPageResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistrationResponse> updateRegistrationById(
            @PathVariable Long id,
            @RequestBody UpdateRegistrationRequest registrationRequest) {
        RegistrationResponse registrationResponse =
                registrationService.updateRegistrationById(id, registrationRequest);
        return ResponseEntity.ok(registrationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistrationById(@PathVariable Long id) {
        registrationService.deleteRegistrationById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/feedback")
    public ResponseEntity<FeedbackResponse> saveFeedback(@PathVariable Long id,
                                                         @RequestBody FeedbackRequest feedbackRequest) {
        FeedbackResponse feedbackResponse = registrationService.saveFeedback(id, feedbackRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackResponse);
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<PageResponse<FeedbackResponse>> getAllFeedbacks(
            @RequestParam Long eventId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updated_at") String sortBy,
            @RequestParam(defaultValue = "des") String sortDir
    ) {
        PageResponse<FeedbackResponse> feedbackPageResponse =
                registrationService.getAllFeebacks(eventId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(feedbackPageResponse);
    }
}