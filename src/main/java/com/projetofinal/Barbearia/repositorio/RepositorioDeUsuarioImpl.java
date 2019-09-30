package com.projetofinal.Barbearia.repositorio;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import com.projetofinal.Barbearia.negocio.Usuario;

@Repository
@EnableJpaRepositories(basePackageClasses = { UsuarioRepositorio.class })
public class RepositorioDeUsuarioImpl implements UsuarioRepositorio {
	@Autowired
	private UsuarioRepositorio repositorio;
	@Autowired
	private EntityManagerFactory fabricaDeEntityManager;

	@Override
	public <S extends Usuario> S save(S entity) {
		return repositorio.save(entity);
	}

	public boolean atualize(Long id, Long idFuncionario) {
		EntityManager entityManager = fabricaDeEntityManager.createEntityManager();
		
		Query update = entityManager.createQuery("UPDATE Usuario SET funcionario = :funcionario where id = :id");
		
		boolean sucesso = false;

		try {
			entityManager.getTransaction().begin();
			update.setParameter("funcionario", idFuncionario);
			update.setParameter("id", id);
			update.executeUpdate();
			sucesso = true;
			entityManager.getTransaction().commit();
		} catch (Exception exception) {
			entityManager.getTransaction().rollback();
		}

		return sucesso;
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

	@SuppressWarnings("unchecked")
	public List<Usuario> consulteTodos() {
		EntityManager entityManager = fabricaDeEntityManager.createEntityManager();
		
		Query query = entityManager.createQuery("SELECT u.id,u.nome FROM Usuario u where u.funcionario is null");

		return query.getResultList();
	}

	public Long consulteIdPeloNome(String nome) 
	{
		EntityManager entityManager = fabricaDeEntityManager.createEntityManager();
		
		Query query = entityManager.createQuery("SELECT u.id FROM Usuario u where u.nome = :nome");
		
		query.setParameter("nome", nome);
		
		return (long)query.getSingleResult();
	}
	
	public Long consulteIdDoUsuario(Long idFuncionario) {
		EntityManager entityManager = fabricaDeEntityManager.createEntityManager();
		
		Query query = entityManager.createQuery("SELECT us.id FROM Usuario us where us.funcionario = :id");

		query.setParameter("id", idFuncionario);
		
		return (long)query.getSingleResult();
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
