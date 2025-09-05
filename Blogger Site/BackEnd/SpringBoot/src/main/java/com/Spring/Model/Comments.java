package com.Spring.Model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-comments")
    private User commenter;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "blog_post_id", nullable = false)
    @JsonBackReference("blog-comments")
    private BlogPost blogPost;

    // --- Constructors ---
    public Comments() {}

    public Comments(Long id, User commenter, String content, LocalDateTime createdAt, BlogPost blogPost) {
        this.id = id;
        this.commenter = commenter;
        this.content = content;
        this.createdAt = createdAt;
        this.blogPost = blogPost;
    }

    public Comments(User commenter, String content, BlogPost blogPost) {
        this.commenter = commenter;
        this.content = content;
        this.blogPost = blogPost;
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public User getCommenter() {
        return commenter;
    }
    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BlogPost getBlogPost() {
        return blogPost;
    }

    public void setBlogPost(BlogPost blogPost) {
        this.blogPost = blogPost;
    }
}
