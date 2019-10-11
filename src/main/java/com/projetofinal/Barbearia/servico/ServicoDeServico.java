package com.projetofinal.Barbearia.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetofinal.Barbearia.negocio.Servico;
import com.projetofinal.Barbearia.repositorio.RepositorioDeServicoImpl;

@Service
public class ServicoDeServico {
	@Autowired
	private RepositorioDeServicoImpl repositorio;
	
	public Servico cadastre(Servico servico) 
	{
		return repositorio.save(servico);
	}
	
	public void exclua(Long id) 
	{
		repositorio.deleteById(id);
	}
	
	public List<Servico> consulteTodos() {
		return (List<Servico>) repositorio.findAll();
	}
}
