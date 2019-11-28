package com.projetofinal.Barbearia.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import com.projetofinal.Barbearia.negocio.Usuario;
import com.projetofinal.Barbearia.relatorio.GeradorDeRelatorioAtendimentos;
import com.projetofinal.Barbearia.servico.ServicoDeAtendimento;
import com.projetofinal.Barbearia.servico.ServicoDeUsuario;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
public class AtendimentoController {
	private GeradorDeRelatorioAtendimentos gerador = new GeradorDeRelatorioAtendimentos();
	@Autowired
	private ServicoDeAtendimento servicoDeAtendimento;
	@Autowired
	private ServicoDeUsuario servicoDeUsuario;
	private Gson conversor = new Gson();

	@ResponseBody
	@RequestMapping(value = "/cadastreatendimento", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> cadastre(@RequestBody String conteudo) throws ParseException {
		Atendimento atendimento = conversor.fromJson(conteudo, Atendimento.class);

		JsonObject jsonObjeto = conversor.fromJson(conteudo, JsonObject.class);

		String horarioInicio = jsonObjeto.get("HorarioInicio").getAsString();

		String horarioFim = jsonObjeto.get("HorarioFim").getAsString();

		String periodo = jsonObjeto.get("Periodo").getAsString();

		if (horarioInicio.equals(horarioFim)) {
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
	@RequestMapping(value = "/gererelatorio", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/pdf")
	public ResponseEntity<byte[]> gereRelatorio(@RequestBody String conteudo) {
		JsonObject jsonObjeto = conversor.fromJson(conteudo, JsonObject.class);
		HttpHeaders headers = new HttpHeaders();
		byte[] dados = null;
		
		try {
			String dataDeInicio = jsonObjeto.get("dataDeInicio").getAsString();
			String dataDeFim = jsonObjeto.get("dataDeFim").getAsString();
			Integer funcionario = jsonObjeto.get("funcionario").getAsInt();
			Integer status = jsonObjeto.get("status").getAsInt();
			Integer pagamento = jsonObjeto.get("pagamento").getAsInt();
			
			JasperPrint print = gerador.gereRelatorio(dataDeInicio, dataDeFim, funcionario, status, pagamento);
			
			dados = JasperExportManager.exportReportToPdf(print);

			headers.setContentType(MediaType.parseMediaType("application/pdf"));

			headers.add("content-disposition", "inline; filename=" + "relatorio.pdf");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<byte[]>(dados, headers, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "/consulteatendimentos", method = RequestMethod.GET)
	public ResponseEntity<String> consulte() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		List<Atendimento> atendimentos = null;

		gsonBuilder.serializeNulls();

		conversor = gsonBuilder.create();

		if (servicoDeUsuario.usuarioLogadoEFuncionario()) {
			Long idUsuario = servicoDeUsuario.consulteIdDoUsuario();

			Long idFuncionario = servicoDeUsuario.consulteIdDeFuncionario(idUsuario);

			atendimentos = servicoDeAtendimento.consultePorFuncionario(idFuncionario);
		} else {
			atendimentos = servicoDeAtendimento.consulteTodos();
		}

		return ResponseEntity.ok().body(conversor.toJson(atendimentos));
	}

	@ResponseBody
	@RequestMapping(value = "/consulteatendimentosparausuario", method = RequestMethod.GET)
	public ResponseEntity<String> consulteatendimentosparausuario() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		List<Atendimento> atendimentos = null;

		gsonBuilder.serializeNulls();

		conversor = gsonBuilder.create();

		Long idUsuario = servicoDeUsuario.consulteIdDoUsuario();

		atendimentos = servicoDeAtendimento.consulteTodosPorUsuario(idUsuario);

		return ResponseEntity.ok().body(conversor.toJson(atendimentos));
	}

	@RequestMapping(value = "/consulteusuario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> consulte(@RequestBody String conteudo) {
		JsonObject objetoJson = conversor.fromJson(conteudo, JsonObject.class);

		Usuario usuario = servicoDeUsuario.obtenhaUsuarioPeloId(objetoJson.get("id").getAsLong());

		return ResponseEntity.ok().body(conversor.toJson(usuario));
	}
}
