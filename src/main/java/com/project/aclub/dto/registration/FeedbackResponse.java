package com.project.aclub.dto.registration;

import com.project.aclub.dto.user.UserResponse;
import com.project.aclub.entity.Registration;
import lombok.Data;

@Data
public class FeedbackResponse {
    UserResponse user;
    String feedback;
    int rating;

    public static FeedbackResponse toDto(Registration registration) {
        FeedbackResponse feedbackResponse = new FeedbackResponse();
        feedbackResponse.setUser(UserResponse.toResponse(registration.getUser()));
        feedbackResponse.setFeedback(registration.getFeedback());
        feedbackResponse.setRating(registration.getRating());
        return feedbackResponse;
    }
}
