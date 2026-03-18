package com.project.aclub.service;

import com.project.aclub.dto.PageResponse;
import com.project.aclub.dto.event.CreateEventRequest;
import com.project.aclub.dto.event.EventResponse;
import com.project.aclub.dto.event.UpdateEventRequest;
import com.project.aclub.entity.Club;
import com.project.aclub.entity.Event;
import com.project.aclub.exception.ResourceNotFoundException;
import com.project.aclub.repository.ClubRepository;
import com.project.aclub.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final ClubRepository clubRepository;
    private final CloudinaryService cloudinaryService;

    public EventService(EventRepository eventRepository, ClubRepository clubRepository,
                        CloudinaryService cloudinaryService) {
        this.eventRepository = eventRepository;
        this.clubRepository = clubRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public EventResponse registerEvent(CreateEventRequest eventRequest) {
        Club club = clubRepository.findById(eventRequest.getClubId()).orElseThrow(() ->
                new ResourceNotFoundException("club not found with this id"));
        String imageUrl = cloudinaryService.uploadImage(eventRequest.getEventImage());

        Event event = eventRequest.toEntity();
        event.setImage(imageUrl);
        event.setClub(club);
        eventRepository.save(event);

        return EventResponse.toDto(event);
    }

    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Event not found with this id"));

        return EventResponse.toDto(event);
    }

    public PageResponse<EventResponse> getAllEvents(Long clubId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Event> events;
        if (clubId == null) {
            events = eventRepository.findAll(pageable);
        } else {
            Club club = clubRepository.findById(clubId).orElseThrow(() ->
                    new ResourceNotFoundException("club with this id is not exist"));
            events = eventRepository.findByClub(club, pageable);
        }
        System.out.println(events.toString());

        List<EventResponse> eventResponses = events.stream()
                .map(event -> EventResponse.toDto(event))
                .toList();

        return new PageResponse<>(eventResponses, events.getNumber(), events.getSize(), events.getTotalPages(),
                events.getTotalElements());
    }

    public void deleteEventById(Long id) {
        Event existingEvent = eventRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Event not found with this id"));

        eventRepository.delete(existingEvent);
    }

    public EventResponse updateEventById(Long id, UpdateEventRequest eventRequest) {
        Event existingEvent = eventRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Event not found with this id"));
        Club club = clubRepository.findById(eventRequest.getClubId()).orElseThrow(() ->
                new ResourceNotFoundException("club not found with this id"));
        if (eventRequest.getEventImage() != null && !eventRequest.getEventImage().isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(eventRequest.getEventImage());
            existingEvent.setImage(imageUrl);
        }

        existingEvent.setEventName(eventRequest.getEventName());
        existingEvent.setClub(club);
        existingEvent.setDetails(eventRequest.getDetails());
        existingEvent.setLocation(eventRequest.getLocation());
        existingEvent.setGuest(eventRequest.getGuest());
        existingEvent.setMainTheme(eventRequest.getMainTheme());
        existingEvent.setEventDateTime(eventRequest.getEventDateTime());
        Event updatedEvent = eventRepository.save(existingEvent);

        return EventResponse.toDto(updatedEvent);
    }
}
