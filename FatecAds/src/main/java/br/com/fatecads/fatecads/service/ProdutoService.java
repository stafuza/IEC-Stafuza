package br.com.fatecads.fatecads.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    