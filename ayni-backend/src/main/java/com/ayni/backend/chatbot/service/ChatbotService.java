package com.ayni.backend.chatbot.service;

import com.ayni.backend.chatbot.dto.*;
import com.ayni.backend.product.entity.Product; // Asegúrate de que coincida con tu paquete de entidades de producto
import com.ayni.backend.product.repository.ProductRepository; // Tu repositorio actual
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.api.model}")
    private String apiModel;

    private final ProductRepository productRepository; // Inyectamos tu repositorio de productos
    private final RestClient restClient = RestClient.create();

    public ChatResponse askBot(ChatRequest request) {
        // 1. Obtenemos los productos actuales directamente desde tu base de datos PostgreSQL
        List<Product> productosDb = productRepository.findAll();

        // 2. Transformamos la lista de productos en un texto claro para que la IA los lea (usando los nombres en español)
        String catalogoInfo = productosDb.stream()
                .map(p -> "- " + p.getNombre() + " (Precio: $" + p.getPrecio() + ")")
                .collect(Collectors.joining("\n"));

        // 3. Diseñamos el System Prompt dinámico con tu inventario real
        String systemPrompt = """
                Eres el asistente virtual oficial de Ayni, una plataforma de comercio electrónico.
                Tu tono debe ser amable, profesional y conciso.
                Ayuda a los clientes basándote ÚNICAMENTE en el siguiente catálogo actual de productos de nuestra base de datos:
                %s
                
                Si un cliente pregunta por un producto que no está en esta lista, indícale amablemente que no lo tenemos disponible. No inventes precios ni productos.
                """.formatted(catalogoInfo);

        // 4. Construimos el historial de mensajes
        List<Message> messages = List.of(
                new Message("system", systemPrompt),
                new Message("user", request.prompt())
        );

        // 5. Preparamos el payload para Groq
        GroqRequest groqPayload = new GroqRequest(apiModel, messages);

        // 6. Consumimos la API de Groq jeje
        GroqResponse response = restClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(groqPayload)
                .retrieve()
                .body(GroqResponse.class);

        if (response != null && response.choices() != null && !response.choices().isEmpty()) {
            String botReply = response.choices().get(0).message().content();
            return new ChatResponse(botReply);
        }

        return new ChatResponse("Lo siento, en este momento tengo problemas para procesar tu consulta.");
    }
}