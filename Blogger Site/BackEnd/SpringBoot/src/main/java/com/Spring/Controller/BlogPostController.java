package com.Spring.Controller;

import com.Spring.Model.BlogPost;
import com.Spring.Model.Category;
import com.Spring.Model.User;
import com.Spring.Repo.CategoryRepository;
import com.Spring.Repo.Repo;
import com.Spring.Service.BlogPostService;
import DTO.BlogpostDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/blogs")
@CrossOrigin(origins = "http://localhost:3000")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private Repo repo;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/add")
    public ResponseEntity<?> createBlogPost(@RequestBody BlogPost blogPost, Authentication auth) {
        if (auth == null || !auth.isAuthenticated())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<User> userOpt = repo.findByUsername(auth.getName());
        if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        User author = userOpt.get();

        Optional<Category> categoryOpt = categoryRepository.findById(blogPost.getCategory().getId());
        if (categoryOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        Category category = categoryOpt.get();

        blogPost.setAuthor(author);
        blogPost.setCategory(category);

        BlogPost savedPost = blogPostService.save(blogPost);
        BlogpostDto dto = blogPostService.convertToDTO(savedPost);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogById(@PathVariable String id) {
        Long blogId;
        try {
            blogId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid blog ID");
        }

        Optional<BlogPost> blogPostOpt = blogPostService.findById(blogId);
        if (blogPostOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        BlogpostDto dto = blogPostService.convertToDTO(blogPostOpt.get());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable String id, Authentication auth) {
        Long blogId;
        try {
            blogId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid blog ID");
        }

        Optional<BlogPost> blogPostOpt = blogPostService.findById(blogId);
        if (blogPostOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        BlogPost blogPost = blogPostOpt.get();

        if (auth == null || !auth.isAuthenticated())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<User> userOpt = repo.findByUsername(auth.getName());
        if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = userOpt.get();

        if (!blogPost.getAuthor().getUsername().equals(user.getUsername()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed to delete this post");

        blogPostService.deleteById(blogId);
        return ResponseEntity.ok("Blog post deleted successfully");
    }
}
