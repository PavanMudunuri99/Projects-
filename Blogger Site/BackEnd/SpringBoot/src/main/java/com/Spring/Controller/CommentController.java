package com.Spring.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.Spring.Model.BlogPost;
import com.Spring.Model.Comments;
import com.Spring.Model.User;
import com.Spring.Repo.BlogPostRepo;
import com.Spring.Repo.Repo;
import com.Spring.Service.CommentService;

import DTO.AuthorDTO;
import DTO.CommentDTO;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogPostRepo blogPostRepo;

    @Autowired
    private Repo repo;

    // Add a comment
    @PostMapping("/{blogPostId}")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long blogPostId,
                                                 @RequestBody CommentDTO commentDTO,
                                                 Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Get user
        User user = repo.findByUsername(authentication.getName());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Get blog post
        BlogPost blogPost = blogPostRepo.findById(blogPostId).orElse(null);
        if (blogPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Create comment
        Comments comment = new Comments();
        comment.setContent(commentDTO.getContent());
        comment.setCommenter(user);
        comment.setBlogPost(blogPost);

        // Save comment
        Comments savedComment = commentService.addComment(blogPostId, comment);

        // Convert to DTO
        CommentDTO savedCommentDTO = new CommentDTO(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                blogPostId,
                new AuthorDTO(user.getUsername())
        );

        return ResponseEntity.ok(savedCommentDTO);
    }

    // Get comments for a blog post
    @GetMapping("/{blogPostId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByBlogPostId(@PathVariable Long blogPostId) {
        List<CommentDTO> commentDTOs = commentService.getCommentsByBlogPostId(blogPostId)
                .stream()
                .map(comment -> new CommentDTO(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        blogPostId,
                        new AuthorDTO(comment.getCommenter().getUsername())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(commentDTOs);
    }

    // Delete a comment
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted successfully!");
    }
}
