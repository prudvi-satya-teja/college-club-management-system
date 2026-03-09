package com.project.aclub.service;

import com.project.aclub.entity.Event;
import com.project.aclub.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void save(Event event) {
        eventRepository.save(event);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(()->
                new RuntimeException("Event not found"));
    }

    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    public void updateEventById(Long id, Event event) {
        Event existingEvent = eventRepository.findById(id).orElseThrow(()->
                new RuntimeException("Event not found"));
        existingEvent.setEventName(event.getEventName());
        existingEvent.setClub(event.getClub());
        existingEvent.setDetails(event.getDetails());
        existingEvent.setLocation(event.getLocation());
        existingEvent.setGuest(event.getGuest());
        existingEvent.setMainTheme(event.getMainTheme());
        existingEvent.setEventDateTime(event.getEventDateTime());
        existingEvent.setImage(event.getImage());
        eventRepository.save(existingEvent);
    }
}
