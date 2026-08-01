package com.ayni.backend.newsletter.service;

import com.ayni.backend.common.exception.BusinessException;
import com.ayni.backend.common.util.GlobalUtil;
import com.ayni.backend.newsletter.dto.NewsletterSubscribeRequest;
import com.ayni.backend.newsletter.dto.NewsletterSubscriberResponse;
import com.ayni.backend.newsletter.entity.NewsletterSubscriber;
import com.ayni.backend.newsletter.mapper.NewsletterMapper;
import com.ayni.backend.newsletter.repository.NewsletterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import com.ayni.backend.notification.service.EmailService;

@Service
@RequiredArgsConstructor
public class NewsletterServiceImpl implements NewsletterService {

    private final NewsletterRepository newsletterRepository;
    private final NewsletterMapper newsletterMapper;
    private final EmailService emailService;

    @Override
    public NewsletterSubscriberResponse subscribe(NewsletterSubscribeRequest request) {
        String email = GlobalUtil.normalizeEmail(request.email());

        if (newsletterRepository.existsByEmail(email)) {
            throw new BusinessException("Este correo ya está suscrito al newsletter.");
        }

        NewsletterSubscriber subscriber = NewsletterSubscriber.builder()
                .email(email)
                .nombres(request.nombres())
                .activo(true)
                .creadoEn(LocalDateTime.now())
                .build();

        NewsletterSubscriber savedSubscriber = newsletterRepository.save(subscriber);

        emailService.sendWelcomeNewsletter(savedSubscriber.getEmail());

        return newsletterMapper.toResponse(savedSubscriber);
    }
}