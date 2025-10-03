package com.technomant.app.controllers;

import com.technomant.app.dto.ArticleDTO;
import com.technomant.app.entity.Article;
import com.technomant.app.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    public ArticleController(ArticleService articleService){}

    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody ArticleDTO article) {
        try {
            Article savedArticle = articleService.save(article);
            return ResponseEntity.ok(savedArticle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<Article>> getArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size){

        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articles = articleService.findAll(pageable);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        Optional<Article> article = articleService.findById(id);
        return article.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        try {
            articleService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("test")
    public String getArticles() {
        return "test endpoint";
    }
}
