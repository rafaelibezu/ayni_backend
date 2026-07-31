package com.ayni.backend.product.repository;

import com.ayni.backend.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByActivoTrue(Pageable pageable);

    Page<Product> findByActivoTrueAndCategorySlug(String categorySlug, Pageable pageable);

    Optional<Product> findBySlugAndActivoTrue(String slug);

    List<Product> findTop8ByActivoTrueAndDestacadoTrueOrderByIdDesc();

    boolean existsBySlug(String slug);

    boolean existsBySku(String sku);

    Page<Product> findAllByOrderByIdDesc(Pageable pageable);
}