package com.ayni.backend.category.controller;

import com.ayni.backend.category.dto.CategoryResponse;
import com.ayni.backend.category.service.CategoryService;
import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(ApiRoutes.CATEGORIES)
    public ApiResponse<List<CategoryResponse>> findAllActive() {
        return ApiResponse.ok(
                "Categorías listadas correctamente.",
                categoryService.findAllActive()
        );
    }
}