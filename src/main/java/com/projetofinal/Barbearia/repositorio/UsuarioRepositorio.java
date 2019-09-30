package com.projetofinal.Barbearia.repositorio;

import org.springframework.data.repository.CrudRepository;

import com.projetofinal.Barbearia.negocio.Usuario;

public interface UsuarioRepositorio extends CrudRepository<Usuario, Long> {
}
