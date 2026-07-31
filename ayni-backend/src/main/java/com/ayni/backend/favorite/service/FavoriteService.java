package com.ayni.backend.favorite.service;

import com.ayni.backend.favorite.dto.FavoriteResponse;

import java.util.List;

public interface FavoriteService {

    List<FavoriteResponse> findMyFavorites();

    FavoriteResponse addFavorite(Long productId);

    void removeFavorite(Long productId);
}