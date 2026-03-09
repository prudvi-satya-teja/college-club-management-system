package com.project.aclub.controller;

import com.project.aclub.dto.auth.ResetPasswordRequest;
import com.project.aclub.dto.auth.SendOtpRequest;
import com.project.aclub.dto.auth.VerifyOtpRequest;
import com.project.aclub.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forgot-password")
public class ForgotPasswordController {
    private final ForgotPasswordService forgotPasswordService;

    @Autowired
    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody SendOtpRequest request) {
        forgotPasswordService.sendOtp(request.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("OTP sent successfully");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequest request) {
        forgotPasswordService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.status(HttpStatus.OK).body("OTP verified");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        forgotPasswordService.resetPassword(request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body("Password reset successful");
    }
}