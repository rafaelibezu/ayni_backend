package com.ayni.backend.cart.service;

import com.ayni.backend.cart.dto.AddCartItemRequest;
import com.ayni.backend.cart.dto.CartResponse;
import com.ayni.backend.cart.dto.UpdateCartItemRequest;
import com.ayni.backend.cart.entity.Cart;
import com.ayni.backend.cart.entity.CartItem;
import com.ayni.backend.cart.mapper.CartMapper;
import com.ayni.backend.cart.repository.CartItemRepository;
import com.ayni.backend.cart.repository.CartRepository;
import com.ayni.backend.common.constants.MessageError;
import com.ayni.backend.common.exception.BadRequestException;
import com.ayni.backend.common.exception.ResourceNotFoundException;
import com.ayni.backend.product.entity.Product;
import com.ayni.backend.product.repository.ProductRepository;
import com.ayni.backend.user.entity.User;
import com.ayni.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ayni.backend.common.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {


    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @Override
    public CartResponse getMyCart() {
        Cart cart = getOrCreateActiveCart();
        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItem(AddCartItemRequest request) {
        Cart cart = getOrCreateActiveCart();

        Product product = productRepository.findById(request.productId())
                .filter(Product::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.PRODUCT_NOT_FOUND));

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        int newQuantity = request.cantidad();

        if (item != null) {
            newQuantity = item.getCantidad() + request.cantidad();
        }

        validateStock(product, newQuantity);

        if (item == null) {
            item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .cantidad(request.cantidad())
                    .precioUnitario(product.getPrecio())
                    .creadoEn(LocalDateTime.now())
                    .build();
        } else {
            item.setCantidad(newQuantity);
            item.setActualizadoEn(LocalDateTime.now());
        }

        cartItemRepository.save(item);
        cart.setActualizadoEn(LocalDateTime.now());

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateItem(Long itemId, UpdateCartItemRequest request) {
        CartItem item = cartItemRepository.findByIdAndCartUserEmail(itemId, SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Item de carrito no encontrado."));

        validateStock(item.getProduct(), request.cantidad());

        item.setCantidad(request.cantidad());
        item.setActualizadoEn(LocalDateTime.now());

        cartItemRepository.save(item);

        return buildCartResponse(item.getCart());
    }

    @Override
    @Transactional
    public void removeItem(Long itemId) {
        CartItem item = cartItemRepository.findByIdAndCartUserEmail(itemId, SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Item de carrito no encontrado."));

        cartItemRepository.delete(item);
    }

    @Override
    @Transactional
    public void clearCart() {
        Cart cart = getOrCreateActiveCart();
        cartItemRepository.deleteByCartId(cart.getId());
        cart.setActualizadoEn(LocalDateTime.now());
    }

    private Cart getOrCreateActiveCart() {
        return cartRepository.findByUserEmailAndActivoTrue(SecurityUtil.getCurrentUserEmail())
                .orElseGet(this::createCartForTempUser);
    }

    private Cart createCartForTempUser() {
        User user = userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.USER_NOT_FOUND));

        Cart cart = Cart.builder()
                .user(user)
                .activo(true)
                .creadoEn(LocalDateTime.now())
                .build();

        return cartRepository.save(cart);
    }

    private CartResponse buildCartResponse(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCartIdOrderByIdAsc(cart.getId());
        return cartMapper.toResponse(cart, items);
    }

    private void validateStock(Product product, int quantity) {
        if (quantity > product.getStock()) {
            throw new BadRequestException(MessageError.STOCK_NOT_AVAILABLE);
        }
    }
}