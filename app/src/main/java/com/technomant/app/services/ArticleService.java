package com.technomant.app.services;

import com.technomant.app.dto.ArticleDTO;
import com.technomant.app.entity.Article;
import com.technomant.app.repository.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
    public Article save(ArticleDTO articleDTO) {
        validateArticle(articleDTO);
        Article article = articleDTO.toEntity();
        return articleRepository.save(article);
    }

    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    private void validateArticle(ArticleDTO articleDTO) throws IllegalArgumentException {
        if (articleDTO.getTitle() == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        if (articleDTO.getAuthor() == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        if (articleDTO.getContent() == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        if (articleDTO.getPublicationDate() == null) {
            throw new IllegalArgumentException("Publication date cannot be null");
        }

        if (articleDTO.getTitle().length() > 100) {
            throw new IllegalArgumentException("Title cannot exceed 100 characters");
        }

        if (articleDTO.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (articleDTO.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        if (articleDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
    }

}
