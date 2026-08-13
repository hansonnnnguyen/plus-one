package com.hanson.plusone.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateEventRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title cannot exceed 100 characters")
        String title,

        @NotBlank(message = "Description is required")
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,

        @NotBlank(message = "Location is required")
        @Size(max = 150, message = "Location cannot exceed 150 characters")
        String location,

        @NotBlank(message = "Category is required")
        @Size(max = 50, message = "Category cannot exceed 50 characters")
        String category,

        @NotNull(message = "Start date and time are required")
        @Future(message = "Event must be scheduled in the future")
        LocalDateTime startsAt,

        @NotNull(message = "Maximum attendees is required")
        @Min(value = 2, message = "An event must allow at least 2 attendees")
        @Max(value = 1000, message = "Maximum attendees cannot exceed 1000")
        Integer maxAttendees

) {
}