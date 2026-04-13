package com.discuessit.communityManagemnet.mapper;

import com.discuessit.communityManagemnet.dto.controller.response.CommunityResponse;
import com.discuessit.communityManagemnet.dto.controller.request.CreateCommunityRequest;
import com.discuessit.communityManagemnet.dto.service.CommunityDto;
import com.discuessit.communityManagemnet.dto.service.CreateCommunityCommand;
import com.discuessit.communityManagemnet.model.Community;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommunityMapper {

    // Controller request → service DTO
    CommunityDto toCommunityDto(CreateCommunityRequest request);

    // Controller request → command
    CreateCommunityCommand toCommunityCommand(CreateCommunityRequest createCommunityRequest, Long userId);

    // Service DTO → controller response
    CommunityResponse toCommunityResponse(CommunityDto dto);

    // Entity → controller response
    @Mapping(source = "id", target = "communityId")
    CommunityResponse toCommunityResponse(Community community);

    // Entity → service DTO
    CommunityDto toCommunityDto(Community community);

    // Command → entity
    Community toCommunityEntity(CreateCommunityCommand createCommunityCommand);
}

