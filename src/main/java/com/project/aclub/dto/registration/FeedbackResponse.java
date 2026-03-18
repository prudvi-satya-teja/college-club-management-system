package com.project.aclub.dto.registration;

import com.project.aclub.dto.user.UserResponse;
import com.project.aclub.entity.Registration;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackResponse {
    UserResponse user;
    Long registrationId;
    String feedback;
    int rating;
    LocalDateTime updatedAt;

    public static FeedbackResponse toDto(Registration registration) {
        FeedbackResponse feedbackResponse = new FeedbackResponse();
        feedbackResponse.setUser(UserResponse.toResponse(registration.getUser()));
        feedbackResponse.setFeedback(registration.getFeedback());
        feedbackResponse.setRating(registration.getRating());
        feedbackResponse.setRegistrationId(registration.getRegistrationId());
        feedbackResponse.setUpdatedAt(registration.getUpdatedAt());
        return feedbackResponse;
    }
}
