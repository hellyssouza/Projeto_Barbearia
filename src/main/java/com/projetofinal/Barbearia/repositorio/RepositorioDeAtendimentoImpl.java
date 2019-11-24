package com.projetofinal.Barbearia.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.projetofinal.Barbearia.enumerador.StatusAtendimento;
import com.projetofinal.Barbearia.negocio.Atendimento;
import com.projetofinal.Barbearia.negocio.Servico;

@Repository
@EnableJpaRepositories(basePackageClasses = { AtendimentorRepositorio.class })
public class RepositorioDeAtendimentoImpl implements AtendimentorRepositorio {
	@Autowired
	private AtendimentorRepositorio repositorio;
	@Autowired
	private EntityManagerFactory fabricaDeEntityManager;

	public boolean atualize(Long id, Long idUsuario, List<Integer> servicos, Float valor) {
		EntityManager entityManager = fabricaDeEntityManager.createEntityManager();
		boolean sucesso = false;

		Query update = entityManager.createQuery("UPDATE Atendimento SET Usuario = :usuario, Valor = :valor where Id = :id");

		Query insert = entityManager.createNativeQuery(
				"INSERT INTO ATENDIMENTO_SERVICO(IDATENDIMENTO,IDSERVICO) VALUES(:atendimento,:servico)");


		try {
			entityManager.getTransaction().begin();
			
			for (Integer servico : servicos) {
				insert.setParameter("atendimento", id);
				insert.setParameter("servico", servico);
				insert.executeUpdate();
			}
			
			update.setParameter("usuario", idUsuario);
			update.setParameter("id", id);
			update.setParameter("valor", valor);
			update.executeUpdate();

			entityManager.getTransaction().commit();
			sucesso = true;
		} catch (Exception exception) {
			entityManager.getTransaction().rollback();
		} finally {
			entityManager.close();
		}

		return sucesso;
	}
	
	public boolean atualize(Long id, StatusAtendimento status, Integer pagamento) {
		EntityManager entityManager = fabricaDeEntityManager.createEntityManager();
		boolean sucesso = false;

		Query update = entityManager.createQuery("UPDATE Atendimento SET Status = :status,Pagamento = :pagamento where Id = :id");
		
		update.setParameter("status", status.getCodigo());
		update.setParameter("id", id);
		update.setParameter("pagamento", pagamento);
		
		try {
			entityManager.getTransaction().begin();
			update.executeUpdate();
			entityManager.getTransaction().commit();
			sucesso = true;
		} catch (Exception exception) {
			entityManager.getTransaction().rollback();
		} finally {
			entityManager.close();
		}

		return sucesso;
	}
	
	public void exclua(Long id) {
		EntityManager entityManager = fabricaDeEntityManager.createEntityManager();
		Query update = entityManager.createQuery("UPDATE Atendimento SET Usuario = :usuario,Status = :status,Valor = :valor where Id = :id");
		Query delete = entityManager.createNativeQuery("DELETE FROM ATENDIMENTO_SERVICO WHERE IDATENDIMENTO = :id");

		update.setParameter("usuario", null);
		update.setParameter("id", id);
		update.setParameter("status", StatusAtendimento.LIVRE.getCodigo());
		update.setParameter("valor", null);
		delete.setParameter("id", id);

		try {
			entityManager.getTransaction().begin();
			update.executeUpdate();
			delete.executeUpdate();
			entityManager.getTransaction().commit();
		} catch (Exception exception) {
			entityManager.getTransaction().rollback();
		} finally {
			entityManager.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Servico> consulteServicosDoAtendimento(Long id) {
		EntityManager entityManager = fabricaDeEntityManager.createEntityManager();
		List<Servico> servicos = new ArrayList<Servico>();
		Query select = entityManager
				.createNativeQuery("SELECT SERVICO.* FROM ATENDIMENTO INNER JOIN ATENDIMENTO_SERVICO \r\n"
						+ "ON ATENDIMENTO.ID = ATENDIMENTO_SERVICO.IDATENDIMENTO INNER JOIN SERVICO \r\n"
						+ "ON ATENDIMENTO_SERVICO.IDSERVICO = SERVICO.ID " + "WHERE ATENDIMENTO.ID = :id");

		select.setParameter("id", id);

		try {
			List<Object[]> lista = select.getResultList();
			
			lista.forEach(x -> {
				Servico servico = new Servico();
				servico.setId(Long.parseLong(x[0].toString()));
				servico.setNome((String) x[1]);
				servico.setValor(Float.parseFloat(x[2].toString()));
				servicos.add(servico);
			});
		} catch (Exception exception) {
			entityManager.getTransaction().rollback();
		} finally {
			entityManager.close();
		}

		return servicos;
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
	
	public List<Atendimento> consulteTodosPorUsuario(Long idUsuario) {
		EntityManager entityManager = fabricaDeEntityManager.createEntityManager();

		TypedQuery<Atendimento> query = entityManager
				.createQuery("select at from Atendimento at where at.Usuario = :usuario OR at.Usuario IS NULL", Atendimento.class);

		query.setParameter("usuario", idUsuario);

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
