package com.projetofinal.Barbearia.repositorio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import com.projetofinal.Barbearia.negocio.Servico;

@Repository
@EnableJpaRepositories(basePackageClasses = { ServicoRepositorio.class })
public class RepositorioDeServicoImpl implements ServicoRepositorio {
	@Autowired
	private ServicoRepositorio repositorio;

	@Override
	public <S extends Servico> S save(S servico) {
		return repositorio.save(servico);
	}

	@Override
	public <S extends Servico> Iterable<S> saveAll(Iterable<S> servicos) {
		return repositorio.saveAll(servicos);
	}

	@Override
	public Optional<Servico> findById(Long id) {
		return repositorio.findById(id);
	}

	@Override
	public boolean existsById(Long id) {
		return repositorio.existsById(id);
	}

	@Override
	public Iterable<Servico> findAll() {
		return repositorio.findAll();
	}

	@Override
	public Iterable<Servico> findAllById(Iterable<Long> ids) {
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
	public void delete(Servico servico) {
		repositorio.delete(servico);
	}

	@Override
	public void deleteAll(Iterable<? extends Servico> servicos) {
		repositorio.deleteAll(servicos);
	}

	@Override
	public void deleteAll() {
		repositorio.deleteAll();
	}
}
