package com.discuessit.communityManagemnet.dto.controller.request;

public record UnarchivePostRequest(
        Long postId,
        Long memberId
) {
}
