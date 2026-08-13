package com.hanson.plusone.dto;

import java.time.LocalDateTime;

public record ProfileResponse(
    Long id,
    Long userId,
    String displayName,
    String bio,
    String city,
    String connectionGoal,
    LocalDateTime createdAt
) {}