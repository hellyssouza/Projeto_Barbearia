package com.projetofinal.Barbearia.repositorio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.projetofinal.Barbearia.negocio.Funcionario;

@Repository
@EnableJpaRepositories(basePackageClasses = { FuncionarioRepositorio.class })
public class RepositorioDeFuncionarioImpl implements FuncionarioRepositorio {
	@Autowired
	private FuncionarioRepositorio repositorio;

	@Override
	public <S extends Funcionario> S save(S funcionario) {
		return repositorio.save(funcionario);
	}

	@Override
	public <S extends Funcionario> Iterable<S> saveAll(Iterable<S> funcionarios) {
		return repositorio.saveAll(funcionarios);
	}

	@Override
	public Optional<Funcionario> findById(Long id) {
		return repositorio.findById(id);
	}

	@Override
	public boolean existsById(Long id) {
		return repositorio.existsById(id);
	}

	@Override
	public Iterable<Funcionario> findAll() {
		return repositorio.findAll();
	}

	@Override
	public Iterable<Funcionario> findAllById(Iterable<Long> ids) {
		return repositorio.findAllById(ids);
	}

	@Override
	public long count() {
		return repositorio.count();
	}

	@Override
	public void deleteById(Long id) {
		repositorio.deleteById(id);
	}

	@Override
	public void delete(Funcionario funcionario) {
		repositorio.delete(funcionario);
	}

	@Override
	public void deleteAll(Iterable<? extends Funcionario> funcionario) {
		repositorio.deleteAll(funcionario);
	}

	@Override
	public void deleteAll() {
		repositorio.deleteAll();
	}
}
