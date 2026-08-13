package com.hanson.plusone.dto;

public record LoginResponse(
    Long id,
    String email,
    String message
) {}