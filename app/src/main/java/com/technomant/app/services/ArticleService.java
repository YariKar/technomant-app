package com.technomant.app.services;

import com.technomant.app.dto.ArticleDTO;
import com.technomant.app.entity.Article;
import com.technomant.app.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

    private void validateArticle(ArticleDTO articleDTO) {

    }
}
