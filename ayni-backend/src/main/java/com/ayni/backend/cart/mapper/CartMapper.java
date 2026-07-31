package com.ayni.backend.cart.mapper;

import com.ayni.backend.cart.dto.CartItemResponse;
import com.ayni.backend.cart.dto.CartResponse;
import com.ayni.backend.cart.entity.Cart;
import com.ayni.backend.cart.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {

    public CartItemResponse toItemResponse(CartItem item) {
        BigDecimal subtotal = item.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(item.getCantidad()));

        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getNombre(),
                item.getProduct().getSlug(),
                item.getProduct().getImagenPrincipalUrl(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                subtotal,
                item.getProduct().getStock()
        );
    }

    public CartResponse toResponse(Cart cart, List<CartItem> items) {
        List<CartItemResponse> itemResponses = items.stream()
                .map(this::toItemResponse)
                .toList();

        BigDecimal subtotal = itemResponses.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = itemResponses.stream()
                .mapToInt(CartItemResponse::cantidad)
                .sum();

        return new CartResponse(
                cart.getId(),
                itemResponses,
                totalItems,
                subtotal
        );
    }
}