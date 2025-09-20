package com.biblioteca.service;

import com.biblioteca.model.Livro;
import com.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LivroServiceTest {

    @Autowired
    private LivroService livroService;

    @Autowired
    private LivroRepository livroRepository;

    @BeforeEach
    void setUp() {
        livroRepository.deleteAll(); // limpa H2 antes de cada teste
    }

    @Test
    void deveCadastrarLivro() {
        Livro livro = new Livro("Clean Code", "Robert C. Martin");
        Livro salvo = livroService.cadastrar(livro);

        assertNotNull(salvo.getId());
        assertEquals("Clean Code", salvo.getTitulo());
        assertEquals("Robert C. Martin", salvo.getAutor());
    }

    @Test
    void deveListarLivros() {
        livroService.cadastrar(new Livro("Livro 1", "Autor A"));
        livroService.cadastrar(new Livro("Livro 2", "Autor B"));

        List<Livro> livros = livroService.listar();
        assertEquals(2, livros.size());
    }

    @Test
    void deveBuscarLivroPorId() {
        Livro salvo = livroService.cadastrar(new Livro("Domain-Driven Design", "Eric Evans"));

        Optional<Livro> encontrado = Optional.ofNullable(livroService.buscarPorId(salvo.getId()));
        assertTrue(encontrado.isPresent());
        assertEquals("Domain-Driven Design", encontrado.get().getTitulo());
    }
}
