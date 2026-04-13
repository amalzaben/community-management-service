package com.discuessit.communityManagemnet.dto.controller.request;

public record CommentRequest(
        long userId,
        long postId,
        String content,
        Long parentCommentId
) {
}
