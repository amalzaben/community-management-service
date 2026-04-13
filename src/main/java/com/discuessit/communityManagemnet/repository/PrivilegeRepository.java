package com.discuessit.communityManagemnet.repository;

import com.discuessit.communityManagemnet.model.ModeratorPrivilegeType;
import com.discuessit.communityManagemnet.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface PrivilegeRepository  extends JpaRepository<Privilege, Long> {

    @Query("SELECT p FROM Privilege p WHERE p.type = :type")
    Optional<Privilege> findByType(@Param("type") ModeratorPrivilegeType type);

    @Query("SELECT p FROM Privilege p")
    List<Privilege> findAll();
}
