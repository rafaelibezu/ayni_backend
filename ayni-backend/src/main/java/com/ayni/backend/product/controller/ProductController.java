package com.ayni.backend.product.controller;

import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import com.ayni.backend.product.dto.ProductDetailResponse;
import com.ayni.backend.product.dto.ProductResponse;
import com.ayni.backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(ApiRoutes.PRODUCTS)
    public ApiResponse<Page<ProductResponse>> findAll(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return ApiResponse.ok(
                "Productos listados correctamente.",
                productService.findAll(category, page, size)
        );
    }

    @GetMapping(ApiRoutes.PRODUCTS + "/{slug}")
    public ApiResponse<ProductDetailResponse> findBySlug(
            @PathVariable String slug
    ) {
        return ApiResponse.ok(
                "Producto encontrado correctamente.",
                productService.findBySlug(slug)
        );
    }

    @GetMapping(ApiRoutes.PRODUCTS + "/featured")
    public ApiResponse<List<ProductResponse>> findFeatured() {
        return ApiResponse.ok(
                "Productos destacados listados correctamente.",
                productService.findFeatured()
        );
    }
}