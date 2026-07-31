package com.ayni.backend.cart.service;

import com.ayni.backend.cart.dto.AddCartItemRequest;
import com.ayni.backend.cart.dto.CartResponse;
import com.ayni.backend.cart.dto.UpdateCartItemRequest;

public interface CartService {

    CartResponse getMyCart();

    CartResponse addItem(AddCartItemRequest request);

    CartResponse updateItem(Long itemId, UpdateCartItemRequest request);

    void removeItem(Long itemId);

    void clearCart();
}