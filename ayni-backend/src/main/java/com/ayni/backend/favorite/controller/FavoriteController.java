package com.ayni.backend.favorite.controller;

import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import com.ayni.backend.favorite.dto.FavoriteResponse;
import com.ayni.backend.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping(ApiRoutes.FAVORITES)
    public ApiResponse<List<FavoriteResponse>> findMyFavorites() {
        return ApiResponse.ok(
                "Favoritos listados correctamente.",
                favoriteService.findMyFavorites()
        );
    }

    @PostMapping(ApiRoutes.FAVORITES + "/{productId}")
    public ApiResponse<FavoriteResponse> addFavorite(
            @PathVariable Long productId
    ) {
        return ApiResponse.ok(
                "Producto agregado a favoritos.",
                favoriteService.addFavorite(productId)
        );
    }

    @DeleteMapping(ApiRoutes.FAVORITES + "/{productId}")
    public ApiResponse<Void> removeFavorite(
            @PathVariable Long productId
    ) {
        favoriteService.removeFavorite(productId);

        return ApiResponse.ok(
                "Producto eliminado de favoritos.",
                null
        );
    }
}