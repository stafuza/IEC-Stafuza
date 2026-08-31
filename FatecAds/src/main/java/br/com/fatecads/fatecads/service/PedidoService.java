package br.com.fatecads.fatecads.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatecads.fatecads.entity.Aluno;
import br.com.fatecads.fatecads.entity.ItemDoPedido;
import br.com.fatecads.fatecads.entity.Pedido;
import br.com.fatecads.fatecads.entity.Produto;
import br.com.fatecads.fatecads.repository.AlunoRepository;
import br.com.fatecads.fatecads.repository.PedidoRepository;
import br.com.fatecads.fatecads.repository.ProdutoRepository;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    //MÉTODO PARA SALVAR UM PEDIDO
    @Transactional
    public Pedido salvarPedido(Pedido pedido) {
        if (pedido.getAluno() == null || pedido.getAluno().getIdAluno() == null) {
            throw new IllegalArgumentException("Informe um aluno para o pedido.");
        }
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("Inclua pelo menos um item no pedido.");
        }

        Aluno aluno = alunoRepository.findById(pedido.getAluno().getIdAluno())
                .orElseThrow(() -> new IllegalArgumentException("Aluno nao encontrado."));
        pedido.setAluno(aluno);
        pedido.setDataPedido(LocalDate.now());
        for (ItemDoPedido item : pedido.getItens()) {
            if (item.getProduto() == null || item.getProduto().getIdProduto() == null
                    || item.getQuantidade() == null || item.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Informe um produto e uma quantidade valida para cada item.");
            }
            Produto produto = produtoRepository.findById(item.getProduto()
                .getIdProduto()).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            item.setProduto(produto);
            item.setPreco(produto.getValorProduto());
            item.atualizarSubtotal();
            item.setPedido(pedido);
        }
        pedido.atualizarTotal();
        return pedidoRepository.save(pedido);
    }

    //MÉTODO PARA BUSCAR UM PEDIDO
    public Pedido findById(Integer id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    //MÉTODO PARA DELETAR UM PEDIDO
    public void deleteById(Integer id) {
        pedidoRepository.deleteById(id);
    }
}
