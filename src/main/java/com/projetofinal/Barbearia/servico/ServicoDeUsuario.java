package com.projetofinal.Barbearia.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	public Usuario obtenhaUsuarioPeloId(Long id) {
		return repositorio.findById(id).get();
	}
	
	public Usuario obtenhaUsuarioLogado() {
		Authentication usuarioDoContexto = SecurityContextHolder.getContext().getAuthentication();
		
		Usuario usuario = new Usuario();
		
		usuario.setNome(usuarioDoContexto.getName());
		
		return usuario;
	}
	
	public boolean existe(Long id) {
		return repositorio.existsById(id);
	}

	public Long consulteIdUsuario(Long idFuncionario) {
		return repositorio.consulteIdDoUsuario(idFuncionario);
	}

	public Long consulteIdDoUsuario() {
		Usuario usuario = obtenhaUsuarioLogado();
		
		return repositorio.consulteIdDoUsuario(usuario.getNome());
	}

	public boolean usuarioLogadoEFuncionario() {
		Long idUsuario = consulteIdDoUsuario();
		
		return repositorio.usuarioLogadoEFuncionario(idUsuario);
	}
	
	public Long consulteIdDeFuncionario(Long idUsuario) {
		return repositorio.consulteIdDeFuncionario(idUsuario);
	}
	
	public List<Usuario> consulteTodos() {
		return (List<Usuario>) repositorio.consulteTodos();
	}
}
