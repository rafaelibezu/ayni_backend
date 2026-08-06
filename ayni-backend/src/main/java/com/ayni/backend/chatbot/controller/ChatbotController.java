package com.ayni.backend.chatbot.controller;

import com.ayni.backend.chatbot.dto.ChatRequest;
import com.ayni.backend.chatbot.dto.ChatResponse;
import com.ayni.backend.chatbot.service.ChatbotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
@Tag(name = "Chatbot AI", description = "Endpoints para la interacción con el asistente virtual de Ayni")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> askQuestion(@RequestBody ChatRequest request) {
        ChatResponse response = chatbotService.askBot(request);
        return ResponseEntity.ok(response);
    }
}