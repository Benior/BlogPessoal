package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Test
	@Order(1)
	@DisplayName("Cadastrar um usuário")
	public void deveCriarUmUsuario () {
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,
				"Paulo antunes","paulo_antunes@email.com.br", "1234567989", "https://i.imgur.com/FETvs2O.jpg"));
		
		ResponseEntity<Usuario> resposta = testRestTemplate
				.exchange("/usuario/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(),resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(),resposta.getBody().getUsuario());
		
	}
	
	@Test
	@Order(2)
	@DisplayName("Não pode duplicar um usuário")
	public void naoDeveDuplicarUmUsuario () {
		usuarioService.cadastrarUsuario(new Usuario(0L,
				"Maria da Silva","maria_silva@email.com.br", "1234567989", "https://i.imgur.com/FETvs2O.jpg"));
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,
				"Maria da Silva","maria_silva@email.com.br", "1234567989", "https://i.imgur.com/FETvs2O.jpg"));
		
		ResponseEntity<Usuario> resposta = testRestTemplate
				.exchange("/usuario/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}
	
	@Test
	@Order(3)
	@DisplayName("Deve atualizar um usuario")
	public void atualizarUmUsuario () {
		Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L,
				"Juliana Andrews","juliana_andrews@email.com.br", "juliana123", "https://i.imgur.com/FETvs2O.jpg"));
		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(),
				"Juliana Andrews Ramos","juliana_ramos@email.com.br", "juliana123", "https://i.imgur.com/FETvs2O.jpg");
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth("root","root")
				.exchange("/usuario/atualizar", HttpMethod.PUT, requisicao, Usuario.class);
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
		assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
		
	}
	@Test
	@Order(4)
	@DisplayName("Deve mostrar todos os usuarios")
	public void mostrarTodosUsuario () {
		usuarioService.cadastrarUsuario(new Usuario(0L,
				"Sabrina Sanches","sabrina_sanches@email.com.br", "sabrina123", "https://i.imgur.com/FETvs2O.jpg"));
		usuarioService.cadastrarUsuario(new Usuario(0L,
				"Ricardo Marques","ricardo_marques@email.com.br", "ricard123", "https://i.imgur.com/FETvs2O.jpg"));
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root","root")
				.exchange("/usuario/all", HttpMethod.GET, null, String.class);
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}
}
