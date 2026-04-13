package com.discuessit.communityManagemnet.controller;

import com.discuessit.communityManagemnet.model.ModeratorPrivilegeType;
import com.discuessit.communityManagemnet.security.JwtAuthentication;
import com.discuessit.communityManagemnet.dto.controller.request.*;
import com.discuessit.communityManagemnet.dto.controller.response.*;
import com.discuessit.communityManagemnet.dto.service.*;
import com.discuessit.communityManagemnet.mapper.CommunityMapper;
import com.discuessit.communityManagemnet.mapper.ModeratorMapper;
import com.discuessit.communityManagemnet.mapper.PostMapper;
import com.discuessit.communityManagemnet.dto.PaginatedResponse;
import com.discuessit.communityManagemnet.model.Community;
import com.discuessit.communityManagemnet.model.Moderator;
import com.discuessit.communityManagemnet.repository.CommunityRepository;
import com.discuessit.communityManagemnet.service.CommunityService;
import com.discuessit.communityManagemnet.service.ModerationService;
import com.discuessit.communityManagemnet.service.PostService;
import com.discuessit.communityManagemnet.service.UserClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/communities")
public class CommunityController {

    @Autowired
    private CommunityService communityService;
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private ModerationService moderationService;
    @Autowired
    private ModeratorMapper moderatorMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private PostService postService;
    @Autowired
    private  UserClient userClient;


    @PostMapping
    public ResponseEntity<CommunityResponse> createCommunity(
            @RequestBody CreateCommunityRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication) {

        CreateCommunityCommand createCommunityCommand = communityMapper
                .toCommunityCommand(request,authentication.getUserId());

        Community createdCommunity = communityService.createCommunity(createCommunityCommand);

        CommunityResponse communityResponse = communityMapper.toCommunityResponse(createdCommunity);

        URI location = URI.create("/communities/" + createdCommunity.getId());
        return ResponseEntity.created(location).body(communityResponse);
    }

    @PostMapping("/{communityId}/assign-new-moderator")
    public ResponseEntity<ModeratorResponse> assignModerator(
            @PathVariable Long communityId,
            @RequestBody AssignNewModeratorRequest assignModeratorRequest ){

        AssignNewModeratorCommand assignNewModeratorCommand=moderatorMapper.toCommand(assignModeratorRequest);
        assignNewModeratorCommand.setCommunityId(communityId);

        Moderator moderator=moderationService.assignMemberAsModeratorWithSpecificPrivileges(assignNewModeratorCommand);
        ModeratorResponse moderatorResponse=moderatorMapper.toResponse(moderator);

        return ResponseEntity.accepted().body(moderatorResponse);//202
    }

    @GetMapping("/moderators/{moderatorId}/privileges")
    public ResponseEntity<ModeratorPrivilegesResponse> getModeratorPrivileges(
            @PathVariable Long moderatorId) {

        List<ModeratorPrivilegeType> privileges = moderationService.getPrivilegesByModeratorId(moderatorId);
        return ResponseEntity.ok(new ModeratorPrivilegesResponse(moderatorId,privileges));
    }

