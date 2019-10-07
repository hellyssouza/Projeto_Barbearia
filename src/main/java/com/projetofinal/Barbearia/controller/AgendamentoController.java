package com.projetofinal.Barbearia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.projetofinal.Barbearia.negocio.Usuario;
import com.projetofinal.Barbearia.servico.ServicoDeAtendimento;
import com.projetofinal.Barbearia.servico.ServicoDeUsuario;

@Controller
public class AgendamentoController {
	@Autowired
	private ServicoDeAtendimento servico;
	@Autowired
	private ServicoDeUsuario servicoDeUsuario;
	private Gson conversor = new Gson();
	
	@ResponseBody
	@RequestMapping(value = "/consulteusuariocontexto", method = RequestMethod.GET)
	public ResponseEntity<String> consulteUsuarioDoContexto()
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		
		gsonBuilder.serializeNulls();
		
		conversor = gsonBuilder.create();
		
		Authentication usuarioDoContexto = SecurityContextHolder.getContext().getAuthentication();
		
		Usuario usuario = new Usuario();
		
		usuario.setNome(usuarioDoContexto.getName());
		
		usuario.setId(servicoDeUsuario.consulteIdPeloNome(usuario.getNome()));
		
		return ResponseEntity.ok().body(conversor.toJson(usuario));
	}
	
	@ResponseBody
	@RequestMapping(value = "/efetueagendamento", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> cadastre(@RequestBody String conteudo) {
		JsonObject jsonObjeto = conversor.fromJson(conteudo, JsonObject.class);
		
		Long idAgendamento = jsonObjeto.get("idAgendamento").getAsLong(); 
		
		Long idUsuario = jsonObjeto.get("idUsuario").getAsLong();
		
		servico.atualize(idAgendamento, idUsuario);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "/excluaagendamento", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> exclua(@RequestBody String conteudo) {
		JsonObject jsonObjeto = conversor.fromJson(conteudo, JsonObject.class);
		
		Long idAgendamento = jsonObjeto.get("idAgendamento").getAsLong(); 
		
		servico.atualize(idAgendamento, null);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
