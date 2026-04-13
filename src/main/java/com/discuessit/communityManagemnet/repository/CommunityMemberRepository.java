package com.discuessit.communityManagemnet.repository;

import com.discuessit.communityManagemnet.model.CommunityMember;
import com.discuessit.communityManagemnet.model.MembershipStatus;
import com.discuessit.communityManagemnet.model.Moderator;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CommunityMemberRepository extends JpaRepository<CommunityMember,Long> {

    @Query("SELECT cm FROM CommunityMember cm WHERE cm.id = :id")
    Optional<CommunityMember> findById(@Param("id") Long id);

    @Query("""
    SELECT COUNT(cm) > 0 
    FROM CommunityMember cm 
    WHERE cm.userId = :userId 
      AND cm.community.id = :communityId 
      AND cm.deleted = false
    """)
    boolean existsByUserIdAndCommunityIdAndDeletedFalse(@Param("userId") Long userId,
                                                        @Param("communityId") Long communityId);

    @Query("""
    SELECT cm 
    FROM CommunityMember cm 
    WHERE cm.community.id = :communityId 
      AND cm.membershipStatus = :status
    """)
    Page<CommunityMember> findByCommunityIdAndMembershipStatus(  @Param("communityId") Long communityId,
                                                                 @Param("status") MembershipStatus status,
                                                                 Pageable pageable
    );

    @Query("""
    SELECT COUNT(cm) > 0 
    FROM CommunityMember cm 
    WHERE cm.id = :memberId 
      AND cm.community.id = :communityId 
      AND cm.membershipStatus = :status 
      AND cm.deleted = false
    """)
    boolean existsApprovedMemberInCommunity(@Param("memberId") Long memberId,
                                            @Param("communityId") Long communityId,
                                            @Param("status") String status
    );


    @Query("SELECT m FROM CommunityMember m WHERE m.userId = :userId AND m.community.id = :communityId AND m.deleted = false")
    Optional<CommunityMember> findByUserIdAndCommunityIdAndDeletedFalse(@Param("userId") Long userId, @Param("communityId") Long communityId);


}
