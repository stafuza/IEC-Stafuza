package br.com.fatecads.fatecads.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.fatecads.fatecads.entity.PasswordResetToken;
import br.com.fatecads.fatecads.repository.AlunoRepository;
import br.com.fatecads.fatecads.repository.CursoRepository;
import br.com.fatecads.fatecads.repository.DisciplinaRepository;
import br.com.fatecads.fatecads.repository.ProfessorRepository;
import br.com.fatecads.fatecads.repository.UsuarioRepository;
import br.com.fatecads.fatecads.service.MailService;
import br.com.fatecads.fatecads.service.PasswordResetService;

@Controller
public class LoginController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Value("${mail.mode:console}")
    private String mailMode;

    @Value("${app.base-url:}")
    private String appBaseUrl;
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("usuariosTotal", usuarioRepository.count());
        model.addAttribute("alunosTotal", alunoRepository.count());
        model.addAttribute("cursosTotal", cursoRepository.count());
        model.addAttribute("professoresTotal", professorRepository.count());
        model.addAttribute("disciplinasTotal", disciplinaRepository.count());
        model.addAttribute("disciplinasSemProfessor", disciplinaRepository.countByProfessorIsNull());
        model.addAttribute("alunosSemCurso", alunoRepository.countByCursoIsNull());
        model.addAttribute("ultimosUsuarios", usuarioRepository.findTop5ByOrderByIdUsuarioDesc());
        model.addAttribute("ultimosAlunos", alunoRepository.findTop5ByOrderByIdAlunoDesc());
        model.addAttribute("ultimosCursos", cursoRepository.findTop5ByOrderByIdCursoDesc());
        model.addAttribute("ultimosProfessores", professorRepository.findTop5ByOrderByIdProfessorDesc());
        model.addAttribute("ultimasDisciplinas", disciplinaRepository.findTop5ByOrderByIdDisciplinaDesc());
        return "home";
    }

    @GetMapping("/esqueci-senha")
    public String esqueciSenha() {
        return "esqueciSenha";
    }

    @PostMapping("/esqueci-senha")
    public String enviarReset(@RequestParam("email") String email, Model model, HttpServletRequest request) {
        String message = "Se o email estiver cadastrado, enviaremos um link para redefinir a senha.";
        model.addAttribute("message", message);

        passwordResetService.createTokenForEmail(email).ifPresent(token -> {
            String resetLink = buildResetLink(request, token);
            mailService.sendPasswordReset(email, resetLink);
            if ("console".equalsIgnoreCase(mailMode)) {
                model.addAttribute("resetLink", resetLink);
            }
        });

        return "esqueciSenha";
    }

    @GetMapping("/redefinir-senha")
    public String redefinirSenha(@RequestParam(value = "token", required = false) String token, Model model) {
        if (token == null || token.isBlank()) {
            model.addAttribute("tokenValid", false);
            model.addAttribute("errorMessage", "Token nao informado.");
            return "redefinirSenha";
        }

        boolean tokenValid = passwordResetService.findValidToken(token).isPresent();
        model.addAttribute("token", token);
        model.addAttribute("tokenValid", tokenValid);
        if (!tokenValid) {
            model.addAttribute("errorMessage", "Token invalido ou expirado.");
        }
        return "redefinirSenha";
    }

    @PostMapping("/redefinir-senha")
    public String salvarNovaSenha(
            @RequestParam("token") String token,
            @RequestParam("senha") String senha,
            @RequestParam("confirmacao") String confirmacao,
            Model model) {
        if (senha == null || senha.isBlank() || !senha.equals(confirmacao)) {
            model.addAttribute("token", token);
            model.addAttribute("tokenValid", true);
            model.addAttribute("errorMessage", "As senhas nao conferem.");
            return "redefinirSenha";
        }

        boolean resetOk = passwordResetService.resetPassword(token, senha);
        if (!resetOk) {
            model.addAttribute("tokenValid", false);
            model.addAttribute("errorMessage", "Token invalido ou expirado.");
            return "redefinirSenha";
        }

        model.addAttribute("resetOk", true);
        model.addAttribute("tokenValid", false);
        return "redefinirSenha";
    }

    private String buildResetLink(HttpServletRequest request, PasswordResetToken token) {
        String baseUrl = appBaseUrl != null && !appBaseUrl.isBlank()
                ? appBaseUrl
                : request.getRequestURL().toString().replace("/esqueci-senha", "");
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl + "/redefinir-senha?token=" + token.getToken();
    }

}
