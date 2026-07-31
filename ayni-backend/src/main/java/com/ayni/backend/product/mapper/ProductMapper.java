package com.ayni.backend.product.mapper;

import com.ayni.backend.product.dto.ProductDetailResponse;
import com.ayni.backend.product.dto.ProductImageResponse;
import com.ayni.backend.product.dto.ProductResponse;
import com.ayni.backend.product.entity.Product;
import com.ayni.backend.product.entity.ProductImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getNombre(),
                product.getSlug(),
                product.getDescripcion(),
                product.getSku(),
                product.getPrecio(),
                product.getPrecioComparacion(),
                product.getStock(),
                product.getImagenPrincipalUrl(),
                product.getBadge(),
                product.getDestacado(),
                product.getCategory().getNombre(),
                product.getCategory().getSlug()
        );
    }

    public ProductImageResponse toImageResponse(ProductImage image) {
        return new ProductImageResponse(
                image.getId(),
                image.getImagenUrl(),
                image.getOrden()
        );
    }

    public ProductDetailResponse toDetailResponse(Product product, List<ProductImage> images) {
        List<ProductImageResponse> imageResponses = images.stream()
                .map(this::toImageResponse)
                .toList();

        return new ProductDetailResponse(
                product.getId(),
                product.getNombre(),
                product.getSlug(),
                product.getDescripcion(),
                product.getSku(),
                product.getPrecio(),
                product.getPrecioComparacion(),
                product.getStock(),
                product.getImagenPrincipalUrl(),
                product.getBadge(),
                product.getDestacado(),
                product.getCategory().getNombre(),
                product.getCategory().getSlug(),
                imageResponses
        );
    }
}