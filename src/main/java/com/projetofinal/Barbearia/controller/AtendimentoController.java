package com.projetofinal.Barbearia.controller;

import java.text.ParseException;
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
import com.google.gson.JsonObject;
import com.projetofinal.Barbearia.negocio.Atendimento;
import com.projetofinal.Barbearia.servico.ServicoDeAtendimento;

@Controller
public class AtendimentoController {
	@Autowired
	private ServicoDeAtendimento servicoDeAtendimento;
	private Gson conversor = new Gson();

	@ResponseBody
	@RequestMapping(value = "/cadastreatendimento", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> cadastre(@RequestBody String conteudo) throws ParseException {
		Atendimento atendimento = conversor.fromJson(conteudo, Atendimento.class);

		JsonObject jsonObjeto = conversor.fromJson(conteudo, JsonObject.class);

		String horarioInicio = jsonObjeto.get("HorarioInicio").getAsString();

		String horarioFim = jsonObjeto.get("HorarioFim").getAsString();

		String periodo = jsonObjeto.get("Periodo").getAsString();

		if(horarioInicio.equals(horarioFim)) 
		{
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Intervalo de horários invalido!");
		}
		
		List<Atendimento> atendimentos = servicoDeAtendimento.cadastre(atendimento, horarioInicio, horarioFim, periodo);

		return ResponseEntity.ok().body(conversor.toJson(atendimentos));
	}

	@ResponseBody
	@RequestMapping(value = "/excluaatendimento", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> exclua(@RequestBody String conteudo) {
		Atendimento atendimento = conversor.fromJson(conteudo, Atendimento.class);

		servicoDeAtendimento.exclua(atendimento.getId());

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "/consulteatendimentos", method = RequestMethod.GET)
	public ResponseEntity<String> consulte() {
		GsonBuilder gsonBuilder = new GsonBuilder();

		gsonBuilder.serializeNulls();

		conversor = gsonBuilder.create();

		List<Atendimento> atendimentos = servicoDeAtendimento.consulteTodos();

		return ResponseEntity.ok().body(conversor.toJson(atendimentos));
	}
}
