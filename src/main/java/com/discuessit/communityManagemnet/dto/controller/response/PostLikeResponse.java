package com.discuessit.communityManagemnet.dto.controller.response;

public record PostLikeResponse(
        Long likeId,
        Long userId,
        Long postId
) {
}
