package com.project.aclub.controller;

import com.project.aclub.entity.Event;
import com.project.aclub.service.EventService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService)  {
        this.eventService = eventService;
    }

    @PostMapping("/event")
    public ResponseEntity<Void> createClub(@RequestBody Event event) {
        eventService.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> fetchAllClubs() {
        List<Event> events = eventService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<Event> findClubById(@PathVariable Long id) {
        Event event = eventService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(event);
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity<Void> deleteClubById(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/event/{id}")
    public ResponseEntity<Void> updateClubById(@PathVariable Long id, @RequestBody Event event) {
        eventService.updateEventById(id, event);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
