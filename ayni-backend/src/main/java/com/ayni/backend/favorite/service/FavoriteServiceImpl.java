package com.ayni.backend.favorite.service;

import com.ayni.backend.common.constants.MessageError;
import com.ayni.backend.common.exception.BusinessException;
import com.ayni.backend.common.exception.ResourceNotFoundException;
import com.ayni.backend.favorite.dto.FavoriteResponse;
import com.ayni.backend.favorite.entity.Favorite;
import com.ayni.backend.favorite.mapper.FavoriteMapper;
import com.ayni.backend.favorite.repository.FavoriteRepository;
import com.ayni.backend.product.entity.Product;
import com.ayni.backend.product.repository.ProductRepository;
import com.ayni.backend.user.entity.User;
import com.ayni.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ayni.backend.common.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final FavoriteMapper favoriteMapper;

    @Override
    public List<FavoriteResponse> findMyFavorites() {
        return favoriteRepository.findByUserEmailOrderByCreadoEnDesc(SecurityUtil.getCurrentUserEmail())
                .stream()
                .map(favoriteMapper::toResponse)
                .toList();
    }

    @Override
    public FavoriteResponse addFavorite(Long productId) {
        if (favoriteRepository.existsByUserEmailAndProductId(SecurityUtil.getCurrentUserEmail(), productId)) {
            throw new BusinessException("El producto ya está en favoritos.");
        }

        User user = userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.USER_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .filter(Product::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.PRODUCT_NOT_FOUND));

        Favorite favorite = Favorite.builder()
                .user(user)
                .product(product)
                .creadoEn(LocalDateTime.now())
                .build();

        return favoriteMapper.toResponse(favoriteRepository.save(favorite));
    }

    @Override
    public void removeFavorite(Long productId) {
        Favorite favorite = favoriteRepository.findByUserEmailAndProductId(SecurityUtil.getCurrentUserEmail(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("El producto no está en favoritos."));

        favoriteRepository.delete(favorite);
    }
}