package com.Spring.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Spring.Model.BlogPost;
import com.Spring.Model.Comments;
import com.Spring.Repo.BlogPostRepo;
import com.Spring.Repo.CommentRepo;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepository;

    @Autowired
    private BlogPostRepo blogPostRepository;

    // Add a comment to a blog post
    public Comments addComment(Long blogPostId, Comments comment) {
        BlogPost blogPost = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new RuntimeException("Blog post not found with ID: " + blogPostId));
        //comment.setBlogPost(blogPost);
        return commentRepository.save(comment);
    }

    // Get all comments for a blog post
    public List<Comments> getCommentsByBlogPostId(Long blogPostId) {
        return commentRepository.findByBlogPostId(blogPostId);
    }

    // Delete a comment by ID
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("Comment not found with ID: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }

}
