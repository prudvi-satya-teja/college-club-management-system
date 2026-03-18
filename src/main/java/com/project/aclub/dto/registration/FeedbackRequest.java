package com.project.aclub.dto.registration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {
    int rating;
    String message;
}
