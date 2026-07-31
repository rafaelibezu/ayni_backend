package com.ayni.backend.cart.controller;

import com.ayni.backend.cart.dto.AddCartItemRequest;
import com.ayni.backend.cart.dto.CartResponse;
import com.ayni.backend.cart.dto.UpdateCartItemRequest;
import com.ayni.backend.cart.service.CartService;
import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping(ApiRoutes.CART)
    public ApiResponse<CartResponse> getMyCart() {
        return ApiResponse.ok(
                "Carrito obtenido correctamente.",
                cartService.getMyCart()
        );
    }

    @PostMapping(ApiRoutes.CART + "/items")
    public ApiResponse<CartResponse> addItem(
            @Valid @RequestBody AddCartItemRequest request
    ) {
        return ApiResponse.ok(
                "Producto agregado al carrito.",
                cartService.addItem(request)
        );
    }

    @PutMapping(ApiRoutes.CART + "/items/{itemId}")
    public ApiResponse<CartResponse> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        return ApiResponse.ok(
                "Cantidad actualizada correctamente.",
                cartService.updateItem(itemId, request)
        );
    }

    @DeleteMapping(ApiRoutes.CART + "/items/{itemId}")
    public ApiResponse<Void> removeItem(
            @PathVariable Long itemId
    ) {
        cartService.removeItem(itemId);

        return ApiResponse.ok(
                "Producto eliminado del carrito.",
                null
        );
    }

    @DeleteMapping(ApiRoutes.CART + "/clear")
    public ApiResponse<Void> clearCart() {
        cartService.clearCart();

        return ApiResponse.ok(
                "Carrito limpiado correctamente.",
                null
        );
    }
}