package com.projetofinal.Barbearia.repositorio;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.projetofinal.Barbearia.negocio.Atendimento;

@Repository
@EnableJpaRepositories(basePackageClasses = { AtendimentorRepositorio.class })
public class RepositorioDeAtendimentoImpl implements AtendimentorRepositorio {
	@Autowired
	private AtendimentorRepositorio repositorio;
	@Autowired
	private EntityManagerFactory fabricaDeEntityManager;

	public boolean atualize(Long id, Long idUsuario, Float valor) {
		EntityManager entityManager = fabricaDeEntityManager.createEntityManager();
		boolean sucesso = false;

		Query update = entityManager.createQuery("UPDATE Atendimento SET Usuario = :usuario, Valor = :valor where Id = :id");

		update.setParameter("usuario", idUsuario);
		update.setParameter("id", id);
		update.setParameter("valor", valor);

		try {
			entityManager.getTransaction().begin();
			update.executeUpdate();
			entityManager.getTransaction().commit();
			sucesso = true;
		} catch (Exception exception) {
			entityManager.getTransaction().rollback();
		}
		finally {
			entityManager.close();
		}
		
		return sucesso;
	}

	public List<Atendimento> consultePorFuncionario(Long idFuncionario) {
		EntityManager entityManager = fabricaDeEntityManager.createEntityManager();

		TypedQuery<Atendimento> query = entityManager
				.createQuery("select at from Atendimento at where at.Funcionario = :id", Atendimento.class);

		query.setParameter("id", idFuncionario);
		
		List<Atendimento> atendimentos = query.getResultList();
		
		entityManager.close();
		
		return atendimentos;
	}

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
