package com.discuessit.communityManagemnet.service;

import com.discuessit.communityManagemnet.mapper.CommunityMapper;
import com.discuessit.communityManagemnet.mapper.ModeratorMapper;
import com.discuessit.communityManagemnet.dto.controller.response.ModeratorPrivilegesResponse;
import com.discuessit.communityManagemnet.dto.service.AssignNewModeratorCommand;
import com.discuessit.communityManagemnet.model.*;
import com.discuessit.communityManagemnet.repository.*;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.discuessit.communityManagemnet.exception.ForbiddenException;
import com.discuessit.communityManagemnet.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class ModerationService {

    @Autowired
    private ModeratorRepository moderatorRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private ModeratorPrivilegeRepository moderatorPrivilegeRepository;
    @Autowired
    private ModeratorMapper moderatorMapper;
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    @Lazy
    private CommunityService communityService;
    @Autowired
    private CommunityMemberRepository communityMemberRepository;


    @Transactional
    public Moderator assignCommunityMemberAsModeratorWithAllPrivileges(CommunityMember communityMember){

        Moderator moderator = Moderator.builder()
                .member(communityMember)
                .assignedBy(null)
                .deleted(false)
                .build();

       moderator= moderatorRepository.save(moderator);
        assignAllPrivileges(moderator);
        return moderator;
    }

    private void assignAllPrivileges(Moderator moderator){

        List<Privilege> allPrivileges = privilegeRepository.findAll();

        List<ModeratorPrivilege> moderatorPrivileges = allPrivileges.stream()
                .map(privilege -> ModeratorPrivilege.builder()
                        .moderator(moderator)
                        .privilege(privilege)
                        .build()
                )
                .toList();

        moderatorPrivilegeRepository.saveAll(moderatorPrivileges);
    }

    @Transactional
    public Moderator assignMemberAsModeratorWithSpecificPrivileges(
          @Valid AssignNewModeratorCommand assignModeratorCommand
    ) {
        Moderator assigner=verifyModeratorExists(assignModeratorCommand.getAssignedByModeratorId());
        Community  community=communityService.verifyCommunityExists(assignModeratorCommand.getCommunityId()) ;
        CommunityMember  member=communityService.verifyCommunityMemberExists(assignModeratorCommand.getMemberToBeModeratorId());

            Moderator moderator = Moderator.builder()
                    .member(member)
                    .assignedBy(assigner)
                    .deleted(false)
                    .build();

            Moderator createdModerator= moderatorRepository.save(moderator);
            assignPrivileges(createdModerator, assignModeratorCommand.getPrivilegesList());

            return createdModerator;
    }

    private void assignPrivileges(Moderator moderator, List<ModeratorPrivilegeType> privilegesList) {
        List<Privilege> privileges = getPrivilegesFromEnum(privilegesList);

        List<ModeratorPrivilege> moderatorPrivileges = privileges.stream()
                .map(privilege -> ModeratorPrivilege.builder()
                        .moderator(moderator)
                        .privilege(privilege)
                        .build())
                .toList();

        moderatorPrivilegeRepository.saveAll(moderatorPrivileges);
    }

    public List<Privilege> getPrivilegesFromEnum(List<ModeratorPrivilegeType> types) {
        return types.stream()
                .map(type -> privilegeRepository.findByType(type)
                        .orElseThrow(() -> new ResourceNotFoundException("Privilege not found: " + type.name())))
                .toList();
    }

    public List<ModeratorPrivilegeType> getPrivilegesByModeratorId(Long moderatorId) {
        List<ModeratorPrivilege> moderatorPrivileges = moderatorPrivilegeRepository.findByModeratorId(moderatorId);
        if(moderatorPrivileges.isEmpty()){
            throw new ResourceNotFoundException("moderator either not found or has no privileges !!");
        }else{
            List<ModeratorPrivilegeType> privilegeTypes = moderatorPrivileges.stream()
                    .map(mp -> ModeratorPrivilegeType.valueOf(mp.getPrivilege().getType().toString()))
                    .toList();

            return privilegeTypes;
        }
    }

//    public void validateModeratorBelongsToCommunity(Long moderatorId, Long communityId) {
//        Moderator moderator = moderatorRepository.findById(moderatorId)
//                .orElseThrow(() -> new ResourceNotFoundException("Moderator not found with ID: " + moderatorId));
//
//        if (!moderator.getMember().getCommunity().getId().equals(communityId)) {
//            throw new ForbiddenException("Moderator does not belong to the specified community");
//        }
//    }
public Long validateModeratorBelongsToCommunity(Long userId, Long communityId) {
    // Find the CommunityMember using userId and communityId
    CommunityMember member = communityMemberRepository.findByUserIdAndCommunityIdAndDeletedFalse(userId, communityId)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found in community"));

    //Find the Moderator using the member
    Moderator moderator = moderatorRepository.findByMemberAndDeletedFalse(member)
            .orElseThrow(() -> new ResourceNotFoundException("Moderator not found for this member"));

    //Check community consistency (defensive check)
    if (!moderator.getMember().getCommunity().getId().equals(communityId)) {
        throw new ForbiddenException("Moderator does not belong to the specified community");
    }

    return moderator.getId();
}

    public void assertModeratorHasPrivilege(Long moderatorId, ModeratorPrivilegeType requiredPrivilege) {
        List<ModeratorPrivilegeType> response = getPrivilegesByModeratorId(moderatorId);

        if (!response.contains(requiredPrivilege)) {
            throw new ForbiddenException("Moderator lacks required privilege: " + requiredPrivilege.name());
        }
    }

    public Moderator verifyModeratorExists(Long moderatorId) {
        return moderatorRepository.findById(moderatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Moderator not found with ID: " + moderatorId));
    }



}
