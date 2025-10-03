package com.technomant.app.services;

import com.technomant.app.entity.Article;
import com.technomant.app.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final ArticleRepository articleRepository;

    public AdminService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Map<LocalDate, Long> getLastWeekStatistics() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        List<Article> articles = articleRepository.findByPublicationDateBetween(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );

        Map<LocalDate, Long> statistics = new LinkedHashMap<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = endDate.minusDays(i);
            statistics.put(date, 0L);
        }

        for (Article article : articles) {
            LocalDate articleDate = article.getPublicationDate().toLocalDate();
            statistics.computeIfPresent(articleDate, (date, count) -> count + 1);
        }

        return statistics;
    }
}
