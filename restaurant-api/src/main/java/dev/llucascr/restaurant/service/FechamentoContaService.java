package dev.llucascr.restaurant.service;

import dev.llucascr.restaurant.domain.entity.FechamentoConta;
import dev.llucascr.restaurant.domain.entity.Pedido;
import dev.llucascr.restaurant.domain.entity.PedidoItem;
import dev.llucascr.restaurant.domain.enums.StatusItemPedido;
import dev.llucascr.restaurant.domain.enums.StatusPedido;
import dev.llucascr.restaurant.dto.FechamentoContaRequest;
import dev.llucascr.restaurant.dto.FechamentoContaResponse;
import dev.llucascr.restaurant.exception.RegraNegocioException;
import dev.llucascr.restaurant.repository.FechamentoContaRepository;
import dev.llucascr.restaurant.repository.PedidoItemRepository;
import dev.llucascr.restaurant.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FechamentoContaService {

    private final FechamentoContaRepository fechamentoContaRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoItemRepository pedidoItemRepository;

    public FechamentoContaService(FechamentoContaRepository fechamentoContaRepository, PedidoRepository pedidoRepository, PedidoItemRepository pedidoItemRepository) {
        this.fechamentoContaRepository = fechamentoContaRepository;
        this.pedidoRepository = pedidoRepository;
        this.pedidoItemRepository = pedidoItemRepository;
    }

    public FechamentoContaResponse fecharConta(Long pedidoId, FechamentoContaRequest request) {
        Pedido pedido = buscarPedidoPorId(pedidoId);

        if (pedido.getStatus() == StatusPedido.FECHADO) {
            throw new RegraNegocioException("Pedido já está fechado");
        }

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new RegraNegocioException("Pedido cancelado não pode ser fechado");
        }

        if (fechamentoContaRepository.existsByPedidoId(pedidoId)) {
            throw new RegraNegocioException("Já existe fechamento para este pedido");
        }

        List<PedidoItem> itens = pedidoItemRepository.findByPedidoId(pedidoId);
        if (itens.isEmpty()) {
            throw new RegraNegocioException("Não é possível fechar uma conta sem itens");
        }

        List<PedidoItem> itensNaoEntregues = pedidoItemRepository
                .findByPedidoIdAndStatusNot(pedidoId, StatusItemPedido.ENTREGUE);
        if (!itensNaoEntregues.isEmpty()) {
            throw new RegraNegocioException("Todos os itens precisam estar entregues para fechar a conta");
        }

        BigDecimal subTotal = itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxaServico = request.taxaServico() != null ?  request.taxaServico() : BigDecimal.ZERO;
        BigDecimal desconto = request.desconto() != null ?  request.desconto() : BigDecimal.ZERO;

        if (taxaServico.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("Taxa de servico não pode ser negativa");
        }

        if (desconto.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("Desconto não pode ser negativa");
        }

        BigDecimal total = subTotal.add(taxaServico.subtract(desconto));

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("Total da conta não pode ser negativa");
        }

        FechamentoConta fechamento = new FechamentoConta();
        fechamento.setPedido(pedido);
        fechamento.setSubtotal(subTotal);
        fechamento.setTaxaServico(taxaServico);
        fechamento.setDesconto(desconto);
        fechamento.setTotal(total);

        pedido.setStatus(StatusPedido.FECHADO);
        pedido.setDataFechamento(LocalDateTime.now());

        FechamentoConta fechamentoSalvo = fechamentoContaRepository.save(fechamento);
        pedidoRepository.save(pedido);

        return FechamentoContaResponse.fromEntity(fechamentoSalvo);
    }

    public FechamentoContaResponse buscarPorPedido(Long pedidoId) {
        FechamentoConta fechamento = fechamentoContaRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RegraNegocioException("Fechamento não encontrado"));
        return  FechamentoContaResponse.fromEntity(fechamento);
    }

    private Pedido buscarPedidoPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RegraNegocioException("Pedido não encontrado"));
    }


}
