package com.discuessit.communityManagemnet.service;

import com.discuessit.communityManagemnet.dto.service.*;
import com.discuessit.communityManagemnet.mapper.CommunityMapper;
import com.discuessit.communityManagemnet.dto.PaginatedResponse;
import com.discuessit.communityManagemnet.dto.controller.response.JoinCommunityResponse;
import com.discuessit.communityManagemnet.model.*;
import com.discuessit.communityManagemnet.repository.CommunityMemberRepository;
import com.discuessit.communityManagemnet.repository.CommunityRepository;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.discuessit.communityManagemnet.exception.ConflictException;
import com.discuessit.communityManagemnet.exception.ForbiddenException;
import com.discuessit.communityManagemnet.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class CommunityService {

    @Autowired
    private  CommunityRepository communityRepository;
    @Autowired
    private  CommunityMapper communityMapper;
    @Autowired
    @Lazy
    private ModerationService moderationService;
    @Autowired
    private CommunityMemberRepository communityMemberRepository;
    @Autowired
    @Lazy
    private PostService postService;
    @Autowired
    UserClient userClient;

    @Transactional
    public Community createCommunity(@Valid CreateCommunityCommand createCommunityCommand) {

        Community community = communityMapper.toCommunityEntity(createCommunityCommand);
        community = communityRepository.save(community);

        CommunityMember communityMember = CommunityMember.builder()
                .community(community)
                .membershipStatus(MembershipStatus.APPROVED)
                .userId(createCommunityCommand.userId())
                .build();
        communityMember=communityMemberRepository.save(communityMember);
        community.setMembersCount(community.getMembersCount()+1);
        moderationService.assignCommunityMemberAsModeratorWithAllPrivileges(communityMember);

        // Action done in third party /

        return community;
    }

    public JoinCommunityResponse joinCommunity(@Valid JoinCommunityCommand joinCommunityCommand) {

        Community community = verifyCommunityExists(joinCommunityCommand.communityId());

        ensureUserNotAlreadyMember(joinCommunityCommand.userId(), joinCommunityCommand.communityId());

        MembershipStatus status = community.isPublicCommunity()
                ? MembershipStatus.APPROVED
                : MembershipStatus.PENDING;

        CommunityMember member = CommunityMember.builder()
                .userId(joinCommunityCommand.userId())
                .community(community)
                .membershipStatus(status)
                .deleted(false)
                .build();

        communityMemberRepository.save(member);
        community.setMembersCount(community.getMembersCount()+1);
        return new JoinCommunityResponse(
                community.getId(),
                member.getId(),
                joinCommunityCommand.userId(),
                status
        );
    }

    public PaginatedResponse<PendingJoinRequestDto> getPendingJoinRequests(Long communityId, Pageable pageable) {

        Community community = verifyCommunityExists(communityId);

        Page<CommunityMember> pendingMembers =
                communityMemberRepository.findByCommunityIdAndMembershipStatus(
                        communityId, MembershipStatus.PENDING, pageable
                );

        List<PendingJoinRequestDto> dtoList = pendingMembers.stream()
                .map(member -> new PendingJoinRequestDto(
                        member.getId(),
                        userClient.getUserById(member.getUserId())
                ))
                .toList();

        return new PaginatedResponse<>(
                dtoList,
                pendingMembers.getNumber(),
                pendingMembers.getTotalPages(),
                pendingMembers.getTotalElements()
        );
    }

    public void handleJoinRequest(Long userId, @Valid HandleJoinRequestCommand command) {
        CommunityMember member = verifyCommunityMemberExists(command.memberId());

        Long moderatorId =moderationService.validateModeratorBelongsToCommunity(
               userId, member.getCommunity().getId()
        );

        moderationService.assertModeratorHasPrivilege(
                moderatorId, ModeratorPrivilegeType.MANAGE_MEMBERS
        );

        member.setMembershipStatus(command.status());
        communityMemberRepository.save(member);
    }

    public PaginatedResponse<PostDto> getPostsByCommunity(Long communityId, Pageable pageable) {
        verifyCommunityExists(communityId);
        return postService.getPostsByCommunity(communityId, pageable);
    }

    public CommunityMember verifyCommunityMemberExists(Long memberId) {
        return communityMemberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + memberId));
    }

    public Community verifyCommunityExists(Long communityId) {
        return communityRepository.findById(communityId)
                .orElseThrow(() -> new ResourceNotFoundException("Community with ID " + communityId + " not found"));
    }

    public void ensureUserNotAlreadyMember(Long userId, Long communityId) {
        boolean exists = communityMemberRepository.existsByUserIdAndCommunityIdAndDeletedFalse(userId, communityId);
        if (exists) {
            throw new ConflictException("User is already a member of this community");
        }
    }
    public void assertApprovedMemberOfCommunity(Long memberId, Long communityId) {
        CommunityMember member = verifyCommunityMemberExists(memberId);

        if (!member.getCommunity().getId().equals(communityId)) {
            throw new ForbiddenException("Member does not belong to the specified community");
        }

        if (!MembershipStatus.APPROVED.toString().equals(member.getMembershipStatus())) {
            throw new ForbiddenException("Member is not approved to interact in this community");
        }
    }


}
