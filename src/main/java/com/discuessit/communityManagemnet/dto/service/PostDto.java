package com.discuessit.communityManagemnet.dto.service;

public record PostDto(
        Long postId,
        String title,
        String content,
        Long memberId,
        Long communityId,
        long likesCount,
        long commentsCount
) {
}
