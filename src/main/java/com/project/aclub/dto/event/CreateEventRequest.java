package com.project.aclub.dto.event;

import com.project.aclub.entity.Event;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class CreateEventRequest {
    @NotNull(message = "event name must not be null")
    String eventName;

    LocalDateTime eventDateTime;

    @NotBlank(message = "location must not be empty")
    String location;

    @NotNull(message = "mainThem must not be empty")
    String mainTheme;

    @NotNull(message = "details must not be empty")
    String Details;

    @NotNull(message = "event image must not be empty")
    MultipartFile eventImage;

    String guest;

    @NotNull(message = "clubId must not be empty")
    Long clubId;

    public Event toEntity() {
        Event event = new Event();
        event.setEventName(this.eventName);
        event.setEventDateTime(this.eventDateTime);
        event.setDetails(this.Details);
        event.setMainTheme(this.mainTheme);
        event.setGuest(this.guest);
        event.setLocation(this.location);
        return event;
    }
}
