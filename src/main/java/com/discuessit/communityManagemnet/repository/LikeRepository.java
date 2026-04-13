package com.discuessit.communityManagemnet.repository;

import com.discuessit.communityManagemnet.model.Like;
import com.discuessit.communityManagemnet.model.Post;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {

    @Query("""
    SELECT l FROM Like l 
    WHERE l.userId = :userId 
      AND l.post = :post 
      AND l.deleted = false
    """)
    Optional<Like> findByUserIdAndPostAndDeletedFalse(
            @Param("userId") Long userId,
            @Param("post") Post post
    );

    @Query("""
    SELECT l FROM Like l 
    WHERE l.post.id = :postId 
      AND l.deleted = false
    """)
    Page<Like> findByPostIdAndDeletedFalse(
            @Param("postId") Long postId,
            Pageable pageable
    );

}
