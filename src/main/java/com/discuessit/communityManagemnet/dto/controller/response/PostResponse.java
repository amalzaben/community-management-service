package com.discuessit.communityManagemnet.dto.controller.response;

public record PostResponse(
        Long postId,
        Long memberId,
        String title,
        String content,
        long likesCount,
        long commentsCount
) {
}
