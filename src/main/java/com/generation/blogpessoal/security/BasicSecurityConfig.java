package com.generation.blogpessoal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

	//serve para comparar os dados do usuário cadastrado no db com o que foi digitado.
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override //método de usuário de testes da API
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsService);
		auth.inMemoryAuthentication()
		.withUser("root")
		.password(passwordEncoder().encode("root"))
		.authorities("ROLE_USER");
	}
	
	//Notação para liberar uma função acessível para toda a aplicação
	@Bean
	//função que criptografa a senha digitada
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests() //autorizar o request
		.antMatchers("/usuario/logar").permitAll() // permite o acesso a pagina de login de qualquer lugar
		.antMatchers("/usuario/cadastrar").permitAll() // permite o acesso a pagina de cadastro de qualquer lugar
		.antMatchers(HttpMethod.OPTIONS).permitAll() // Libera quais metodos tenho disponível para usar
		.anyRequest().authenticated() // vai querer acesso para efetuar alterações
		.and().httpBasic()
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().cors() // permitir o acesso de qualquer porta. tanto back quanto front
		.and().csrf().disable(); //autoriza o put e delete.

	}
	
//	.antMatchers("/**").permitAll()
//	.antMatchers(HttpMethod.GET ,"/postagens").permitAll()
//	.antMatchers(HttpMethod.GET ,"/tema").permitAll()

	
}
