package com.projetofinal.Barbearia.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final String SELECT_USUARIOS = "SELECT LOGIN,SENHA,ATIVO,NOME FROM USUARIO WHERE LOGIN = ?";
	private final String SELECT_PERMISSOES = "SELECT USUARIO.NOME,PERMISSAO.NOME FROM USUARIO \r\n"
			+ "INNER JOIN PERMISSAO ON USUARIO.PERMISSAO = PERMISSAO.ID\r\n" + "WHERE USUARIO.LOGIN = ?";

	@Bean
	public PasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder builder, PasswordEncoder encoder, DataSource dataSource)
			throws Exception {
		builder.jdbcAuthentication().dataSource(dataSource).passwordEncoder(encoder)
				.usersByUsernameQuery(SELECT_USUARIOS).authoritiesByUsernameQuery(SELECT_PERMISSOES);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests().antMatchers("/usuario","/cadastreusuario", "/js/**", "/css/**").permitAll()
				.anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("https://localhost"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
