package com.discuessit.communityManagemnet.service;

import com.discuessit.communityManagemnet.dto.service.*;
import com.discuessit.communityManagemnet.exception.ConflictException;
import com.discuessit.communityManagemnet.exception.ResourceNotFoundException;
import com.discuessit.communityManagemnet.mapper.PostMapper;
import com.discuessit.communityManagemnet.dto.PaginatedResponse;
import com.discuessit.communityManagemnet.model.*;
import com.discuessit.communityManagemnet.repository.*;
import com.discuessit.communityManagemnet.exception.ForbiddenException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private CommunityMemberRepository communityMemberRepository;
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    @Lazy
    private CommunityService communityService;

    public PostDto createPost(CreatePostCommand command) {

        // Fetch the community member by ID
        CommunityMember member = communityService.verifyCommunityMemberExists(command.getMemberId());

        // Check if the member belongs to the community provided in the command
        communityService.assertApprovedMemberOfCommunity(member.getId(),command.getCommunityId());

        // Fetch the community
        Community community = communityService.verifyCommunityExists(command.getCommunityId());

        // Create the Post
        Post post = Post.builder()
                .title(command.getTitle())
                .content(command.getContent())
                .member(member)
                .likesCount(0)
                .commentsCount(0)
                .deleted(false)
                .archived(false)
                .build();

        //  Save and return DTO
        Post saved = postRepository.save(post);
        return postMapper.toDto(saved);
    }

    public void deletePost(Long postId) {

        Post post = verifyPostExists(postId);

        if (post.isDeleted()) {
            throw new ConflictException("Post is already deleted");
        }

        post.setDeleted(true);
        postRepository.save(post);
    }

    public void archivePost(@Valid ArchivePostCommand command) {
        Post post = verifyPostExists(command.postId());

        //check if the request is coming from the post owner
        if (!post.getMember().getId().equals(command.memberId())) {
            throw new IllegalStateException("Member not authorized to archive this post");
        }

        post.setArchived(true);
        postRepository.save(post);
    }

    public void unarchivePost(@Valid UnarchivePostCommand command) {

        Post post =verifyPostExists(command.postId());

        if (!post.getMember().getId().equals(command.memberId())) {
            throw new ForbiddenException("This member is not authorized to unarchive the post");
        }

        if (!post.isArchived()) {
            throw new ConflictException("Post is not archived");
        }

        post.setArchived(false);
        postRepository.save(post);
    }

    public PaginatedResponse<PostDto> getPostsByCommunity(Long communityId, Pageable pageable) {

        Community community = communityService.verifyCommunityExists(communityId);

        Page<Post> posts = postRepository.findByCommunityIdAndDeletedFalse(communityId, pageable);

        List<PostDto> dtoList = posts.stream()
                .map(postMapper::toDto)
                .toList();

        return new PaginatedResponse<>(
                dtoList,
                posts.getNumber(),
                posts.getTotalPages(),
                posts.getTotalElements()
        );
    }

    @Transactional
    public void likePost(@Valid LikePostCommand command) {
        Post post = verifyPostExists(command.postId());

        Optional<Like> existing = likeRepository.findByUserIdAndPostAndDeletedFalse(command.userId(), post);
        if (existing.isPresent()) {
            throw new ConflictException("User has already liked this post.");
        }

        Like like = new Like();
        like.setUserId(command.userId());
        like.setPost(post);
        likeRepository.save(like);

        post.setLikesCount(post.getLikesCount()+1);
        postRepository.save(post);
    }

    @Transactional
    public void removeLike(@Valid RemoveLikeCommand command) {

        Post post = verifyPostExists(command.postId());

        Like like = likeRepository.findByUserIdAndPostAndDeletedFalse(command.userId(), post)
                .orElseThrow(() -> new ConflictException("User has not liked this post"));

        like.setDeleted(true);
        likeRepository.save(like);

        if (post.getLikesCount() > 0) {
            post.setLikesCount(post.getLikesCount() - 1);
            postRepository.save(post);
        }
    }

    @Transactional
    public void createComment(@Valid CommentCommand command) {

        Post post = verifyPostExists(command.postId());

        Comment comment = Comment.builder()
                .post(post)
                .userId(command.userId())
                .parentComment(command.parentCommentId() != null
                        //just set the id , saves me time instead of fetching the comment
                        ? Comment.builder().id(command.parentCommentId()).build()
                        : null)
                .content(command.content())
                .deleted(false)
                .build();

        commentRepository.save(comment);

        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepository.save(post);
    }

    public PaginatedResponse<PostLikeDto> getLikesByPost(Long postId, Pageable pageable) {

        Post post = verifyPostExists(postId);

        Page<Like> likes = likeRepository.findByPostIdAndDeletedFalse(postId, pageable);

        List<PostLikeDto> dtoList = likes.stream()
                .map(like -> new PostLikeDto(
                        like.getId(),
                        like.getUserId(),
                        postId
                )).toList();

        return new PaginatedResponse<>(
                dtoList,
                likes.getNumber(),
                likes.getTotalPages(),
                likes.getTotalElements()
        );
    }

    public Post verifyPostExists(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + postId + " not found"));
    }







}
