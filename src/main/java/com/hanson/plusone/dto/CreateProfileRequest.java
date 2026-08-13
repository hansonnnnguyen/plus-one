package com.hanson.plusone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProfileRequest(

    @NotBlank(message = "Display name is required")
    @Size(min = 2, max = 60, message = "Display name must be 2 to 60 characters")
    String displayName,

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    String bio,

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    String city,

    @NotBlank(message = "Connection goal is required")
    @Size(max = 200, message = "Connection goal cannot exceed 200 characters")
    String connectionGoal
) {}