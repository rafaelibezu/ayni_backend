package com.ayni.backend.favorite.repository;

import com.ayni.backend.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserEmailOrderByCreadoEnDesc(String email);

    boolean existsByUserEmailAndProductId(String email, Long productId);

    Optional<Favorite> findByUserEmailAndProductId(String email, Long productId);
}