    @PostMapping("/{communityId}/join")
    public ResponseEntity<JoinCommunityResponse> joinCommunity(
            @PathVariable Long communityId,
            @AuthenticationPrincipal JwtAuthentication authentication) {

        JoinCommunityCommand joinCommunityCommand= new JoinCommunityCommand
                (authentication.getUserId(), communityId);

        JoinCommunityResponse response = communityService.joinCommunity(joinCommunityCommand);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{communityId}/pending-join-requests")
    public ResponseEntity<PaginatedResponse<PendingJoinRequestResponse>> getPendingJoinRequests(
            @PathVariable Long communityId,
            @PageableDefault(size = 10) Pageable pageable) {

        PaginatedResponse<PendingJoinRequestDto> serviceResponse =
                communityService.getPendingJoinRequests(communityId, pageable);

        List<PendingJoinRequestResponse> controllerResponse = serviceResponse.getContent().stream()
                .map(dto -> new PendingJoinRequestResponse(dto.memberId(), dto.userResponse()))
                .toList();

        PaginatedResponse<PendingJoinRequestResponse> response = new PaginatedResponse<>(
                controllerResponse,
                serviceResponse.getPageNumber(),
                serviceResponse.getTotalPages(),
                serviceResponse.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("{communityId}/handle-join-request")
    public ResponseEntity<String> handleJoinRequest(
            @RequestBody HandleJoinRequestRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {

        HandleJoinRequestCommand command = moderatorMapper.toHandleJoinRequestCommand(request);
        communityService.handleJoinRequest(authentication.getUserId(),command);

        return ResponseEntity.ok("Join request status updated successfully");
    }

    @PostMapping("/{communityId}/posts")
    public ResponseEntity<PostResponse> createPost(
            @PathVariable Long communityId,
            @RequestBody CreatePostRequest createPostRequest) {

        CreatePostCommand command = postMapper.toCommand(createPostRequest);
        command.setCommunityId(communityId);

        PostDto postDto = postService.createPost(command);
        PostResponse response = postMapper.toResponse(postDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.postId())
                .toUri();
        return ResponseEntity.created(location).body(response);

    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PostMapping("/posts/archive")
    public ResponseEntity<Void> archivePost(@RequestBody ArchivePostRequest request) {
        ArchivePostCommand command = postMapper.toCommand(request);
        postService.archivePost(command);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PostMapping("/posts/unarchive")
    public ResponseEntity<Void> unarchivePost(@RequestBody UnarchivePostRequest request) {
        UnarchivePostCommand command = postMapper.toCommand(request);
        postService.unarchivePost(command);
        return ResponseEntity.ok().build(); // 200 OK
    }

    @GetMapping("/{communityId}/posts")
    public ResponseEntity<PaginatedResponse<PostResponse>> getPostsByCommunity(
            @PathVariable Long communityId,
            @PageableDefault(size = 10) Pageable pageable) {

        PaginatedResponse<PostDto> serviceResponse = communityService.getPostsByCommunity(communityId, pageable);

        List<PostResponse> responseList = serviceResponse.getContent().stream()
                .map(postDto -> new PostResponse(
                        postDto.postId(),
                        postDto.memberId(),
                        postDto.title(),
                        postDto.content(),
                        postDto.likesCount(),
                        postDto.commentsCount()
                )).toList();

        return ResponseEntity.ok(
                new PaginatedResponse<>(
                        responseList,
                        serviceResponse.getPageNumber(),
                        serviceResponse.getTotalPages(),
                        serviceResponse.getTotalElements()
                )
        );
    }

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId,
                                         @RequestBody LikePostRequest request) {
        LikePostCommand command = postMapper.toCommand(postId, request);
        postService.likePost(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{postId}/unlike")
    public ResponseEntity<Void> removeLike(
            @PathVariable Long postId,
            @RequestBody @Valid RemoveLikeRequest request) {

        RemoveLikeCommand command = postMapper.toCommand(request, postId);
        postService.removeLike(command);

        return ResponseEntity.noContent().build(); // HTTP 204
    }

    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<PaginatedResponse<PostLikeResponse>> getPostLikes(
            @PathVariable Long postId,
            @PageableDefault(size = 10) Pageable pageable) {

        PaginatedResponse<PostLikeDto> serviceResponse = postService.getLikesByPost(postId, pageable);

        List<PostLikeResponse> responseList = serviceResponse.getContent().stream()
                .map(dto -> new PostLikeResponse(dto.likeId(), dto.userId(), dto.postId()))
                .toList();

        return ResponseEntity.ok(
                new PaginatedResponse<>(
                        responseList,
                        serviceResponse.getPageNumber(),
                        serviceResponse.getTotalPages(),
                        serviceResponse.getTotalElements()
                )
        );
    }

    @PostMapping("/comments")
    public ResponseEntity<Void> commentOnPost( @RequestBody CommentRequest request) {
        CommentCommand command = postMapper.toCommand(request);
        postService.createComment(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //just to test the webclient config is working properly
    @GetMapping("{userId}/users-info")
    public ResponseEntity<UserResponse> commentOnPost( @PathVariable Long userId) {
        return ResponseEntity.ok(userClient.getUserById(userId));
    }






}
