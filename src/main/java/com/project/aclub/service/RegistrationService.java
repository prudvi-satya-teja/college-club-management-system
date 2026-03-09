package com.project.aclub.service;

import com.project.aclub.dto.RegistrationRequest;
import com.project.aclub.entity.Event;
import com.project.aclub.entity.Registration;
import com.project.aclub.entity.User;
import com.project.aclub.repository.EventRepository;
import com.project.aclub.repository.RegistrationRepository;
import com.project.aclub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public RegistrationService(RegistrationRepository registrationRepository,
                               UserRepository userRepository,
                               EventRepository eventRepository) {
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public boolean createRegistration(RegistrationRequest request) {
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        Optional<Event> eventOpt = eventRepository.findById(request.getEventId());

        if (userOpt.isEmpty() || eventOpt.isEmpty()) {
            return false;
        }

        Registration registration = new Registration();
        registration.setUser(userOpt.get());
        registration.setEvent(eventOpt.get());
        registration.setRating(request.getRating());
        registration.setFeedback(request.getFeedback());

        registrationRepository.save(registration);
        return true;
    }
}