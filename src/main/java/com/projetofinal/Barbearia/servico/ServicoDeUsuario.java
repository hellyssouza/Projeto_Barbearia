package com.projetofinal.Barbearia.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projetofinal.Barbearia.negocio.Usuario;
import com.projetofinal.Barbearia.repositorio.RepositorioDeUsuario;

@Service
public class ServicoDeUsuario {
	@Autowired
	private RepositorioDeUsuario repositorio;
	
	public Usuario cadastre(Usuario usuario) {
		return repositorio.save(usuario);
	}
	
	public boolean existe(Long id) {
		return repositorio.existsById(id);
	}
}
