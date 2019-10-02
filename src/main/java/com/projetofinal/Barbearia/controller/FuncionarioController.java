package com.projetofinal.Barbearia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.projetofinal.Barbearia.enumerador.Permissao;
import com.projetofinal.Barbearia.negocio.Funcionario;
import com.projetofinal.Barbearia.negocio.Usuario;
import com.projetofinal.Barbearia.servico.ServicoDeFuncionario;
import com.projetofinal.Barbearia.servico.ServicoDeUsuario;

@Controller
public class FuncionarioController {
	@Autowired
	private ServicoDeFuncionario servicoDeFuncionario;
	@Autowired
	private ServicoDeUsuario servicoDeUsuario;
	private Gson conversor = new Gson();

	@ResponseBody
	@RequestMapping(value = "/cadastrefuncionario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> cadastre(@RequestBody String conteudo) {
		JsonObject jsonObjeto = conversor.fromJson(conteudo, JsonObject.class);

		Long idUsuario = jsonObjeto.get("IdUsuario").getAsLong();

		Funcionario funcionario = conversor.fromJson(conteudo, Funcionario.class);

		funcionario = servicoDeFuncionario.cadastre(funcionario);

		servicoDeUsuario.atualize(idUsuario, funcionario.getId(), Permissao.FUNCIONARIO);

		return ResponseEntity.ok().body(conversor.toJson(funcionario));
	}

	@ResponseBody
	@RequestMapping(value = "/excluafuncionario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> exclua(@RequestBody String conteudo) {
		Funcionario funcionario = conversor.fromJson(conteudo, Funcionario.class);

		Long idUsuario = servicoDeUsuario.consulteIdUsuario(funcionario.getId());

		servicoDeUsuario.atualize(idUsuario, null, Permissao.CLIENTE);

		servicoDeFuncionario.exclua(funcionario.getId());

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "/consultefuncionarios", method = RequestMethod.GET)
	public ResponseEntity<String> consulte() {
		List<Funcionario> funcionarios = servicoDeFuncionario.consulteTodos();

		return ResponseEntity.ok().body(conversor.toJson(funcionarios));
	}

	@ResponseBody
	@RequestMapping(value = "/consulteusuarios", method = RequestMethod.GET)
	public ResponseEntity<String> consulteUsuarios() {
		List<Usuario> funcionarios = servicoDeUsuario.consulteTodos();

		return ResponseEntity.ok().body(conversor.toJson(funcionarios));
	}
}
