package com.ayni.backend.product.service;

import com.ayni.backend.product.dto.ProductDetailResponse;
import com.ayni.backend.product.dto.ProductImageRequest;
import com.ayni.backend.product.dto.ProductRequest;
import com.ayni.backend.product.dto.ProductResponse;
import com.ayni.backend.product.dto.UpdateProductStockRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Page<ProductResponse> findAll(String categorySlug, int page, int size);

    Page<ProductResponse> findAllForAdmin(int page, int size);

    ProductDetailResponse findBySlug(String slug);

    List<ProductResponse> findFeatured();

    ProductResponse create(ProductRequest request);

    ProductResponse update(Long id, ProductRequest request);

    ProductResponse updateStock(Long id, UpdateProductStockRequest request);

    ProductResponse toggleStatus(Long id);

    ProductDetailResponse addImage(Long productId, ProductImageRequest request);

    void deleteImage(Long imageId);
}