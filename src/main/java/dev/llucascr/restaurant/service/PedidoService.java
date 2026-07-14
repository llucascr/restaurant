package dev.llucascr.restaurant.service;

import dev.llucascr.restaurant.domain.entity.Mesa;
import dev.llucascr.restaurant.domain.entity.Pedido;
import dev.llucascr.restaurant.domain.enums.StatusMesa;
import dev.llucascr.restaurant.domain.enums.StatusPedido;
import dev.llucascr.restaurant.dto.PedidoRequest;
import dev.llucascr.restaurant.dto.PedidoResponse;
import dev.llucascr.restaurant.exception.RegraNegocioException;
import dev.llucascr.restaurant.repository.MesaRepository;
import dev.llucascr.restaurant.repository.PedidoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final MesaRepository mesaRepository;

    public PedidoService(PedidoRepository pedidoRepository, MesaRepository mesaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.mesaRepository = mesaRepository;
    }

    public PedidoResponse abrirPedido(PedidoRequest request) {
        Mesa mesa = mesaRepository.findById(request.mesaId())
                .orElseThrow(() -> new RegraNegocioException("Mesa inexistente"));

        if (mesa.getStatus() != StatusMesa.LIVRE) {
            throw new RegraNegocioException("Mesa não está livre para abertura de pedido");
        }

        Pedido pedido = new Pedido();
        pedido.setMesa(mesa);
        pedido.setStatus(StatusPedido.ABERTO);
        pedido.setObservacao(request.observacao());

        mesa.setStatus(StatusMesa.OCUPADA);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        mesaRepository.save(mesa);

        return PedidoResponse.fromEntity(pedidoSalvo);
    }

    public Page<PedidoResponse> listar(Pageable pageable) {
        return pedidoRepository.findAll(pageable).map(PedidoResponse::fromEntity);
    }

    public PedidoResponse buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Pedido não encontrado"));
        return PedidoResponse.fromEntity(pedido);
    }
}
