package com.ayni.backend.notification.service;

import com.ayni.backend.notification.dto.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Override
    public void send(EmailMessage message) {
        log.info("""
                
                ================= EMAIL SIMULADO =================
                Para: {}
                Asunto: {}
                
                {}
                =================================================
                """,
                message.to(),
                message.subject(),
                message.body()
        );
    }

    @Override
    public void sendWelcomeNewsletter(String email) {
        send(new EmailMessage(
                email,
                "Bienvenido a las novedades de Ayni",
                """
                Gracias por suscribirte a Ayni.
                
                Pronto recibirás novedades, promociones y nuevos productos.
                """
        ));
    }

    @Override
    public void sendOrderCreated(String email, String codigoPedido, String total) {
        send(new EmailMessage(
                email,
                "Tu pedido fue creado en Ayni",
                """
                Hola,
                
                Tu pedido %s fue creado correctamente.
                Total a pagar: S/ %s
                
                Recuerda registrar tu pago Yape para que podamos validarlo.
                """.formatted(codigoPedido, total)
        ));
    }

    @Override
    public void sendYapePaymentReceived(String email, String codigoPedido) {
        send(new EmailMessage(
                email,
                "Recibimos tu comprobante Yape",
                """
                Hola,
                
                Recibimos el comprobante de pago de tu pedido %s.
                Nuestro equipo lo revisará y validará pronto.
                """.formatted(codigoPedido)
        ));
    }

    @Override
    public void sendPaymentValidated(String email, String codigoPedido) {
        send(new EmailMessage(
                email,
                "Tu pago fue validado",
                """
                Hola,
                
                Tu pago del pedido %s fue validado correctamente.
                Tu pedido pasará a preparación.
                
                Gracias por comprar en Ayni.
                """.formatted(codigoPedido)
        ));
    }
}