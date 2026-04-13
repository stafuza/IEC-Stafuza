package br.com.fatecads.fatecads.service;

import br.com.fatecads.fatecads.entity.Disciplina;
import br.com.fatecads.fatecads.repository.DisciplinaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisciplinaService {
    
    //Injeção de depedencia do repositório de disciplinas
    @Autowired
    private DisciplinaRepository disciplinaRepository;

    //Método para salvar uma nova disciplina
    public Disciplina save(Disciplina disciplina) {
        return disciplinaRepository.save(disciplina);
    }

    //Método para listar todas as disciplinas
    public List<Disciplina> findAll() {
        return disciplinaRepository.findAll();
    }

    //Método para excluir uma disciplina por ID
    public void deleteById(Integer id) {
        disciplinaRepository.deleteById(id);
    }

    //Método para buscar uma disciplina por ID
    public Disciplina findById(Integer id) {
        return disciplinaRepository.findById(id).orElse(null);
    }

}