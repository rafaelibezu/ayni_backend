package com.ayni.backend.category.service;

import com.ayni.backend.category.dto.CategoryRequest;
import com.ayni.backend.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> findAllActive();

    List<CategoryResponse> findAllForAdmin();

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    CategoryResponse toggleStatus(Long id);
}