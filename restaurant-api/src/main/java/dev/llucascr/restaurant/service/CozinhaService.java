package dev.llucascr.restaurant.service;

import dev.llucascr.restaurant.domain.entity.PedidoItem;
import dev.llucascr.restaurant.domain.enums.StatusItemPedido;
import dev.llucascr.restaurant.dto.CozinhaItemResponse;
import dev.llucascr.restaurant.exception.RegraNegocioException;
import dev.llucascr.restaurant.repository.PedidoItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CozinhaService {

    private final PedidoItemRepository pedidoItemRepository;

    public CozinhaService(PedidoItemRepository pedidoItemRepository) {
        this.pedidoItemRepository = pedidoItemRepository;
    }

    public List<CozinhaItemResponse> listaItensPendentes() {
        return pedidoItemRepository.findByStatusOrderByIdAsc(StatusItemPedido.PENDENTE)
                .stream()
                .map(CozinhaItemResponse::fromEntity)
                .toList();
    }

    public List<CozinhaItemResponse> listaItensEmPreparo() {
        return pedidoItemRepository.findByStatusOrderByIdAsc(StatusItemPedido.EM_PREPARO)
                .stream()
                .map(CozinhaItemResponse::fromEntity)
                .toList();
    }

    public CozinhaItemResponse iniciarPreparo(Long itemId) {
        PedidoItem item = buscarItemPorId(itemId);

        if (item.getStatus() != StatusItemPedido.PENDENTE) {
            throw new RegraNegocioException("Somente itens PENDENTES podem ir para EM_PREPARO");
        }

        item.setStatus(StatusItemPedido.EM_PREPARO);
        item.setDataInicioPreparo(LocalDateTime.now());

        return CozinhaItemResponse.fromEntity(pedidoItemRepository.save(item));
    }

    public CozinhaItemResponse marcarComoPronto(Long itemId) {
        PedidoItem item = buscarItemPorId(itemId);

        if (item.getStatus() != StatusItemPedido.EM_PREPARO) {
            throw new RegraNegocioException("Somente itens EM_PREPARO podem ir para PRONTO");
        }

        item.setStatus(StatusItemPedido.PRONTO);
        item.setDataPronto(LocalDateTime.now());

        return  CozinhaItemResponse.fromEntity(pedidoItemRepository.save(item));
    }

    public CozinhaItemResponse entregarItem(Long itemId) {
        PedidoItem item = buscarItemPorId(itemId);

        if (item.getStatus() != StatusItemPedido.PRONTO) {
            throw new RegraNegocioException("Somente itens PRONTO podem ir para ENTREGUE");
        }

        item.setStatus(StatusItemPedido.ENTREGUE);
        item.setDataEntrega(LocalDateTime.now());

        return  CozinhaItemResponse.fromEntity(pedidoItemRepository.save(item));
    }

    private PedidoItem buscarItemPorId(Long itemId) {
        return pedidoItemRepository.findById(itemId)
                .orElseThrow(() -> new RegraNegocioException("Item do pedido não encontrado"));
    }



}
