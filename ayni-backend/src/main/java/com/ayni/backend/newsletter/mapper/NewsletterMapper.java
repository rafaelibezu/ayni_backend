package com.ayni.backend.newsletter.mapper;

import com.ayni.backend.newsletter.dto.NewsletterSubscriberResponse;
import com.ayni.backend.newsletter.entity.NewsletterSubscriber;
import org.springframework.stereotype.Component;

@Component
public class NewsletterMapper {

    public NewsletterSubscriberResponse toResponse(NewsletterSubscriber subscriber) {
        return new NewsletterSubscriberResponse(
                subscriber.getId(),
                subscriber.getEmail(),
                subscriber.getNombres(),
                subscriber.getActivo(),
                subscriber.getCreadoEn()
        );
    }
}