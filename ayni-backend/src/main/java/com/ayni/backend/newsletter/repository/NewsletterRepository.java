package com.ayni.backend.newsletter.repository;

import com.ayni.backend.newsletter.entity.NewsletterSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsletterRepository extends JpaRepository<NewsletterSubscriber, Long> {

    boolean existsByEmail(String email);

    Optional<NewsletterSubscriber> findByEmail(String email);
}