package br.com.fatecads.fatecads.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Controller;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.fatecads.fatecads.entity.Produto;
import br.com.fatecads.fatecads.service.ProdutoService;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    //MÉTODO PARA LISTAR UM PRODUTO
    @GetMapping("/listar")
    public String listar(Model model) {
        List<Produto> produtos = produtoService.findAll();
        model.addAttribute("produtos", produtos);
        return "produto/listarProdutos";
    }

    //MÉTODO PARA ABRIR O FORMULÁRIO DE CRIAÇÃO DE UM NOVO PRODUTO
    @GetMapping("/criar")
    public String criarForm(Model model) {
        model.addAttribute("produto", new Produto());
        return "produto/formularioProduto";
    }
    //MÉTODO PARA SALVAR UM NOVO PRODUTO
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Produto produto,
            @RequestParam(name = "imagem", required = false) MultipartFile imagem) {
        produtoService.save(produto, imagem);
        return "redirect:/produtos/listar";
    }

    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> exibirImagem(@PathVariable Integer id) {
        Produto produto = produtoService.findById(id);
        if (produto == null || produto.getImagemProduto() == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType tipo = produto.getTipoImagemProduto() == null
                ? MediaType.APPLICATION_OCTET_STREAM
                : MediaType.parseMediaType(produto.getTipoImagemProduto());
        return ResponseEntity.ok()
                .contentType(tipo)
                .cacheControl(CacheControl.noCache())
                .body(produto.getImagemProduto());
    }

    //MÉTODO PARA ABRIR O FORMULÁRIO DE EDIÇÃO DE UM PRODUTO
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Integer id, Model model) {
        Produto produto = produtoService.findById(id);
        model.addAttribute("produto", produto);
        return "produto/formularioProduto";
    }

    //MÉTODO PARA EXCLUIR UM PRODUTO
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        produtoService.deleteById(id);
        return "redirect:/produtos/listar";
    }
}
