package com.projetofinal.Barbearia.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetofinal.Barbearia.enumerador.Permissao;
import com.projetofinal.Barbearia.negocio.Usuario;
import com.projetofinal.Barbearia.repositorio.RepositorioDeUsuarioImpl;

@Service
public class ServicoDeUsuario {
	@Autowired
	private RepositorioDeUsuarioImpl repositorio;

	public Usuario cadastre(Usuario usuario) {
		return repositorio.save(usuario);
	}

	public boolean atualize(Long id, Long idFuncionario, Permissao permissao) {
		return repositorio.atualize(id, idFuncionario, permissao);
	}

	public boolean existe(Long id) {
		return repositorio.existsById(id);
	}

	public Long consulteIdUsuario(Long idFuncionario) {
		return repositorio.consulteIdDoUsuario(idFuncionario);
	}

	public Long consulteIdPeloNome(String nome) {
		return repositorio.consulteIdPeloNome(nome);
	}

	public List<Usuario> consulteTodos() {
		return (List<Usuario>) repositorio.consulteTodos();
	}
}
