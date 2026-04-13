package com.discuessit.communityManagemnet.dto.controller.request;

public record CreatePostRequest(
        Long memberId,
        Long communityId,
        String title,
        String content
) {
}
