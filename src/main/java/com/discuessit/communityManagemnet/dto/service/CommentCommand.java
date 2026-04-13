package com.discuessit.communityManagemnet.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCommand(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Post ID is required")
        Long postId,

        @NotBlank(message = "Content must not be blank")
        String content,

        Long parentCommentId
) {
}
