package com.technomant.app.services;

import com.technomant.app.dto.ArticleDTO;
import com.technomant.app.entity.Article;
import com.technomant.app.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    @Test
    void shouldSaveArticleSuccessfully() {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTitle("Test Title");
        articleDTO.setAuthor("Test Author");
        articleDTO.setContent("Test Content");
        articleDTO.setPublicationDate(LocalDateTime.now());

        Article savedArticle = new Article();
        savedArticle.setId(1L);
        savedArticle.setTitle("Test Title");
        savedArticle.setAuthor("Test Author");
        savedArticle.setContent("Test Content");

        when(articleRepository.save(any(Article.class))).thenReturn(savedArticle);

        Article result = articleService.save(articleDTO);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Title");
    }

    @Test
    void shouldThrowExceptionWhenTitleIsTooLong() {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTitle("a".repeat(101));
        articleDTO.setAuthor("Test Author");
        articleDTO.setContent("Test Content");
        articleDTO.setPublicationDate(LocalDateTime.now());

        assertThatThrownBy(() -> articleService.save(articleDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("100");
    }
}