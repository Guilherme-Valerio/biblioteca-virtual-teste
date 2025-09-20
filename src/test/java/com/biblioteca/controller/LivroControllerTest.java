package com.biblioteca.controller;

import com.biblioteca.model.Livro;
import com.biblioteca.repository.LivroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        livroRepository.deleteAll(); // limpar H2 antes de cada teste
    }

    @Test
    void deveCadastrarLivro() throws Exception {
        Livro livro = new Livro("Refactoring", "Martin Fowler");

        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Refactoring"))
                .andExpect(jsonPath("$.autor").value("Martin Fowler"));
    }

    @Test
    void deveListarLivros() throws Exception {
        livroRepository.save(new Livro("Livro Teste 1", "Autor A"));
        livroRepository.save(new Livro("Livro Teste 2", "Autor B"));

        mockMvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deveBuscarLivroPorId() throws Exception {
        Livro salvo = livroRepository.save(new Livro("Patterns of Enterprise Application Architecture", "Martin Fowler"));

        mockMvc.perform(get("/api/livros/" + salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Patterns of Enterprise Application Architecture"))
                .andExpect(jsonPath("$.autor").value("Martin Fowler"));
    }
}
