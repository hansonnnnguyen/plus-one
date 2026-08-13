package com.hanson.plusone.exception;

import java.util.Map;

public record ApiError(
    String message,
    Map<String, String> errors
) {}