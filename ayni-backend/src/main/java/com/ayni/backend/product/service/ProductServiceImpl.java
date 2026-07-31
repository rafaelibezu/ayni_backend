package com.ayni.backend.product.service;

import com.ayni.backend.category.entity.Category;
import com.ayni.backend.category.repository.CategoryRepository;
import com.ayni.backend.common.constants.MessageError;
import com.ayni.backend.common.exception.BadRequestException;
import com.ayni.backend.common.exception.ResourceNotFoundException;
import com.ayni.backend.common.util.GlobalUtil;
import com.ayni.backend.common.util.SlugUtil;
import com.ayni.backend.product.dto.ProductDetailResponse;
import com.ayni.backend.product.dto.ProductImageRequest;
import com.ayni.backend.product.dto.ProductRequest;
import com.ayni.backend.product.dto.ProductResponse;
import com.ayni.backend.product.dto.UpdateProductStockRequest;
import com.ayni.backend.product.entity.Product;
import com.ayni.backend.product.entity.ProductImage;
import com.ayni.backend.product.mapper.ProductMapper;
import com.ayni.backend.product.repository.ProductImageRepository;
import com.ayni.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<ProductResponse> findAll(String categorySlug, int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<Product> products;

        if (categorySlug == null || categorySlug.isBlank()) {
            products = productRepository.findByActivoTrue(pageable);
        } else {
            products = productRepository.findByActivoTrueAndCategorySlug(categorySlug, pageable);
        }

        return products.map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> findAllForAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findAllByOrderByIdDesc(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public ProductDetailResponse findBySlug(String slug) {
        Product product = productRepository.findBySlugAndActivoTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.PRODUCT_NOT_FOUND));

        return buildProductDetailResponse(product);
    }

    @Override
    public List<ProductResponse> findFeatured() {
        return productRepository.findTop8ByActivoTrueAndDestacadoTrueOrderByIdDesc()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.CATEGORY_NOT_FOUND));

        String slug = resolveSlug(request.nombre(), request.slug());
        String sku = GlobalUtil.cleanText(request.sku());

        if (productRepository.existsBySlug(slug)) {
            throw new BadRequestException("Ya existe un producto con ese slug.");
        }

        if (productRepository.existsBySku(sku)) {
            throw new BadRequestException("Ya existe un producto con ese SKU.");
        }

        Product product = Product.builder()
                .category(category)
                .nombre(GlobalUtil.cleanText(request.nombre()))
                .slug(slug)
                .descripcion(GlobalUtil.cleanText(request.descripcion()))
                .sku(sku)
                .precio(request.precio())
                .precioComparacion(request.precioComparacion())
                .stock(request.stock())
                .imagenPrincipalUrl(GlobalUtil.cleanText(request.imagenPrincipalUrl()))
                .badge(GlobalUtil.cleanText(request.badge()))
                .destacado(Boolean.TRUE.equals(request.destacado()))
                .activo(true)
                .creadoEn(LocalDateTime.now())
                .build();

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.PRODUCT_NOT_FOUND));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.CATEGORY_NOT_FOUND));

        String slug = resolveSlug(request.nombre(), request.slug());
        String sku = GlobalUtil.cleanText(request.sku());

        if (!product.getSlug().equals(slug) && productRepository.existsBySlug(slug)) {
            throw new BadRequestException("Ya existe otro producto con ese slug.");
        }

        if (!product.getSku().equals(sku) && productRepository.existsBySku(sku)) {
            throw new BadRequestException("Ya existe otro producto con ese SKU.");
        }

        product.setCategory(category);
        product.setNombre(GlobalUtil.cleanText(request.nombre()));
        product.setSlug(slug);
        product.setDescripcion(GlobalUtil.cleanText(request.descripcion()));
        product.setSku(sku);
        product.setPrecio(request.precio());
        product.setPrecioComparacion(request.precioComparacion());
        product.setStock(request.stock());
        product.setImagenPrincipalUrl(GlobalUtil.cleanText(request.imagenPrincipalUrl()));
        product.setBadge(GlobalUtil.cleanText(request.badge()));
        product.setDestacado(Boolean.TRUE.equals(request.destacado()));
        product.setActualizadoEn(LocalDateTime.now());

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse updateStock(Long id, UpdateProductStockRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.PRODUCT_NOT_FOUND));

        product.setStock(request.stock());
        product.setActualizadoEn(LocalDateTime.now());

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse toggleStatus(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.PRODUCT_NOT_FOUND));

        product.setActivo(!Boolean.TRUE.equals(product.getActivo()));
        product.setActualizadoEn(LocalDateTime.now());

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDetailResponse addImage(Long productId, ProductImageRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.PRODUCT_NOT_FOUND));

        String imageUrl = GlobalUtil.cleanText(request.imagenUrl());

        if (productImageRepository.existsByProductIdAndImagenUrl(productId, imageUrl)) {
            throw new BadRequestException("Esta imagen ya está registrada para el producto.");
        }

        Integer order = request.orden();

        if (order == null) {
            order = productImageRepository.findByProductIdOrderByOrdenAsc(productId).size() + 1;
        }

        ProductImage productImage = ProductImage.builder()
                .product(product)
                .imagenUrl(imageUrl)
                .orden(order)
                .creadoEn(LocalDateTime.now())
                .build();

        productImageRepository.save(productImage);

        product.setActualizadoEn(LocalDateTime.now());
        productRepository.save(product);

        return buildProductDetailResponse(product);
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) {
        ProductImage productImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen de producto no encontrada."));

        Product product = productImage.getProduct();

        productImageRepository.delete(productImage);

        product.setActualizadoEn(LocalDateTime.now());
        productRepository.save(product);
    }

    private ProductDetailResponse buildProductDetailResponse(Product product) {
        List<ProductImage> images =
                productImageRepository.findByProductIdOrderByOrdenAsc(product.getId());

        return productMapper.toDetailResponse(product, images);
    }

    private String resolveSlug(String nombre, String requestedSlug) {
        String slug = GlobalUtil.isBlank(requestedSlug)
                ? SlugUtil.generateSlug(nombre)
                : SlugUtil.generateSlug(requestedSlug);

        if (GlobalUtil.isBlank(slug)) {
            throw new BadRequestException("El slug del producto es inválido.");
        }

        return slug;
    }
}