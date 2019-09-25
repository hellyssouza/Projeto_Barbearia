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
import com.projetofinal.Barbearia.negocio.Funcionario;
import com.projetofinal.Barbearia.servico.ServicoDeFuncionario;

@Controller
public class FuncionarioController {
	@Autowired
	private ServicoDeFuncionario servico;
	private Gson conversor = new Gson();
	
	@ResponseBody
	@RequestMapping(value = "/cadastrefuncionario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> cadastre(@RequestBody String conteudo) {
		Funcionario funcionario = conversor.fromJson(conteudo, Funcionario.class); 
		
		funcionario = servico.cadastre(funcionario);
		
		return ResponseEntity.ok().body(conversor.toJson(funcionario));
	}
	
	@ResponseBody
	@RequestMapping(value = "/excluafuncionario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> exclua(@RequestBody String conteudo) {
		Funcionario funcionario = conversor.fromJson(conteudo, Funcionario.class); 
		
		servico.exclua(funcionario.getId());
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "/consulteafuncionarios", method = RequestMethod.GET)
	public ResponseEntity<String> consulte() {
		List<Funcionario> funcionarios = servico.consulteTodos();
		
		return ResponseEntity.ok().body(conversor.toJson(funcionarios));
	}
}
