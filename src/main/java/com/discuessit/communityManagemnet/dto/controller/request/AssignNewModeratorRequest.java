package com.discuessit.communityManagemnet.dto.controller.request;

import com.discuessit.communityManagemnet.model.ModeratorPrivilegeType;


import java.util.List;

public record AssignNewModeratorRequest(
        Long memberToBeModeratorId,
        Long assignedByModeratorId,
        List<ModeratorPrivilegeType> privilegesList
) {
}
