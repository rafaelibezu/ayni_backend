package com.ayni.backend.product.repository;

import com.ayni.backend.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductIdOrderByOrdenAsc(Long productId);

    boolean existsByProductIdAndImagenUrl(Long productId, String imagenUrl);
}