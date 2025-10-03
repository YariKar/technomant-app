package com.technomant.app.repository;

import com.technomant.app.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>{

    List<Article> findByPublicationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}