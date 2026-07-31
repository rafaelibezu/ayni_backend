package com.ayni.backend.category.service;

import com.ayni.backend.category.dto.CategoryRequest;
import com.ayni.backend.category.dto.CategoryResponse;
import com.ayni.backend.category.entity.Category;
import com.ayni.backend.category.mapper.CategoryMapper;
import com.ayni.backend.category.repository.CategoryRepository;
import com.ayni.backend.common.constants.MessageError;
import com.ayni.backend.common.exception.BadRequestException;
import com.ayni.backend.common.exception.ResourceNotFoundException;
import com.ayni.backend.common.util.GlobalUtil;
import com.ayni.backend.common.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> findAllActive() {
        return categoryRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        String slug = resolveSlug(request.nombre(), request.slug());

        if (categoryRepository.existsBySlug(slug)) {
            throw new BadRequestException("Ya existe una categoría con ese slug.");
        }

        Category category = Category.builder()
                .nombre(GlobalUtil.cleanText(request.nombre()))
                .slug(slug)
                .descripcion(GlobalUtil.cleanText(request.descripcion()))
                .imagenUrl(GlobalUtil.cleanText(request.imagenUrl()))
                .activo(true)
                .creadoEn(LocalDateTime.now())
                .build();

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.CATEGORY_NOT_FOUND));

        String slug = resolveSlug(request.nombre(), request.slug());

        if (!category.getSlug().equals(slug) && categoryRepository.existsBySlug(slug)) {
            throw new BadRequestException("Ya existe otra categoría con ese slug.");
        }

        category.setNombre(GlobalUtil.cleanText(request.nombre()));
        category.setSlug(slug);
        category.setDescripcion(GlobalUtil.cleanText(request.descripcion()));
        category.setImagenUrl(GlobalUtil.cleanText(request.imagenUrl()));
        category.setActualizadoEn(LocalDateTime.now());

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse toggleStatus(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.CATEGORY_NOT_FOUND));

        category.setActivo(!Boolean.TRUE.equals(category.getActivo()));
        category.setActualizadoEn(LocalDateTime.now());

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    private String resolveSlug(String nombre, String requestedSlug) {
        String slug = GlobalUtil.isBlank(requestedSlug)
                ? SlugUtil.generateSlug(nombre)
                : SlugUtil.generateSlug(requestedSlug);

        if (GlobalUtil.isBlank(slug)) {
            throw new BadRequestException("El slug de la categoría es inválido.");
        }

        return slug;
    }

    @Override
    public List<CategoryResponse> findAllForAdmin() {
        return categoryRepository.findAllByOrderByIdDesc()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }
}