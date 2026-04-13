package com.discuessit.communityManagemnet.dto.service;

import jakarta.validation.constraints.NotNull;

public record JoinCommunityCommand(
        @NotNull(message = "User ID must not be null")
        Long userId,

        @NotNull(message = "Community ID must not be null")
        Long communityId
) {
}
