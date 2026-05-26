package br.com.fatecads.fatecads.service;

import br.com.fatecads.fatecads.entity.Usuario;
import br.com.fatecads.fatecads.repository.UsuarioRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    
    //Injeção de depedencia do repositório de usuários
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Método para salvar um novo usuário
    public Usuario save(Usuario usuario) {
        usuario.setSenhaUsuario(passwordEncoder.encode(usuario.getSenhaUsuario()));
        return usuarioRepository.save(usuario);
    }

    //Método para listar todos os usuários
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    //Método para excluir um usuário por ID
    public void deleteById(Integer id) {
        usuarioRepository.deleteById(id);
    }

    //Método para buscar um usuário por ID
    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmailUsuario(email).orElse(null);
    }

    public void updatePassword(Usuario usuario, String rawPassword) {
        usuario.setSenhaUsuario(passwordEncoder.encode(rawPassword));
        usuarioRepository.save(usuario);
    }

}