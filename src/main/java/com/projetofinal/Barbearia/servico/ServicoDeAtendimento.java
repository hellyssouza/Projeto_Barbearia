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
	
	public void cadastreLista(List<Atendimento> atendimentos) 
	{
		repositorio.saveAll(atendimentos);
	}
	
	public Atendimento cadastre(Atendimento atendimento) {
		return repositorio.save(atendimento);
	}
	
	public List<Atendimento> obtenhaTodos(){
		return (List<Atendimento>) repositorio.findAll();
	}
	
	public void remova(Long id) {
		repositorio.deleteById(id);
	}
}
