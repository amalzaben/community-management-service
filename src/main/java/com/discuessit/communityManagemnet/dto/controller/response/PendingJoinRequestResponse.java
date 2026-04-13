package com.discuessit.communityManagemnet.dto.controller.response;

public record PendingJoinRequestResponse(
        Long memberId,
        UserResponse user
) {
}
