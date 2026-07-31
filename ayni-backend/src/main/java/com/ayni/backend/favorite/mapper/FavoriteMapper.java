package com.ayni.backend.favorite.mapper;

import com.ayni.backend.favorite.dto.FavoriteResponse;
import com.ayni.backend.favorite.entity.Favorite;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

    public FavoriteResponse toResponse(Favorite favorite) {
        var product = favorite.getProduct();

        return new FavoriteResponse(
                favorite.getId(),
                product.getId(),
                product.getNombre(),
                product.getSlug(),
                product.getPrecio(),
                product.getPrecioComparacion(),
                product.getStock(),
                product.getImagenPrincipalUrl(),
                product.getBadge(),
                product.getCategory().getNombre(),
                product.getCategory().getSlug(),
                favorite.getCreadoEn()
        );
    }
}