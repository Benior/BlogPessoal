package com.generation.blogpessoal.service;

import java.util.Optional;
import java.nio.charset.Charset;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;
	
	//função para cadastrar usuario
	public Optional<Usuario>cadastrarUsuario(Usuario usuario){
		//valida se tem um usuario igual no db
		if(repository.findByUsuario(usuario.getUsuario()).isPresent())
			return Optional.empty();
			//criptogradfa a senha do usuário caso não exista
			usuario.setSenha(criptografarSenha(usuario.getSenha()));
			//e por ultimo, salvo o usuario com a senha já criptografada
			return Optional.of(repository.save(usuario));
		
	}
	
	public Optional<Usuario>atualizarUsuario(Usuario usuario){
		
		if (repository.findById(usuario.getId()).isPresent()){
			
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
			
		return Optional.of(repository.save(usuario));
		}
		return Optional.empty();
		
	}
	
	//função para criptografar a senha digitada pelo usuario
	private String criptografarSenha(String senha) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(senha);

	}
	
	
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {

		Optional<Usuario> usuario = repository.findByUsuario(usuarioLogin.get().getUsuario());

		if (usuario.isPresent()) {
			if (compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {
				/*
				 * uma vez que o usuário é validado essa função seta as informações 
				 * dentro de login usuario e gera o token
				 */
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setTipo(usuario.get().getTipo());
				usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());

				return usuarioLogin;
			}
		}	
		return Optional.empty();
	}
	
	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.matches(senhaDigitada, senhaBanco);

	}
	//gerar o token
	private String gerarBasicToken(String usuario, String senha) {

		String token = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);

	}



}
