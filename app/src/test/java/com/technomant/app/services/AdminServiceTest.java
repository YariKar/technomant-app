package com.technomant.app.services;

import com.technomant.app.entity.Article;
import com.technomant.app.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void shouldReturnLastWeekStatistics() {
        LocalDateTime now = LocalDateTime.now();
        List<Article> articles = Arrays.asList(
                createArticle("Article 1", now),
                createArticle("Article 2", now.minusDays(1)),
                createArticle("Article 3", now.minusDays(1)),
                createArticle("Article 4", now.minusDays(2))
        );

        when(articleRepository.findByPublicationDateBetween(any(), any()))
                .thenReturn(articles);

        Map<LocalDate, Long> statistics = adminService.getLastWeekStatistics();

        assertThat(statistics).isNotNull();
        assertThat(statistics.values().stream().mapToLong(Long::longValue).sum())
                .isEqualTo(4L); // Всего 4 статьи
    }

    private Article createArticle(String title, LocalDateTime date) {
        Article article = new Article();
        article.setTitle(title);
        article.setAuthor("Author");
        article.setContent("Content");
        article.setPublicationDate(date);
        return article;
    }
}