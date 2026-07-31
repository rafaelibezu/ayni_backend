package com.ayni.backend.category.mapper;

import com.ayni.backend.category.dto.CategoryResponse;
import com.ayni.backend.category.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getNombre(),
                category.getSlug(),
                category.getDescripcion(),
                category.getImagenUrl(),
                category.getActivo()
        );
    }
}