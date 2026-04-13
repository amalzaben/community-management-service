package com.discuessit.communityManagemnet.repository;

import com.discuessit.communityManagemnet.model.CommunityMember;
import com.discuessit.communityManagemnet.model.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator,Long> {

    @Query("SELECT m FROM Moderator m WHERE m.id = :moderatorId")
    Optional<Moderator> findById(@Param("moderatorId") Long moderatorId);

    @Query("SELECT mod FROM Moderator mod WHERE mod.member = :member AND mod.deleted = false")
    Optional<Moderator> findByMemberAndDeletedFalse(@Param("member") CommunityMember member);



}
