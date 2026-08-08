package com.ayni.backend.notification.service;

import com.ayni.backend.notification.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${app.mail.from}")
    private String from;

    @Value("${app.mail.enabled:true}")
    private boolean mailEnabled;

    @Override
    public void send(EmailMessage message) {
        log.info("MAIL ENABLED: {}", mailEnabled);
        log.info("MAIL FROM: {}", from);

        if (!mailEnabled) {
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
            return;
        }

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    true,
                    "UTF-8"
            );

            helper.setFrom(from, "Ayni Tienda Perú");
            helper.setTo(message.to());
            helper.setSubject(message.subject());
            helper.setText(message.body(), true);

            javaMailSender.send(mimeMessage);

            log.info("Email enviado correctamente a {}", message.to());

        } catch (MailException | MessagingException | java.io.UnsupportedEncodingException exception) {
            log.error("Error enviando email a {}: {}", message.to(), exception.getMessage());
        }
    }

    @Override
    public void sendWelcomeNewsletter(String email) {
        send(new EmailMessage(
                email,
                "¡Bienvenido a las novedades de Ayni!",
                """
                <div style="margin:0; padding:0; background:#111113; font-family:Arial, Helvetica, sans-serif;">
                    <div style="max-width:680px; margin:0 auto; background:#151517; color:#ffffff; padding:36px 32px; border-radius:14px;">
                        
                        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:30px;">
                            <div>
                                <h1 style="font-size:34px; line-height:1.15; margin:0; color:#ffffff;">
                                    ¡Bienvenido a las<br>novedades de Ayni!
                                </h1>
                                <div style="width:70px; height:5px; background:#e33434; margin-top:18px; border-radius:4px;"></div>
                            </div>
                            <div style="text-align:right;">
                                <div style="font-size:30px; font-weight:800; color:#ffffff; letter-spacing:1px;">
                                    AYNI<span style="color:#e33434;">.PE</span>
                                </div>
                                <div style="font-size:13px; color:#bbbbbb;">Tienda online</div>
                            </div>
                        </div>
    
                        <div style="margin-top:34px;">
                            <h2 style="font-size:22px; margin:0 0 12px 0; color:#ffffff;">¡Hola!</h2>
                            
                            <p style="font-size:17px; line-height:1.7; color:#eeeeee; margin:0 0 22px 0;">
                                Gracias por suscribirte a <strong style="color:#e33434;">Ayni</strong>.
                            </p>
    
                            <p style="font-size:17px; line-height:1.7; color:#eeeeee; margin:0 0 22px 0;">
                                A partir de ahora, serás de los primeros en enterarte de nuestras novedades,
                                promociones exclusivas y nuevos productos.
                            </p>
    
                            <p style="font-size:17px; line-height:1.7; color:#eeeeee; margin:0 0 22px 0;">
                                Nos alegra tenerte en nuestra comunidad.
                            </p>
    
                            <p style="font-size:20px; font-weight:bold; color:#ffffff; margin:0 0 28px 0;">
                                ¡Gracias por ser parte de Ayni! <span style="color:#e33434;">❤</span>
                            </p>
                        </div>
    
                        <hr style="border:none; border-top:1px solid #333; margin:32px 0;">
    
                        <div style="display:flex; gap:14px; justify-content:space-between; margin-bottom:30px;">
                            <div style="width:33%; text-align:center;">
                                <div style="width:54px; height:54px; line-height:54px; margin:0 auto 10px auto; background:#e33434; border-radius:50%; font-size:24px;">
                                    🏷️
                                </div>
                                <div style="font-size:15px; color:#ffffff;">Promociones<br>exclusivas</div>
                            </div>
    
                            <div style="width:33%; text-align:center; border-left:1px solid #333; border-right:1px solid #333;">
                                <div style="width:54px; height:54px; line-height:54px; margin:0 auto 10px auto; background:#e33434; border-radius:50%; font-size:24px;">
                                    🛍️
                                </div>
                                <div style="font-size:15px; color:#ffffff;">Nuevos<br>productos</div>
                            </div>
    
                            <div style="width:33%; text-align:center;">
                                <div style="width:54px; height:54px; line-height:54px; margin:0 auto 10px auto; background:#e33434; border-radius:50%; font-size:24px;">
                                    🔔
                                </div>
                                <div style="font-size:15px; color:#ffffff;">Novedades<br>antes que todos</div>
                            </div>
                        </div>
    
                        <div style="border:1px solid #333; border-radius:12px; padding:22px; margin-top:26px; background:#18181b;">
                            <table style="width:100%; border-collapse:collapse;">
                                <tr>
                                    <td style="width:72px; vertical-align:middle;">
                                        <div style="font-size:42px; color:#e33434;">✉️</div>
                                    </td>
                                    <td style="vertical-align:middle;">
                                        <div style="font-size:19px; font-weight:bold; color:#ffffff; margin-bottom:6px;">
                                            ¿Tienes preguntas o sugerencias?
                                        </div>
                                        <div style="font-size:15px; color:#dddddd;">
                                            Estamos aquí para ayudarte.
                                        </div>
                                        <div style="font-size:16px; color:#e33434; margin-top:6px;">
                                            ayni.tiendaperu@gmail.com
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
    
                        <div style="text-align:center; margin-top:34px;">
                            <p style="font-size:18px; font-weight:bold; color:#ffffff; margin:0 0 6px 0;">
                                ¡Gracias por elegir Ayni!
                            </p>
                            <p style="font-size:15px; color:#cccccc; margin:0 0 18px 0;">
                                Seguimos trabajando para brindarte lo mejor.
                            </p>
                            <div style="font-size:28px; color:#e33434;">♡</div>
                        </div>
    
                        <div style="margin-top:34px; padding-top:22px; border-top:1px solid #333; display:flex; justify-content:space-between; color:#aaaaaa; font-size:13px;">
                            <div>
                                Instagram · Facebook · TikTok
                            </div>
                            <div>
                                www.ayni.pe
                            </div>
                        </div>
                    </div>
                </div>
                """
        ));
    }

    @Override
    public void sendOrderCreated(String email, String codigoPedido, String total) {
        send(new EmailMessage(
                email,
                "Tu pedido fue creado en Ayni",
                """
                <div style="font-family: Arial, sans-serif; background-color:#f7f7f7; padding:24px;">
                    <div style="max-width:600px; margin:auto; background:#ffffff; border-radius:12px; padding:28px; border:1px solid #eeeeee;">
                        
                        <h1 style="color:#222;">Ayni Tienda Perú</h1>
                        <p style="color:#666;">Gracias por comprar con nosotros.</p>
                        
                        <hr style="border:none; border-top:1px solid #eeeeee; margin:24px 0;">
                        
                        <h2 style="color:#222;">Tu pedido fue creado correctamente 🛍️</h2>
                        
                        <p style="color:#444; font-size:16px;">
                            Hemos registrado tu pedido. Ahora solo falta que registres tu pago Yape para poder validarlo.
                        </p>
                        
                        <div style="background:#f3f4f6; padding:18px; border-radius:10px; margin:22px 0;">
                            <p style="margin:0 0 8px 0; color:#333;"><strong>Número de pedido:</strong> %s</p>
                            <p style="margin:0 0 8px 0; color:#333;"><strong>Total a pagar:</strong> S/ %s</p>
                            <p style="margin:0; color:#333;"><strong>Estado:</strong> Pendiente de pago</p>
                        </div>
                        
                        <p style="color:#444; font-size:15px; line-height:1.6;">
                            Recuerda subir tu comprobante Yape desde la sección de pedidos para que nuestro equipo pueda revisarlo.
                        </p>
                        
                        <div style="margin:28px 0;">
                            <a href="http://localhost:4200"
                               style="background:#111827; color:#ffffff; text-decoration:none; padding:12px 22px; border-radius:8px; font-weight:bold; display:inline-block;">
                                Ver mi pedido
                            </a>
                        </div>
                        
                        <hr style="border:none; border-top:1px solid #eeeeee; margin:24px 0;">
                        
                        <p style="color:#999; font-size:12px; text-align:center;">
                            © Ayni Tienda Perú. Gracias por confiar en nosotros.
                        </p>
                    </div>
                </div>
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