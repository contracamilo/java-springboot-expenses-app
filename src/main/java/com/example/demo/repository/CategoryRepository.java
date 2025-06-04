package com.example.demo.repository;

import com.example.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    // No se necesitan métodos específicos de owner ya que las categorías fijas son globales
    // y las personalizadas no están ligadas a un usuario en este modelo simplificado del prompt.
    // Si se quisiera un modelo donde las categorías no fijas pertenecen a un User,
    // se añadirían aquí métodos como findByOwner(User user).
} 