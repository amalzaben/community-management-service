package com.discuessit.communityManagemnet.dto.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record CommunityResponse(
        Long communityId,
        String name,
        String description,
        @JsonProperty("public_community")
        Boolean publicCommunity ,
        Long membersCount,
        LocalDateTime createdAt
) {
}
