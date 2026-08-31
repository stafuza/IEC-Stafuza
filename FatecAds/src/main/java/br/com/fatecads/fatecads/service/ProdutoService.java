package br.com.fatecads.fatecads.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import br.com.fatecads.fatecads.entity.Produto;
import br.com.fatecads.fatecads.repository.ProdutoRepository;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;

    //MÉTODO PARA SALVAR UM PRODUTO
    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto save(Produto produto, MultipartFile imagem) {
        if (produto.getIdProduto() != null) {
            Produto existente = produtoRepository.findById(produto.getIdProduto())
                    .orElseThrow(() -> new IllegalArgumentException("Produto nao encontrado."));
            produto.setImagemProduto(existente.getImagemProduto());
            produto.setTipoImagemProduto(existente.getTipoImagemProduto());
        }

        if (imagem != null && !imagem.isEmpty()) {
            validarImagem(imagem);
            try {
                produto.setImagemProduto(imagem.getBytes());
                produto.setTipoImagemProduto(imagem.getContentType());
            } catch (java.io.IOException e) {
                throw new IllegalArgumentException("Nao foi possivel ler a imagem enviada.", e);
            }
        }

        return produtoRepository.save(produto);
    }

    private void validarImagem(MultipartFile imagem) {
        if (imagem.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("A imagem deve ter no maximo 5 MB.");
        }

        String tipo = imagem.getContentType();
        if (tipo == null || !(tipo.equals("image/jpeg") || tipo.equals("image/png")
                || tipo.equals("image/webp"))) {
            throw new IllegalArgumentException("Envie uma imagem JPG, PNG ou WebP.");
        }
    }

    //MÉTODO PARA LISTAR TODOS OS PRODUTOS
    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    //MÉTODO PARA BUSCAR UM PRODUTO
    public Produto findById(Integer id) {
        return produtoRepository.findById(id).orElse(null);
    }

    //MÉTODO PARA DELETAR UM PRODUTO
    public void deleteById(Integer id) {
        produtoRepository.deleteById(id);
    }
}
