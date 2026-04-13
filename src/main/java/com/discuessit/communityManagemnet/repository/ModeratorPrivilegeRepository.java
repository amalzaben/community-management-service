package com.discuessit.communityManagemnet.repository;


import com.discuessit.communityManagemnet.model.Moderator;
import com.discuessit.communityManagemnet.model.ModeratorPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeratorPrivilegeRepository extends JpaRepository<ModeratorPrivilege,Long> {

    @Query("SELECT mp FROM ModeratorPrivilege mp WHERE mp.moderator.id = :moderatorId")
    List<ModeratorPrivilege> findByModeratorId(@Param("moderatorId") Long moderatorId);

}
