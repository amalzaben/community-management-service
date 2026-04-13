package com.discuessit.communityManagemnet.dto.controller.request;

import com.discuessit.communityManagemnet.model.MembershipStatus;

public record HandleJoinRequestRequest(
        Long memberId,
        MembershipStatus status
) {
}
