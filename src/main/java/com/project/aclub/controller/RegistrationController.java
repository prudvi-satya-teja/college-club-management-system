package com.project.aclub.controller;

import com.project.aclub.dto.RegistrationRequest;
import com.project.aclub.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/")
    public ResponseEntity<String> createRegistration(@RequestBody RegistrationRequest request) {
        boolean success = registrationService.createRegistration(request);
        if (success) {
            return ResponseEntity.ok("Registration created successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid user or event ID");
        }
    }
}