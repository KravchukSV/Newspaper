package com.example.newspaper.controller;

import com.example.newspaper.entity.Article;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleController articleController;

    @Test
    void contextLoads() {
        assertThat(articleController);
    }


    @Test
    void findAll() throws Exception{
        this.mockMvc.perform(get("/articles")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {
        this.mockMvc
                .perform(get("/articles/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.titleEnglish").value("ARTICLE"))
                .andExpect(jsonPath("$.titleGerman").value("ARTICLE"))
                .andExpect(jsonPath("$.issnNumber").value(13))
                .andExpect(jsonPath("$.isbnNumber").value(12))
                .andExpect(jsonPath("$.yearPublication").value(2023))
                .andExpect(jsonPath("$.editionNumber").value(11))
                .andDo(print());
    }

    @Test
    void deleteById() throws Exception {
        this.mockMvc
                .perform(delete("/articles/{id}", 10) )
                .andExpect(status().isNoContent());
    }

    @Test
    void update() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Article article = new Article();
        article.setId(10);
        article.setTitleEnglish("English");
        article.setTitleGerman("German");
        article.setEditionNumber(1);
        article.setIsbnNumber(1);
        article.setIssnNumber(1);
        article.setYearPublication(2023);

        this.mockMvc
                .perform(put("/articles/{id}", 10)
                        .content(mapper.writeValueAsString(article))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.titleEnglish").value("English"))
                .andExpect(jsonPath("$.titleGerman").value("German"))
                .andExpect(jsonPath("$.issnNumber").value(1))
                .andExpect(jsonPath("$.isbnNumber").value(1))
                .andExpect(jsonPath("$.yearPublication").value(2023))
                .andExpect(jsonPath("$.editionNumber").value(1))
                .andDo(print());
    }

    @Test
    void create() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Article article = new Article();
        article.setTitleEnglish("English");
        article.setTitleGerman("German");
        article.setEditionNumber(1);
        article.setIsbnNumber(1);
        article.setIssnNumber(1);
        article.setYearPublication(2023);

        this.mockMvc
                .perform(post("/articles")
                        .content(mapper.writeValueAsString(article))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titleEnglish").value("English"))
                .andExpect(jsonPath("$.titleGerman").value("German"))
                .andExpect(jsonPath("$.issnNumber").value(1))
                .andExpect(jsonPath("$.isbnNumber").value(1))
                .andExpect(jsonPath("$.yearPublication").value(2023))
                .andExpect(jsonPath("$.editionNumber").value(1))
                .andDo(print());
    }

    @Test
    void findDuplicateArticles() throws Exception {

        this.mockMvc
                .perform(get("/articles/{id}/duplicates", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", isA(Collection.class)))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].titleEnglish").value("ARTICLE"))
                .andExpect(jsonPath("$[0].titleGerman").value("ARTICLE"))
                .andExpect(jsonPath("$[0].issnNumber").value(13))
                .andExpect(jsonPath("$[0].isbnNumber").value(12))
                .andExpect(jsonPath("$[0].yearPublication").value(2023))
                .andExpect(jsonPath("$[0].editionNumber").value(11))
                .andExpect(jsonPath("$[1].id").value(40))
                .andExpect(jsonPath("$[1].titleEnglish").value("ARTICLE"))
                .andExpect(jsonPath("$[1].titleGerman").value("ARTICLE_GER"))
                .andExpect(jsonPath("$[1].issnNumber").value(13))
                .andExpect(jsonPath("$[1].isbnNumber").value(12))
                .andExpect(jsonPath("$[1].yearPublication").value(2020))
                .andExpect(jsonPath("$[1].editionNumber").value(11))
                .andDo(print());
    }
}