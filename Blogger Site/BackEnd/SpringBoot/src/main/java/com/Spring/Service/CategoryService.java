package com.Spring.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Spring.Model.Category;
import com.Spring.Repo.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Get all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Get category by ID
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Create a new category
    public Category createCategory(Category category) {
        categoryRepository.findByName(category.getName())
            .ifPresent(c -> {
                throw new RuntimeException("Category with name '" + category.getName() + "' already exists");
            });
        return categoryRepository.save(category);
    }

    // Delete category by ID
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

    // Find category by name
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
}
