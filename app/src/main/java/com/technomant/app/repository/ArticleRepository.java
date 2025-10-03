package com.technomant.app.repository;

import com.technomant.app.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long>{
}