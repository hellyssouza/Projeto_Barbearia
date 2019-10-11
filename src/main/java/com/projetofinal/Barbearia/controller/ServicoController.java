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
import com.projetofinal.Barbearia.negocio.Servico;
import com.projetofinal.Barbearia.servico.ServicoDeServico;

@Controller
public class ServicoController {
	@Autowired
	private ServicoDeServico servicoDeServico;
	private Gson conversor = new Gson();
	
	@ResponseBody
	@RequestMapping(value = "/cadastreservico", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> cadastre(@RequestBody String conteudo) {
		Servico servico = conversor.fromJson(conteudo, Servico.class);

		servico = servicoDeServico.cadastre(servico);
		
		return ResponseEntity.ok().body(conversor.toJson(servico));
	}
	
	@ResponseBody
	@RequestMapping(value = "/excluaservico", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> exclua(@RequestBody String conteudo) {
		JsonObject objeto = conversor.fromJson(conteudo, JsonObject.class);
		
		servicoDeServico.exclua(objeto.get("id").getAsLong());

		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "/consulteservicos", method = RequestMethod.GET)
	public ResponseEntity<String> consulte() {
		List<Servico> servicos = servicoDeServico.consulteTodos();

		return ResponseEntity.ok().body(conversor.toJson(servicos));
	}
}
