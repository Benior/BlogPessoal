package com.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.blogpessoal.model.Usuario;

//indicando que a classe é uma classe de teste em qualquer porta a cada teste realizado
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//cria um novo ambiente para testar a cada teste, criando um ciclo de vida para cada teste(será executado e resetado após o uso).
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository repository;

	//antes de tudo é preciso ter dados no banco para testar
	@BeforeAll
	void start() {
		
		repository.save(new Usuario(0L, "Michael Silva", "isadora@gmail.com","51 e pinga","https://i.imgur.com/FETvs2O.jpg", "tipo 1"));
		
		repository.save(new Usuario(0L, "Mayara Romero", "michaeltrimundial@gmail.com","nunca fui rebaixado","https://i.imgur.com/FETvs2O.jpg", "tipo 1"));
		
		repository.save(new Usuario(0L, "Brocco Silva", "broco@hotmail.com","broccolis","https://i.imgur.com/FETvs2O.jpg", "tipo 1"));
		
		repository.save(new Usuario(0L, "Maiar Silva", "will31smith@gmail.com","cenoura","https://i.imgur.com/FETvs2O.jpg", "tipo 1"));
	}

	@Test
	@DisplayName("teste para retornar 1 usuario")
	public void retornaUmUsuario() {
		//Detalho o que eu quero encontrar com esse teste
		Optional<Usuario> usuario = repository.findByUsuario("isadora@gmail.com");
		//Me confirma se o que eu pedi realmente veio
		assertTrue(usuario.get().getUsuario().equals("isadora@gmail.com"));
		
	}
	@Test
	@DisplayName("teste para retornar 3 usuarios")
	public void retornaTresUsuario() {
		//Detalho o que eu quero encontrar com esse teste
		List<Usuario> usuario = repository.findAllByNomeContainingIgnoreCase("Silva");
		//Me confirma se o que eu pedi realmente veio
		assertEquals(3, usuario.size());
		assertTrue(usuario.get(2).getNome().equals("Maiar Silva"));
		assertTrue(usuario.get(0).getNome().equals("Michael Silva"));
		assertTrue(usuario.get(1).getNome().equals("Brocco Silva"));
	}
	
	//a notação define que essa função vem depois de tudo, e a função para deletar tudo após o teste.
	@AfterAll
	public void end() {
		repository.deleteAll();
	}
	
}
