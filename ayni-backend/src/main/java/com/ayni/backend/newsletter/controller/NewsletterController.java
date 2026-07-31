package com.ayni.backend.newsletter.controller;

import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import com.ayni.backend.newsletter.dto.NewsletterSubscribeRequest;
import com.ayni.backend.newsletter.dto.NewsletterSubscriberResponse;
import com.ayni.backend.newsletter.service.NewsletterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NewsletterController {

    private final NewsletterService newsletterService;

    @PostMapping(ApiRoutes.NEWSLETTER + "/subscribe")
    public ApiResponse<NewsletterSubscriberResponse> subscribe(
            @Valid @RequestBody NewsletterSubscribeRequest request
    ) {
        return ApiResponse.ok(
                "Suscripción registrada correctamente.",
                newsletterService.subscribe(request)
        );
    }
}