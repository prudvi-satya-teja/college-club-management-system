package com.project.aclub.service;

import com.project.aclub.dto.PageResponse;
import com.project.aclub.dto.registration.FeedbackResponse;
import com.project.aclub.dto.registration.RegistrationRequest;
import com.project.aclub.dto.registration.RegistrationResponse;
import com.project.aclub.dto.registration.UpdateRegistrationRequest;
import com.project.aclub.entity.*;
import com.project.aclub.exception.ConflictException;
import com.project.aclub.exception.ResourceNotFoundException;
import com.project.aclub.repository.EventRepository;
import com.project.aclub.repository.RegistrationRepository;
import com.project.aclub.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public RegistrationResponse createRegistration(RegistrationRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() ->
                new ResourceNotFoundException("User not found with this id"));
        Event event = eventRepository.findById(request.getEventId()).orElseThrow(() ->
                new ResourceNotFoundException("Event not found with this id"));
        if (registrationRepository.existsByUserAndEvent(user, event)) {
            throw new ConflictException("Registration alreaddy found with this user and event");
        }
        ;

        Registration registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);
        Registration savedRegistration = registrationRepository.save(registration);

        return RegistrationResponse.toDto(savedRegistration);
    }

    public RegistrationResponse getRegistrationById(Long id) {
        Registration registration = registrationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Registration not found with this id"));
        return RegistrationResponse.toDto(registration);
    }

    public PageResponse<RegistrationResponse> getAllRegistrations(
            Long eventId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Registration> registrationsPage;
        if (eventId == null) {
            registrationsPage = registrationRepository.findAll(pageable);
        } else {
            Event event = eventRepository.findById(eventId).orElseThrow(() ->
                    new ResourceNotFoundException("Event not exists with this id"));
            registrationsPage = registrationRepository.findByEvent(event, pageable);
        }

        List<RegistrationResponse> registrations = registrationsPage.stream()
                .map(registration -> RegistrationResponse.toDto(registration))
                .toList();

        return new PageResponse<RegistrationResponse>(registrations, registrationsPage.getNumber(),
                registrationsPage.getSize(), registrationsPage.getTotalPages(), registrationsPage.getTotalElements());
    }

    public RegistrationResponse updateRegistrationById(Long id, UpdateRegistrationRequest registrationRequest) {
        Registration registration = registrationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Registration not exists with this id"));
        User user = userRepository.findById(registrationRequest.getUserId()).orElseThrow(() ->
                new ResourceNotFoundException("User not exists with this id"));
        Event event = eventRepository.findById(registrationRequest.getEventId()).orElseThrow(() ->
                new ResourceNotFoundException("Event not exists with this id"));
        if (registrationRepository.existsByUserAndEventAndRegistrationIdNot(user, event, id)) {
            throw new ConflictException("Registration found with user and club");
        }
        registration.setUser(user);
        registration.setEvent(event);
        registration.setFeedback(registrationRequest.getFeedback());
        registration.setRating(registrationRequest.getRating());
        Registration updatedRegistration = registrationRepository.save(registration);

        return RegistrationResponse.toDto(updatedRegistration);
    }

    public void deleteRegistrationById(Long id) {
        Registration registration = registrationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Participation not exists with this id"));

        registrationRepository.delete(registration);
    }

    public PageResponse<FeedbackResponse> getAllFeebacks(Long eventId, int page,
                                                         int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResourceNotFoundException("Event not exists with this id"));
        Page<Registration> registrationsPage = registrationRepository.findFeedbacksByEvent(eventId, pageable);

        List<FeedbackResponse> feedbacks = registrationsPage.stream()
                .map(registration -> FeedbackResponse.toDto(registration))
                .toList();

        return new PageResponse<FeedbackResponse>(feedbacks, registrationsPage.getNumber(),
                registrationsPage.getSize(), registrationsPage.getTotalPages(), registrationsPage.getTotalElements());
    }
}