package com.discuessit.communityManagemnet.dto.service;

import com.discuessit.communityManagemnet.model.MembershipStatus;
import jakarta.validation.constraints.NotNull;

public record HandleJoinRequestCommand(
        @NotNull(message = "Member ID must not be null")
        Long memberId,

        @NotNull(message = "Status must not be null")
        MembershipStatus status
) {
}
