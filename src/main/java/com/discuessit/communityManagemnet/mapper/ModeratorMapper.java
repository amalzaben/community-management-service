package com.discuessit.communityManagemnet.mapper;

import com.discuessit.communityManagemnet.dto.controller.request.AssignNewModeratorRequest;
import com.discuessit.communityManagemnet.dto.controller.request.HandleJoinRequestRequest;
import com.discuessit.communityManagemnet.dto.controller.response.ModeratorResponse;
import com.discuessit.communityManagemnet.dto.service.AssignNewModeratorCommand;
import com.discuessit.communityManagemnet.dto.service.HandleJoinRequestCommand;
import com.discuessit.communityManagemnet.dto.service.ModeratorDto;
import com.discuessit.communityManagemnet.model.Moderator;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModeratorMapper {

    ModeratorDto toDto(Moderator moderator);
    Moderator toEntity(ModeratorDto moderatorDto);
    AssignNewModeratorCommand toCommand(AssignNewModeratorRequest assignNewModeratorRequest);
    ModeratorResponse toResponse(Moderator moderator);
    public HandleJoinRequestCommand toHandleJoinRequestCommand(HandleJoinRequestRequest request);
}
