package br.com.fatecads.fatecads.controller;

import br.com.fatecads.fatecads.entity.Curso;
import br.com.fatecads.fatecads.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    // Injeção de dependência da service de cursos
    @Autowired
    private CursoService cursoService;

    // Método para salvar um novo curso
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Curso curso) {
        cursoService.save(curso);
        return "redirect:/cursos/listar";
    }

    // Método para listar todos os cursos
    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("cursos", cursoService.findAll());
        return "curso/listarCursos";
    }

    // Método para criar um novo curso e abrir o formulário de cadastro
    @GetMapping("/criar")
    public String criarForm(Model model) {
        model.addAttribute("curso", new Curso());
        return "curso/formularioCurso";
    }

    // Método para excluir um curso por ID
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        cursoService.deleteById(id);
        return "redirect:/cursos/listar";
    }

    // Método para editar um curso por ID
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Integer id, Model model) {
        Curso curso = cursoService.findById(id);
        model.addAttribute("curso", curso);
        return "curso/formularioCurso";
    }
}