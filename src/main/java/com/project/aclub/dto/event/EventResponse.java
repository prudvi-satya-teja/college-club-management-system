package com.project.aclub.dto.event;

import com.project.aclub.dto.club.ClubResponse;
import com.project.aclub.entity.Club;
import com.project.aclub.entity.Event;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class EventResponse {
    Long eventId;
    String eventName;
    LocalDateTime eventDateTime;
    String location;
    String mainTheme;
    String Details;
    String eventImage;
    String guest;
    ClubResponse club;

    public static EventResponse toDto(Event event) {
        EventResponse eventResponse = new EventResponse();
        eventResponse.setEventId(event.getEventId());
        eventResponse.setEventName(event.getEventName());
        eventResponse.setEventDateTime(event.getEventDateTime());
        eventResponse.setLocation(event.getLocation());
        eventResponse.setMainTheme(event.getMainTheme());
        eventResponse.setDetails(event.getDetails());
        eventResponse.setEventImage(event.getImage());
        eventResponse.setGuest(event.getGuest());
        eventResponse.setClub(ClubResponse.toDTO(event.getClub()));
        return eventResponse;
    }
}
