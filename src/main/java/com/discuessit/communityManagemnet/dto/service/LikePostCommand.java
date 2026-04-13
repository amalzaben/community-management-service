package com.discuessit.communityManagemnet.dto.service;

import jakarta.validation.constraints.NotNull;

public record LikePostCommand(
        @NotNull(message = "User ID must not be null")
         Long userId,

        @NotNull(message = "Post ID must not be null")
         Long postId
) {

}
