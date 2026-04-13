package com.discuessit.communityManagemnet.repository;

import com.discuessit.communityManagemnet.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Query("SELECT c FROM Community c WHERE c.id = :id")
    Optional<Community> findById(@Param("id") Long id);
}
