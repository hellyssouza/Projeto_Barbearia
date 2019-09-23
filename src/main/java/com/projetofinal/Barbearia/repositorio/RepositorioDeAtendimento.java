package com.projetofinal.Barbearia.repositorio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.projetofinal.Barbearia.negocio.Atendimento;

@Repository
@EnableJpaRepositories(basePackageClasses = { AtendimentoRepository.class })
public class RepositorioDeAtendimento implements AtendimentoRepository {
	@Autowired
	private AtendimentoRepository repositorio;
	
	@Override
	public <S extends Atendimento> S save(S atendimento) {
		return repositorio.save(atendimento);
	}

	@Override
	public <S extends Atendimento> Iterable<S> saveAll(Iterable<S> atendimentos) {
		return repositorio.saveAll(atendimentos);
	}

	@Override
	public Optional<Atendimento> findById(Long id) {
		return repositorio.findById(id);
	}

	@Override
	public boolean existsById(Long id) {
		return repositorio.existsById(id);
	}

	@Override
	public Iterable<Atendimento> findAll() {
		return repositorio.findAll();
	}

	@Override
	public Iterable<Atendimento> findAllById(Iterable<Long> ids) {
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
	public void delete(Atendimento atendimento) {
		repositorio.delete(atendimento);
	}

	@Override
	public void deleteAll(Iterable<? extends Atendimento> atendimentos) {
		repositorio.deleteAll(atendimentos);
	}

	@Override
	public void deleteAll() {
		repositorio.deleteAll();
	}
}
