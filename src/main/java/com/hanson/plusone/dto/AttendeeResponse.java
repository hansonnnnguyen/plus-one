package com.hanson.plusone.dto;

import java.time.LocalDateTime;

public record AttendeeResponse(
        Long userId,
        String displayName,
        LocalDateTime joinedAt
) {
}