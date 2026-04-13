package com.discuessit.communityManagemnet.dto.service;

import jakarta.validation.constraints.NotNull;

public record ArchivePostCommand(
        @NotNull(message = "Post ID must not be null")
        Long postId,

        @NotNull(message = "Member ID must not be null")
        Long memberId
) {
}
