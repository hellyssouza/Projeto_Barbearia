package com.projetofinal.Barbearia.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetofinal.Barbearia.negocio.Atendimento;
import com.projetofinal.Barbearia.repositorio.RepositorioDeAtendimentoImpl;

@Service
public class ServicoDeAtendimento {
	@Autowired
	private RepositorioDeAtendimentoImpl repositorio;
	
	public Atendimento cadastre(Atendimento atendimento) {
		return repositorio.save(atendimento);
	}
	
	public boolean atualize(Long id, Long idUsuario) {
		return repositorio.atualize(id, idUsuario);
	}
	
	public List<Atendimento> consulteTodos(){
		return (List<Atendimento>) repositorio.findAll();
	}
	
	public void exclua(Long id) {
		repositorio.deleteById(id);
	}
}
