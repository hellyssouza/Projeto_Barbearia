package com.projetofinal.Barbearia.repositorio;

import org.springframework.data.repository.CrudRepository;
import com.projetofinal.Barbearia.negocio.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
}
