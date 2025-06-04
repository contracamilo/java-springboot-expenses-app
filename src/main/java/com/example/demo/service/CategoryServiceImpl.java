package com.example.demo.service;

import com.example.demo.dto.CategoryDto;
import com.example.demo.exception.CategoryNameAlreadyExistsException;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        categoryRepository.findByName(categoryDto.getName()).ifPresent(c -> {
            throw new CategoryNameAlreadyExistsException("Una categoría con el nombre '" + categoryDto.getName() + "' ya existe.");
        });
        Category category = convertToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + categoryId));
        return convertToDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con nombre: " + name));
        return convertToDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + categoryId));

        // Check if name is being changed and if the new name already exists for another category
        if (!category.getName().equals(categoryDto.getName())) {
            Optional<Category> existingCategoryWithNewName = categoryRepository.findByName(categoryDto.getName());
            if (existingCategoryWithNewName.isPresent() && !existingCategoryWithNewName.get().getId().equals(categoryId)) {
                throw new CategoryNameAlreadyExistsException("Otra categoría con el nombre '" + categoryDto.getName() + "' ya existe.");
            }
        }

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setFixed(categoryDto.isFixed()); // Allow updating 'fixed' status as per DTO
        Category updatedCategory = categoryRepository.save(category);
        return convertToDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + categoryId));
        // Associated expenses will be removed due to cascade = CascadeType.REMOVE in Category entity
        categoryRepository.delete(category);
    }

    private CategoryDto convertToDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isFixed()
        );
    }

    private Category convertToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        // category.setId(categoryDto.getId()); // ID should not be set from DTO for new entity
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setFixed(categoryDto.isFixed());
        return category;
    }
} 