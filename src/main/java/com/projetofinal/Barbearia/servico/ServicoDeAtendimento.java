package com.projetofinal.Barbearia.servico;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetofinal.Barbearia.enumerador.StatusAtendimento;
import com.projetofinal.Barbearia.negocio.Atendimento;
import com.projetofinal.Barbearia.negocio.GeradorDeHorarios;
import com.projetofinal.Barbearia.negocio.Servico;
import com.projetofinal.Barbearia.repositorio.RepositorioDeAtendimentoImpl;

@Service
public class ServicoDeAtendimento {
	@Autowired
	private RepositorioDeAtendimentoImpl repositorio;

	public Atendimento cadastre(Atendimento atendimento) {
		return repositorio.save(atendimento);
	}

	public List<Atendimento> cadastre(Atendimento atendimento, String horarioDeInicio, String horarioDeFim, String periodo) {
		List<Atendimento> atendimentos = new ArrayList<Atendimento>();
		GeradorDeHorarios gerenciador = new GeradorDeHorarios();

		try {
			List<String> horarios = gerenciador.gere(atendimento.getDataEHorario(), horarioDeInicio, horarioDeFim, periodo);

			for (String horario : horarios) {
				Atendimento atendimentoAuxiliar = atendimento.clone();
				atendimentoAuxiliar.setDataEHorario(horario);
				atendimentoAuxiliar.setStatus(StatusAtendimento.LIVRE.getCodigo());
				atendimentos.add(atendimentoAuxiliar);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return (List<Atendimento>) repositorio.saveAll(atendimentos);
	}
	
	public boolean atualize(Long id, StatusAtendimento status, Integer pagamento) {
		return repositorio.atualize(id, status, pagamento);
	}
	
	public boolean atualize(Long id, Long idUsuario, List<Integer> servicos, Float valor) {
		return repositorio.atualize(id, idUsuario, servicos, valor);
	}

	public List<Servico> consulteServicosDoAtendimento(Long id) {
		return repositorio.consulteServicosDoAtendimento(id);
	}

	public void excluaAgendamento(Long id) {
		repositorio.exclua(id);
	}

	public List<Atendimento> consultePorFuncionario(Long idFuncionario) {
		return repositorio.consultePorFuncionario(idFuncionario);
	}
	
	public List<Atendimento> consulteTodosPorUsuario(Long idUsuario){
		return repositorio.consulteTodosPorUsuario(idUsuario);
	}
	
	public List<Atendimento> consulteTodos() {
		return (List<Atendimento>) repositorio.findAll();
	}

	public void exclua(Long id) {
		repositorio.deleteById(id);
	}
}
