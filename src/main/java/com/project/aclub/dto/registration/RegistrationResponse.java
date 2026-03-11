package com.project.aclub.dto.registration;

import com.project.aclub.dto.event.EventResponse;
import com.project.aclub.dto.user.UserResponse;
import com.project.aclub.entity.Registration;
import lombok.Data;

@Data
public class RegistrationResponse {
    Long registrationId;
    UserResponse user;
    EventResponse event;
    String feedback;
    int rating;

    public static RegistrationResponse toDto(Registration savedRegistration) {
        RegistrationResponse registrationResponse = new RegistrationResponse();
        registrationResponse.registrationId = savedRegistration.getRegistrationId();
        registrationResponse.setUser(UserResponse.toResponse(savedRegistration.getUser()));
        registrationResponse.setEvent(EventResponse.toDto(savedRegistration.getEvent()));
        registrationResponse.setFeedback(savedRegistration.getFeedback());
        registrationResponse.setRating(savedRegistration.getRating());
        return registrationResponse;
    }
}
