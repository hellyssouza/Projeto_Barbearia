package com.projetofinal.Barbearia.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetofinal.Barbearia.negocio.Funcionario;
import com.projetofinal.Barbearia.repositorio.RepositorioDeFuncionario;

@Service
public class ServicoDeFuncionario {
	@Autowired
	private RepositorioDeFuncionario repositorio;

	public Funcionario cadastre(Funcionario funcionario) {
		return repositorio.save(funcionario);
	}

	public void exclua(Long id) {
		repositorio.deleteById(id);
	}
	
	public List<Funcionario> consulteTodos() {
		return (List<Funcionario>) repositorio.findAll();
	}
}
