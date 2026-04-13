package com.discuessit.communityManagemnet.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        int status,
        String message,
        ApiErrorResponseCode apiExceptionResponseCode,
        String description,
        LocalDateTime timestamp
) {
}
