package br.com.fatecads.fatecads.repository;

import br.com.fatecads.fatecads.entity.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByLoginUsuario(String loginUsuario);

    Optional<Usuario> findByEmailUsuario(String emailUsuario);

    List<Usuario> findTop5ByOrderByIdUsuarioDesc();
    
}