package com.ayni.backend.product.controller;

import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import com.ayni.backend.product.dto.ProductRequest;
import com.ayni.backend.product.dto.ProductResponse;
import com.ayni.backend.product.dto.UpdateProductStockRequest;
import com.ayni.backend.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import com.ayni.backend.product.dto.ProductDetailResponse;
import com.ayni.backend.product.dto.ProductImageRequest;

@RestController
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @PostMapping(ApiRoutes.ADMIN + "/products")
    public ApiResponse<ProductResponse> create(
            @Valid @RequestBody ProductRequest request
    ) {
        return ApiResponse.ok(
                "Producto creado correctamente.",
                productService.create(request)
        );
    }

    @PutMapping(ApiRoutes.ADMIN + "/products/{id}")
    public ApiResponse<ProductResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request
    ) {
        return ApiResponse.ok(
                "Producto actualizado correctamente.",
                productService.update(id, request)
        );
    }

    @PatchMapping(ApiRoutes.ADMIN + "/products/{id}/stock")
    public ApiResponse<ProductResponse> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductStockRequest request
    ) {
        return ApiResponse.ok(
                "Stock actualizado correctamente.",
                productService.updateStock(id, request)
        );
    }

    @PatchMapping(ApiRoutes.ADMIN + "/products/{id}/toggle-status")
    public ApiResponse<ProductResponse> toggleStatus(
            @PathVariable Long id
    ) {
        return ApiResponse.ok(
                "Estado del producto actualizado correctamente.",
                productService.toggleStatus(id)
        );
    }

    /*Listado Admin Productos*/
    @GetMapping(ApiRoutes.ADMIN + "/products")
    public ApiResponse<Page<ProductResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return ApiResponse.ok(
                "Productos obtenidos correctamente.",
                productService.findAllForAdmin(page, size)
        );
    }

    @PostMapping(ApiRoutes.ADMIN + "/products/{productId}/images")
    public ApiResponse<ProductDetailResponse> addImage(
            @PathVariable Long productId,
            @Valid @RequestBody ProductImageRequest request
    ) {
        return ApiResponse.ok(
                "Imagen agregada correctamente.",
                productService.addImage(productId, request)
        );
    }

    @DeleteMapping(ApiRoutes.ADMIN + "/products/images/{imageId}")
    public ApiResponse<Void> deleteImage(
            @PathVariable Long imageId
    ) {
        productService.deleteImage(imageId);

        return ApiResponse.ok(
                "Imagen eliminada correctamente.",
                null
        );
    }
}