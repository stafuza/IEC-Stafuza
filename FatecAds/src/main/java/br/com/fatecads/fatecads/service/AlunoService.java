package br.com.fatecads.fatecads.service;

import br.com.fatecads.fatecads.entity.Aluno;
import br.com.fatecads.fatecads.repository.AlunoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlunoService {
    
    //Injeção de depedencia do repositório de alunos
    @Autowired
    private AlunoRepository alunoRepository;

    //Método para salvar um novo aluno
    public Aluno save(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    //Método para listar todos os alunos
    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    //Método para excluir um aluno por ID
    public void deleteById(Integer id) {
        alunoRepository.deleteById(id);
    }

    //Método para buscar um aluno por ID
    public Aluno findById(Integer id) {
        return alunoRepository.findById(id).orElse(null);
    }

}