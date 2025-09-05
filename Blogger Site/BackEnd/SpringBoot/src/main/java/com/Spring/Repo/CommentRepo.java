package com.Spring.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Spring.Model.Comments;

@Repository
public interface CommentRepo extends JpaRepository<Comments, Long> {
    List<Comments> findByBlogPostId(Long blogPostId);
    Optional<Comments> findByIdAndCommenterId(Long commentId, Long commenterId);
    void deleteByBlogPostId(Long blogPostId);
}