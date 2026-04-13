package com.discuessit.communityManagemnet.dto.service;

import java.time.LocalDateTime;

public record CommunityDto(
        Long communityId,
        String name,
        String description,
        Boolean publicCommunity ,
        Long membersCount,
        LocalDateTime createdAt
) {
}
