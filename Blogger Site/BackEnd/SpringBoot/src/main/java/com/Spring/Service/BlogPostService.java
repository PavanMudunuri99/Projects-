package com.Spring.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.Spring.Model.BlogPost;
import com.Spring.Model.User;
import com.Spring.Repo.BlogPostRepo;

import DTO.BlogpostDto;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepo blogPostRepo;

    // All blog posts
    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepo.findAll();
    }

    // Find by ID
    public Optional<BlogPost> findById(Long id) {
        return blogPostRepo.findById(id);
    }

    // Save or update
    public BlogPost save(BlogPost post) {
        return blogPostRepo.save(post);
    }

    // Delete by ID
    public void deleteById(Long id) {
        blogPostRepo.deleteById(id);
    }

    // Search by keyword
    public List<BlogPost> searchByKeyword(String keyword) {
        return blogPostRepo.findByTitleContainingIgnoreCase(keyword);
    }

    // Pagination support
    public Page<BlogPost> getAllPosts(int page, int size) {
        return blogPostRepo.findAll(PageRequest.of(page, size));
    }

    // Get posts by author/user ID
    public List<BlogPost> findByAuthorId(Long userId) {
        User author = new User();
        author.setId(userId);
        return blogPostRepo.findByAuthor(author);
    }

    public BlogpostDto convertToDTO(BlogPost savedPost) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToDTO'");
    }
}

