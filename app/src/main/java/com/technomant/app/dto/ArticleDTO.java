package com.technomant.app.dto;

import com.technomant.app.entity.Article;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleDTO {

    @JsonProperty("id") // Используем JsonProperty вместо SerializedName
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("author")
    private String author;

    @JsonProperty("publicationDate")
    private LocalDateTime publicationDate;

    // Метод для преобразования в Entity
    public Article toEntity() {
        Article article = new Article();
        article.setTitle(this.title);
        article.setAuthor(this.author);
        article.setContent(this.content);
        article.setPublicationDate(this.publicationDate);
        return article;
    }
}
