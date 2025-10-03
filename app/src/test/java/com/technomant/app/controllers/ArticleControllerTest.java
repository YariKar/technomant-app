package com.technomant.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technomant.app.configs.SecurityConfig;
import com.technomant.app.dto.ArticleDTO;
import com.technomant.app.entity.Article;
import com.technomant.app.services.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
@Import(SecurityConfig.class)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArticleService articleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateArticle() throws Exception {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTitle("Test Title");
        articleDTO.setAuthor("Test Author");
        articleDTO.setContent("Test Content");
        articleDTO.setPublicationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        Article savedArticle = new Article();
        savedArticle.setId(1L);
        savedArticle.setTitle("Test Title");
        savedArticle.setAuthor("Test Author");
        savedArticle.setContent("Test Content");

        when(articleService.save(any(ArticleDTO.class))).thenReturn(savedArticle);

        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void shouldGetArticles() throws Exception {
        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk());
    }
}