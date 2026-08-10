package br.com.fatecads.fatecads.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import br.com.fatecads.fatecads.entity.ItemDoPedido;
import br.com.fatecads.fatecads.entity.Pedido;
import br.com.fatecads.fatecads.entity.Produto;
import br.com.fatecads.fatecads.repository.PedidoRepository;
import br.com.fatecads.fatecads.repository.ProdutoRepository;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    //MÉTODO PARA SALVAR UM PEDIDO
    public Pedido salvarPedido(Pedido pedido) {
        pedido.setDataPedido(LocalDate.now());
        for (ItemDoPedido item : pedido.getItens()) {
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