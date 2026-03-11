package com.project.aclub.controller;

import com.project.aclub.dto.PageResponse;
import com.project.aclub.dto.event.CreateEventRequest;
import com.project.aclub.dto.event.EventResponse;
import com.project.aclub.dto.event.UpdateEventRequest;
import com.project.aclub.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @PreAuthorize("@clubAuthService.isClubAdmin(authentication, #eventRequest.clubId)")
    public ResponseEntity<EventResponse> registerEvent(@Valid @ModelAttribute CreateEventRequest eventRequest) {
        EventResponse eventResponse = eventService.registerEvent(eventRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        EventResponse eventResponse = eventService.getEventById(id);
        return ResponseEntity.ok(eventResponse);
    }

    @GetMapping
    public ResponseEntity<PageResponse<EventResponse>> getAllEvents(
            @RequestParam(required = false) Long clubId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventDateTime") String sortBy,
            @RequestParam(defaultValue = "des") String sortDir) {
        System.out.println(clubId + " CLub id ");
        PageResponse<EventResponse> events = eventService.getAllEvents(clubId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(events);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@clubAuthService.isClubAdminByEventId(authentication, #eventRequest.clubId)")
    public ResponseEntity<Void> deleteEventById(@PathVariable Long id) {
        eventService.deleteEventById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("@clubAuthService.isClubAdmin(authentication, #eventRequest.clubId)")
    public ResponseEntity<EventResponse> updateEventById(@PathVariable Long id,
                                                         @Valid @ModelAttribute UpdateEventRequest eventRequest) {
        EventResponse eventResponse = eventService.updateEventById(id, eventRequest);
        return ResponseEntity.ok(eventResponse);
    }
}
