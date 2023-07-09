package com.example.newspaper.controller;

import com.example.newspaper.entity.Article;
import com.example.newspaper.entity.Site;
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
import java.util.HashSet;
import java.util.Set;

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
class SiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SiteController siteController;


    @Test
    void contextLoads() {
        assertThat(siteController);
    }


    @Test
    void findAll() throws Exception{
        this.mockMvc.perform(get("/sites")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {
        this.mockMvc
                .perform(get("/sites/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.weblink").value("SITE"))
                .andDo(print());
    }

    @Test
    void deleteById() throws Exception {
        this.mockMvc
                .perform(delete("/sites/{id}", 10) )
                .andExpect(status().isNoContent());
    }

    @Test
    void update() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Site site = new Site();
        site.setId(1);
        site.setWeblink("SITE UPDATE");


        this.mockMvc
                .perform(put("/sites/{id}", 1)
                        .content(mapper.writeValueAsString(site))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.weblink").value("SITE UPDATE"))
                .andDo(print());
    }

    @Test
    void create() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Site site = new Site();
        site.setWeblink("NEW SITE");

        this.mockMvc
                .perform(post("/sites")
                        .content(mapper.writeValueAsString(site))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.weblink").value("NEW SITE"))
                .andDo(print());
    }

    @Test
    void allArticlesSite() throws Exception {
        this.mockMvc
                .perform(get("/sites/{id}/articles", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", isA(Collection.class)))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].titleEnglish").value("ARTICLE"))
                .andExpect(jsonPath("$[0].titleGerman").value("ARTICLE"))
                .andExpect(jsonPath("$[0].issnNumber").value(13))
                .andExpect(jsonPath("$[0].isbnNumber").value(12))
                .andExpect(jsonPath("$[0].yearPublication").value(2023))
                .andExpect(jsonPath("$[0].editionNumber").value(11))
                .andDo(print());
    }

    @Test
    void updateListArticle() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Set<Article> articleSet = new HashSet<>();

        Article article = new Article();
        article.setTitleEnglish("English Title");
        article.setTitleGerman("German Title");
        article.setEditionNumber(23);
        article.setIsbnNumber(1132);
        article.setIssnNumber(1241);
        article.setYearPublication(2023);

        articleSet.add(article);

        this.mockMvc
                .perform(put("/sites/{id}/update-articles", 10)
                        .content(mapper.writeValueAsString(articleSet))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }
}