package com.technomant.app.integration;

import com.technomant.app.AppApplication;
import com.technomant.app.dto.ArticleDTO;
import com.technomant.app.entity.Article;
import com.technomant.app.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = AppApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.security.user.name=admin",
        "spring.security.user.password=admin123",
        "spring.security.user.roles=ADMIN"
})
class ArticleIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ArticleRepository articleRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        articleRepository.deleteAll();
    }

    @Test
    void shouldCreateArticle() {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTitle("Test Article");
        articleDTO.setAuthor("Test Author");
        articleDTO.setContent("Test Content");
        articleDTO.setPublicationDate(LocalDateTime.now());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ArticleDTO> request = new HttpEntity<>(articleDTO, headers);

        ResponseEntity<Article> response = restTemplate.postForEntity(
                baseUrl + "/api/articles", request, Article.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Test Article");
        assertThat(response.getBody().getAuthor()).isEqualTo("Test Author");
    }

    @Test
    void shouldGetArticlesWithPagination() {
        createTestArticles();

        ResponseEntity<String> response1 = restTemplate.getForEntity(
                baseUrl + "/api/articles", String.class);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> response2 = restTemplate.getForEntity(
                baseUrl + "/api/articles?page=0&size=3", String.class);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> response3 = restTemplate.getForEntity(
                baseUrl + "/api/articles?page=1&size=3", String.class);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldGetStatisticsWithAdminAuth() {
        createTestArticlesWithDifferentDates();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin123");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/admin/stats",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldDenyAccessToStatsWithoutAuth() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/api/admin/stats", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private void createTestArticles() {
        for (int i = 1; i <= 5; i++) {
            Article article = new Article();
            article.setTitle("Article " + i);
            article.setAuthor("Author " + i);
            article.setContent("Content " + i);
            article.setPublicationDate(LocalDateTime.now());
            articleRepository.save(article);
        }
    }

    private void createTestArticlesWithDifferentDates() {
        LocalDateTime now = LocalDateTime.now();

        articleRepository.save(createArticle("Article 1", now));
        articleRepository.save(createArticle("Article 2", now));

        articleRepository.save(createArticle("Article 3", now.minusDays(1)));
        articleRepository.save(createArticle("Article 4", now.minusDays(1)));

        articleRepository.save(createArticle("Article 5", now.minusDays(2)));
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