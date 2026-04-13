package com.discuessit.communityManagemnet.repository;

import com.discuessit.communityManagemnet.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
