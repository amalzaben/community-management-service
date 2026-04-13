package com.discuessit.communityManagemnet.repository;

import com.discuessit.communityManagemnet.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    @Query("SELECT p FROM Post p WHERE p.id = :postId")
    Optional<Post> findById(@Param("postId") Long postId);

//    @Query("""
//    SELECT p FROM Post p
//    WHERE p.community.id = :communityId
//      AND p.deleted = false
//    """)
//    Page<Post> findByCommunityIdAndDeletedFalse(@Param("communityId") Long communityId,
//                                                Pageable pageable
//    );

    @Query("SELECT p FROM Post p WHERE p.member.community.id = :communityId AND p.deleted = false")
    Page<Post> findByCommunityIdAndDeletedFalse(@Param("communityId") Long communityId, Pageable pageable);


}
