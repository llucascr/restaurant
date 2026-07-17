package dev.llucascr.restaurant.service;

import dev.llucascr.restaurant.client.PagamentoClient;
import dev.llucascr.restaurant.domain.entity.FechamentoConta;
import dev.llucascr.restaurant.domain.entity.Mesa;
import dev.llucascr.restaurant.domain.entity.Pagamento;
import dev.llucascr.restaurant.domain.entity.Pedido;
import dev.llucascr.restaurant.domain.enums.FormaPagamento;
import dev.llucascr.restaurant.domain.enums.StatusMesa;
import dev.llucascr.restaurant.domain.enums.StatusPagamento;
import dev.llucascr.restaurant.domain.enums.StatusPedido;
import dev.llucascr.restaurant.dto.PagamentoRequest;
import dev.llucascr.restaurant.dto.PagamentoResponse;
import dev.llucascr.restaurant.exception.RegraNegocioException;
import dev.llucascr.restaurant.repository.FechamentoContaRepository;
import dev.llucascr.restaurant.repository.MesaRepository;
import dev.llucascr.restaurant.repository.PagamentoRepository;
import dev.llucascr.restaurant.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

    private final PagamentoClient pagamentoClient;
    private final FechamentoContaRepository fechamentoContaRepository;
    private final PedidoRepository pedidoRepository;
    private final MesaRepository mesaRepository;
    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoClient pagamentoClient, FechamentoContaRepository fechamentoContaRepository,
                            PedidoRepository pedidoRepository, MesaRepository mesaRepository,
                            PagamentoRepository pagamentoRepository) {
        this.pagamentoClient = pagamentoClient;
        this.fechamentoContaRepository = fechamentoContaRepository;
        this.pedidoRepository = pedidoRepository;
        this.mesaRepository = mesaRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    @Transactional
    public void pagar(Long pedidoId, String formaPagamento) {

        FechamentoConta fechamento = fechamentoContaRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RegraNegocioException("Conta não encontrada"));

        PagamentoResponse response = pagamentoClient.processar(
                new PagamentoRequest(
                        fechamento.getTotal(),
                        formaPagamento
                )
        );

        if ("APROVADO".equals(response.status())) {

            Pedido pedido = fechamento.getPedido();
            pedido.setStatus(StatusPedido.FECHADO);

            Mesa mesa = pedido.getMesa();
            mesa.setStatus(StatusMesa.LIVRE);

            Pagamento pagamento = new Pagamento();
            pagamento.setPedido(pedido);
            pagamento.setFormaPagamento(FormaPagamento.valueOf(formaPagamento));
            pagamento.setStatus(StatusPagamento.APROVADO);
            pagamento.setValor(fechamento.getTotal());
            pagamento.setDataPagamento(fechamento.getDataFechamento());

            pedidoRepository.save(pedido);
            mesaRepository.save(mesa);
            pagamentoRepository.save(pagamento);
        }

    }

}
