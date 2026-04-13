package com.discuessit.communityManagemnet.dto.controller.response;

import com.discuessit.communityManagemnet.model.ModeratorPrivilegeType;

import java.util.List;

public record ModeratorPrivilegesResponse(
        Long moderatorId,
        List<ModeratorPrivilegeType>privileges
) {
}
