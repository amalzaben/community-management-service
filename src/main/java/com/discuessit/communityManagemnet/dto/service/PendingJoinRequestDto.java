package com.discuessit.communityManagemnet.dto.service;


import com.discuessit.communityManagemnet.dto.controller.response.UserResponse;
import jakarta.validation.constraints.NotNull;

public record PendingJoinRequestDto(

        @NotNull(message = "Member ID must not be null")
        Long memberId,

        @NotNull(message = "User must not be null")
        UserResponse userResponse
) {
}
