package com.ayni.backend.notification.service;

import com.ayni.backend.notification.dto.EmailMessage;

public interface EmailService {

    void send(EmailMessage message);

    void sendWelcomeNewsletter(String email);

    void sendOrderCreated(String email, String codigoPedido, String total);

    void sendYapePaymentReceived(String email, String codigoPedido);

    void sendPaymentValidated(String email, String codigoPedido);
}