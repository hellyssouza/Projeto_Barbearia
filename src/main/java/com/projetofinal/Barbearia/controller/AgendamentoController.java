package com.projetofinal.Barbearia.controller;

import java.util.ArrayList;
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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.projetofinal.Barbearia.enumerador.StatusAtendimento;
import com.projetofinal.Barbearia.negocio.Servico;
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
		
		Usuario usuario = servicoDeUsuario.obtenhaUsuarioLogado();
		
		usuario.setId(servicoDeUsuario.consulteIdDoUsuario());
		
		return ResponseEntity.ok().body(conversor.toJson(usuario));
	}
	
	@ResponseBody
	@RequestMapping(value = "/consulteservicosdoatendimento", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> consulteServicosDoAtendimento(@RequestBody String conteudo)
	{
		JsonObject jsonObjeto = conversor.fromJson(conteudo, JsonObject.class);
		
		Long idAgendamento = jsonObjeto.get("idAgendamento").getAsLong();
		
		List<Servico> servicos = servico.consulteServicosDoAtendimento(idAgendamento);
		
		return ResponseEntity.ok().body(conversor.toJson(servicos));
	}
	
	@ResponseBody
	@RequestMapping(value = "/efetueagendamento", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> cadastre(@RequestBody String conteudo) {
		JsonObject jsonObjeto = conversor.fromJson(conteudo, JsonObject.class);
		
		List<Integer> idsServico = new ArrayList<Integer>();
		
		Long idAgendamento = jsonObjeto.get("idAgendamento").getAsLong(); 
		
		Long idUsuario = jsonObjeto.get("idUsuario").getAsLong();
		
		Float valor = jsonObjeto.get("valor").getAsFloat();
		
		JsonArray servicos = jsonObjeto.getAsJsonArray("servicos");
		
		servicos.spliterator().forEachRemaining(x -> idsServico.add(x.getAsInt()));
		
		servico.atualize(idAgendamento, idUsuario, idsServico, valor);
		
		servico.atualize(idAgendamento, StatusAtendimento.AGENDADO, null);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "/excluaagendamento", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> exclua(@RequestBody String conteudo) {
		JsonObject jsonObjeto = conversor.fromJson(conteudo, JsonObject.class);
		
		Long idAgendamento = jsonObjeto.get("idAgendamento").getAsLong(); 
		
		servico.excluaAgendamento(idAgendamento);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "/atulizestatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> atualizeStatus(@RequestBody String conteudo) {
		JsonObject jsonObjeto = conversor.fromJson(conteudo, JsonObject.class);
		
		Long idAgendamento = jsonObjeto.get("idAgendamento").getAsLong(); 
		
		Integer pagamento = jsonObjeto.get("pagamento").getAsInt();
		
		StatusAtendimento status = StatusAtendimento.LIVRE;
		
		status.setCodigo(jsonObjeto.get("statusAgendamento").getAsInt());
		
		servico.atualize(idAgendamento, status, pagamento);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
