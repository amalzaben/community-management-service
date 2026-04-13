package com.discuessit.communityManagemnet.dto.controller.response;

import com.discuessit.communityManagemnet.model.MembershipStatus;

public record JoinCommunityResponse(
        Long communityId,
        Long memberId,
        Long userId,
        MembershipStatus membershipStatus
) {
}
