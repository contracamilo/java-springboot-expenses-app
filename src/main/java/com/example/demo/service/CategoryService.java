package com.example.demo.service;

import com.example.demo.dto.CategoryDto;
import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(Long categoryId);
    CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto);
    void deleteCategory(Long categoryId);
    CategoryDto getCategoryByName(String name); // Added for convenience and to check for duplicates
} 