package com.discuessit.communityManagemnet.dto.service;

import com.discuessit.communityManagemnet.model.ModeratorPrivilegeType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignNewModeratorCommand {
        @NotNull(message = "member to be promoted must not be null")
        Long memberToBeModeratorId;

        @NotNull(message = "Moderator ID must not be null")
        Long assignedByModeratorId;

        @NotNull(message = "Community ID must not be null")
        Long communityId;

        @NotEmpty(message = "Privilege list must not be empty")
        List<ModeratorPrivilegeType> privilegesList;
}

