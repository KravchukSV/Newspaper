package com.example.newspaper.controller;

import com.example.newspaper.entity.Author;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorController authorController;

    @Test
    void contextLoads() {
        assertThat(authorController);
    }


    @Test
    void findAll() throws Exception{
        this.mockMvc.perform(get("/authors")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {
        this.mockMvc
                .perform(get("/authors/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.fullName").value("AUTHOR"))
                .andDo(print());
    }

    @Test
    void deleteById() throws Exception {
        this.mockMvc
                .perform(delete("/authors/{id}", 10) )
                .andExpect(status().isNoContent());
    }

    @Test
    void update() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Author author = new Author();
        author.setId(10);
        author.setFullName("SECOND AUTHOR");


        this.mockMvc
                .perform(put("/authors/{id}", 10)
                        .content(mapper.writeValueAsString(author))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.fullName").value("SECOND AUTHOR"))
                .andDo(print());
    }

    @Test
    void create() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Author author = new Author();
        author.setFullName("NEW AUTHOR");

        this.mockMvc
                .perform(post("/authors")
                        .content(mapper.writeValueAsString(author))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1 ))
                .andExpect(jsonPath("$.fullName").value("NEW AUTHOR"))
                .andDo(print());
    }
}