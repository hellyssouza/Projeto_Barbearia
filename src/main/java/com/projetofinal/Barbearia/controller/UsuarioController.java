package com.projetofinal.Barbearia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.projetofinal.Barbearia.enumerador.Permissao;
import com.projetofinal.Barbearia.negocio.Usuario;
import com.projetofinal.Barbearia.servico.ServicoDeUsuario;

@Controller
public class UsuarioController {
	@Autowired
	private BCryptPasswordEncoder encriptador;
	@Autowired
	private ServicoDeUsuario servico;
	private Gson conversor = new Gson();

	@RequestMapping(value = "/usuario", method = RequestMethod.GET)
	public String cadastrousuario() {
		return "cadastrousuario";
	}

	@RequestMapping(value = "/cadastreusuario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> cadastrar(@RequestBody String conteudo) {
		Usuario usuario = conversor.fromJson(conteudo, Usuario.class);
		usuario.setSenha(encriptador.encode(usuario.getSenha()));
		usuario.setPermissao((long) Permissao.CLIENTE.ordinal());
		usuario.setFuncionario(null);

		Usuario usuarioCadastrado = servico.cadastre(usuario);

		String jsonRetorno = usuarioCadastrado != null ? "{ \"cadastrado\": true }" : "{ \"cadastrado\": false }";

		return ResponseEntity.ok().body(conversor.toJson(jsonRetorno));
	}
}
