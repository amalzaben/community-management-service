package com.discuessit.communityManagemnet.dto.service;

public record PostLikeDto(
        Long likeId,
        Long userId,
        Long postId
) {
}
