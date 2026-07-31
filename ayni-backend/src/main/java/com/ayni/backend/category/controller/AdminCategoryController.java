package com.ayni.backend.category.controller;

import com.ayni.backend.category.dto.CategoryRequest;
import com.ayni.backend.category.dto.CategoryResponse;
import com.ayni.backend.category.service.CategoryService;
import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping(ApiRoutes.ADMIN + "/categories")
    public ApiResponse<CategoryResponse> create(
            @Valid @RequestBody CategoryRequest request
    ) {
        return ApiResponse.ok(
                "Categoría creada correctamente.",
                categoryService.create(request)
        );
    }

    @PutMapping(ApiRoutes.ADMIN + "/categories/{id}")
    public ApiResponse<CategoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request
    ) {
        return ApiResponse.ok(
                "Categoría actualizada correctamente.",
                categoryService.update(id, request)
        );
    }

    @PatchMapping(ApiRoutes.ADMIN + "/categories/{id}/toggle-status")
    public ApiResponse<CategoryResponse> toggleStatus(
            @PathVariable Long id
    ) {
        return ApiResponse.ok(
                "Estado de categoría actualizado correctamente.",
                categoryService.toggleStatus(id)
        );
    }

    /*Listado Categoria Administrador*/
    @GetMapping(ApiRoutes.ADMIN + "/categories")
    public ApiResponse<List<CategoryResponse>> findAll() {
        return ApiResponse.ok(
                "Categorías obtenidas correctamente.",
                categoryService.findAllForAdmin()
        );
    }
}