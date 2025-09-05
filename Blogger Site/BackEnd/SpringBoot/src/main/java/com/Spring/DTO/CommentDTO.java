package DTO;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long blogPostId;
    private AuthorDTO commenter;

    // Constructors
    public CommentDTO() {}

    public CommentDTO(Long id, String content, LocalDateTime createdAt, 
                     Long blogPostId, AuthorDTO commenter) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.blogPostId = blogPostId;
        this.commenter = commenter;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getBlogPostId() { return blogPostId; }
    public void setBlogPostId(Long blogPostId) { this.blogPostId = blogPostId; }

    public AuthorDTO getCommenter() { return commenter; }
    public void setCommenter(AuthorDTO commenter) { this.commenter = commenter; }
}