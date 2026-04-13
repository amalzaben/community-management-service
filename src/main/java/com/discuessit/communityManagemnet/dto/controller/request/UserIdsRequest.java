package com.discuessit.communityManagemnet.dto.controller.request;

import java.util.List;

public record UserIdsRequest(
        List<Long> userIds
) {
}
