package com.project.aclub.controller;

import com.project.aclub.dto.PageResponse;
import com.project.aclub.dto.registration.FeedbackResponse;
import com.project.aclub.dto.registration.RegistrationRequest;
import com.project.aclub.dto.registration.RegistrationResponse;
import com.project.aclub.dto.registration.UpdateRegistrationRequest;
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
            @RequestParam(required = false) Long eventId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "registrationId") String sortBy,
            @RequestParam(defaultValue = "des") String sortDir

    ) {
        PageResponse<RegistrationResponse> registrationsPageResponse =
                registrationService.getAllRegistrations(eventId, page, size, sortBy, sortDir);
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