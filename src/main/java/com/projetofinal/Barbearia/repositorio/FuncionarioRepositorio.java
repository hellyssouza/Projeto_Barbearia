package com.projetofinal.Barbearia.repositorio;

import org.springframework.data.repository.CrudRepository;

import com.projetofinal.Barbearia.negocio.Funcionario;

public interface FuncionarioRepositorio extends CrudRepository<Funcionario, Long>{
}
