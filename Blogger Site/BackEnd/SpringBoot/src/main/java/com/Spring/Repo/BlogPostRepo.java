package com.Spring.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Spring.Model.BlogPost;
import com.Spring.Model.User;

@Repository
public interface BlogPostRepo extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findByTitleContainingIgnoreCase(String keyword);

    List<BlogPost> findByCategoryId(Long categoryId);

    List<BlogPost> findByAuthor(User author);

    Page<BlogPost> findAll(Pageable pageable);

    // Search with pagination
    @Query("SELECT bp FROM BlogPost bp WHERE LOWER(bp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(bp.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<BlogPost> searchByTitleOrContent(@Param("keyword") String keyword, Pageable pageable);
}
