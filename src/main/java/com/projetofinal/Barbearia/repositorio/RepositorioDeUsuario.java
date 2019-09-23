package com.projetofinal.Barbearia.repositorio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.projetofinal.Barbearia.negocio.Usuario;

@Repository
@EnableJpaRepositories(basePackageClasses = { UsuarioRepository.class })
public class RepositorioDeUsuario implements UsuarioRepository {
	@Autowired
	private UsuarioRepository repositorio;

	@Override
	public <S extends Usuario> S save(S entity) {
		return repositorio.save(entity);
	}

	@Override
	public <S extends Usuario> Iterable<S> saveAll(Iterable<S> entities) {
		return repositorio.saveAll(entities);
	}

	@Override
	public Optional<Usuario> findById(Long id) {
		return repositorio.findById(id);
	}

	@Override
	public boolean existsById(Long id) {
		return repositorio.existsById(id);
	}

	@Override
	public Iterable<Usuario> findAll() {
		return repositorio.findAll();
	}

	@Override
	public Iterable<Usuario> findAllById(Iterable<Long> ids) {
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
	public void delete(Usuario entity) {
		repositorio.delete(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends Usuario> entities) {
		repositorio.deleteAll(entities);
	}

	@Override
	public void deleteAll() {
		repositorio.deleteAll();
	}
}
