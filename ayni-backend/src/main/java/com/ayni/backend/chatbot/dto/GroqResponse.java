package com.ayni.backend.chatbot.dto;

import java.util.List;

public record GroqResponse(List<GroqChoice> choices) {}