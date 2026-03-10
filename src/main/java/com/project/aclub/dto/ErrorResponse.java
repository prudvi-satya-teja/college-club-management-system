package com.project.aclub.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    String message;
    int status;
    String error;
    String path;
    LocalDateTime timestamp;

}
