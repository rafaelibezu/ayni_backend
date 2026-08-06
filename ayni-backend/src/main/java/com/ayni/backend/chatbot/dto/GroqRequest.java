package com.ayni.backend.chatbot.dto;

import java.util.List;

public record GroqRequest(String model, List<Message> messages) {}