package com.projetofinal.Barbearia.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetofinal.Barbearia.negocio.Atendimento;
import com.projetofinal.Barbearia.repositorio.RepositorioDeAtendimento;

@Service
public class ServicoDeAtendimento {
	@Autowired
	private RepositorioDeAtendimento repositorio;
	
	public Atendimento cadastre(Atendimento atendimento) {
		return repositorio.save(atendimento);
	}
	
	public List<Atendimento> consulteTodos(){
		return (List<Atendimento>) repositorio.findAll();
	}
	
	public void exclua(Long id) {
		repositorio.deleteById(id);
	}
}
