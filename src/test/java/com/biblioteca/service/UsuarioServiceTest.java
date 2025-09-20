package com.biblioteca.service;

import com.biblioteca.dto.LoginDTO;
import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.exception.UnauthorizedException;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // carrega o contexto completo (Spring, Beans, Repositories)
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        // limpa o banco em memória H2 antes de cada teste
        usuarioRepository.deleteAll();
    }

    @Test
    void deveCadastrarUsuarioComSenhaCriptografada() {
        UsuarioDTO dto = new UsuarioDTO("Maria", "maria@email.com", "123456");
        Usuario usuario = usuarioService.cadastrar(dto);

        assertNotNull(usuario.getId());
        assertEquals("Maria", usuario.getNome());
        assertTrue(encoder.matches("123456", usuario.getSenha())); // verifica hash
    }

    @Test
    void naoDeveCadastrarUsuarioComEmailDuplicado() {
        UsuarioDTO dto1 = new UsuarioDTO("João", "joao@email.com", "123");
        UsuarioDTO dto2 = new UsuarioDTO("Outro João", "joao@email.com", "456");

        usuarioService.cadastrar(dto1);

        // como a chave do email é única no banco, deve lançar exceção
        assertThrows(DataIntegrityViolationException.class, () -> {
            usuarioService.cadastrar(dto2);
        });
    }

    @Test
    void deveLogarComCredenciaisCorretas() {
        UsuarioDTO dto = new UsuarioDTO("Pedro", "pedro@email.com", "senha");
        usuarioService.cadastrar(dto);

        LoginDTO login = new LoginDTO("pedro@email.com", "senha");
        Usuario usuarioLogado = usuarioService.login(login);

        assertNotNull(usuarioLogado);
        assertEquals("Pedro", usuarioLogado.getNome());
    }

    @Test
    void naoDeveLogarComSenhaErrada() {
        UsuarioDTO dto = new UsuarioDTO("Ana", "ana@email.com", "correta");
        usuarioService.cadastrar(dto);

        LoginDTO login = new LoginDTO("ana@email.com", "errada");

        assertThrows(UnauthorizedException.class, () -> {
            usuarioService.login(login);
        });
    }

    @Test
    void deveAtualizarNomeDoUsuario() {
        UsuarioDTO dto = new UsuarioDTO("Carlos", "carlos@email.com", "abc");
        Usuario usuario = usuarioService.cadastrar(dto);

        usuario.setNome("Carlos Silva");
        usuarioRepository.save(usuario);

        Optional<Usuario> usuarioAtualizado = usuarioRepository.findById(usuario.getId());
        assertTrue(usuarioAtualizado.isPresent());
        assertEquals("Carlos Silva", usuarioAtualizado.get().getNome());
    }

    @Test
    void deveRedefinirSenha() {
        UsuarioDTO dto = new UsuarioDTO("Julia", "julia@email.com", "minhasenha");
        Usuario usuario = usuarioService.cadastrar(dto);

        usuario.setSenha(encoder.encode("novasenha"));
        usuarioRepository.save(usuario);

        Usuario atualizado = usuarioRepository.findById(usuario.getId()).get();
        assertTrue(encoder.matches("novasenha", atualizado.getSenha()));
    }
}
