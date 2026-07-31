package com.ayni.backend.category.repository;

import com.ayni.backend.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByActivoTrueOrderByNombreAsc();

    Optional<Category> findBySlugAndActivoTrue(String slug);

    boolean existsBySlug(String slug);

    List<Category> findAllByOrderByIdDesc();
}