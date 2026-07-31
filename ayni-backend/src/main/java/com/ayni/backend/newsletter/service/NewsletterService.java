package com.ayni.backend.newsletter.service;

import com.ayni.backend.newsletter.dto.NewsletterSubscribeRequest;
import com.ayni.backend.newsletter.dto.NewsletterSubscriberResponse;

public interface NewsletterService {

    NewsletterSubscriberResponse subscribe(NewsletterSubscribeRequest request);
}