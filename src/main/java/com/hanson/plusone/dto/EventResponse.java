package com.hanson.plusone.dto;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        Long hostUserId,
        String hostDisplayName,
        String title,
        String description,
        String location,
        String category,
        LocalDateTime startsAt,
        Integer maxAttendees,
        LocalDateTime createdAt,
        long attendeeCount,
        long spotsRemaining
) {
}