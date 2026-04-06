package br.com.fatecads.fatecads.repository;

import br.com.fatecads.fatecads.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {
    
}