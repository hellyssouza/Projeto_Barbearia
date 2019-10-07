package com.projetofinal.Barbearia.servico;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetofinal.Barbearia.negocio.Atendimento;
import com.projetofinal.Barbearia.negocio.GeradorDeHorarios;
import com.projetofinal.Barbearia.repositorio.RepositorioDeAtendimentoImpl;

@Service
public class ServicoDeAtendimento {
	@Autowired
	private RepositorioDeAtendimentoImpl repositorio;

	public Atendimento cadastre(Atendimento atendimento) {
		return repositorio.save(atendimento);
	}

	public List<Atendimento> cadastre(Atendimento atendimento, String horarioDeInicio, String horarioDeFim,
			String periodo) {
		List<Atendimento> atendimentos = new ArrayList<Atendimento>();
		GeradorDeHorarios gerenciador = new GeradorDeHorarios();

		try {
			List<String> horarios = gerenciador.gere(atendimento.getDataEHorario(), horarioDeInicio, horarioDeFim,
					periodo);
			
			for (String horario : horarios) {
				Atendimento atendimentoAuxiliar = atendimento.clone();
				atendimentoAuxiliar.setDataEHorario(horario);
				atendimentos.add(atendimentoAuxiliar);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return (List<Atendimento>) repositorio.saveAll(atendimentos);
	}

	public boolean atualize(Long id, Long idUsuario) {
		return repositorio.atualize(id, idUsuario);
	}

	public List<Atendimento> consulteTodos() {
		return (List<Atendimento>) repositorio.findAll();
	}

	public void exclua(Long id) {
		repositorio.deleteById(id);
	}
}
