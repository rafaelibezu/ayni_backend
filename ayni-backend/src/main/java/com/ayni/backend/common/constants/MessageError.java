package com.ayni.backend.common.constants;

public final class MessageError {

    private MessageError() {
    }

    public static final String INTERNAL_ERROR = "Ocurrió un error interno en el servidor.";
    public static final String BAD_REQUEST = "Solicitud inválida.";
    public static final String RESOURCE_NOT_FOUND = "Recurso no encontrado.";

    public static final String EMAIL_ALREADY_EXISTS = "El correo ya se encuentra registrado.";
    public static final String INVALID_CREDENTIALS = "Credenciales inválidas.";

    public static final String ROLE_NOT_FOUND = "Rol no encontrado.";
    public static final String USER_NOT_FOUND = "Usuario no encontrado.";
    public static final String CATEGORY_NOT_FOUND = "Categoría no encontrada.";
    public static final String PRODUCT_NOT_FOUND = "Producto no encontrado.";

    public static final String STOCK_NOT_AVAILABLE = "Stock insuficiente.";
    public static final String CART_EMPTY = "El carrito está vacío.";

    public static final String PAYMENT_ALREADY_REGISTERED = "Este pedido ya tiene un pago registrado.";
    public static final String ORDER_NOT_FOUND = "Pedido no encontrado.";
    public static final String NEWSLETTER_ALREADY_EXISTS = "Este correo ya está suscrito al newsletter.";
    public static final String CART_ITEM_NOT_FOUND = "Item de carrito no encontrado.";
    public static final String FAVORITE_ALREADY_EXISTS = "El producto ya está en favoritos.";
    public static final String FAVORITE_NOT_FOUND = "El producto no está en favoritos.";
}