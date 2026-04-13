package com.discuessit.communityManagemnet.mapper;

import com.discuessit.communityManagemnet.dto.controller.request.*;
import com.discuessit.communityManagemnet.dto.controller.response.PostResponse;
import com.discuessit.communityManagemnet.dto.service.*;
import com.discuessit.communityManagemnet.model.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    CreatePostCommand toCommand(CreatePostRequest request);
    PostDto toDto(Post post);
    PostResponse toResponse(PostDto dto);
    ArchivePostCommand toCommand(ArchivePostRequest request);
    UnarchivePostCommand toCommand(UnarchivePostRequest request);
    LikePostCommand toCommand(Long postId, LikePostRequest request);
    RemoveLikeCommand toCommand(RemoveLikeRequest request, Long postId);
    CommentCommand toCommand(CommentRequest request);
}
