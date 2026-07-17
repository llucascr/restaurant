package dev.llucascr.restaurant.repository;

import dev.llucascr.restaurant.domain.entity.PedidoItem;
import dev.llucascr.restaurant.domain.enums.StatusItemPedido;
import dev.llucascr.restaurant.domain.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {

    List<PedidoItem> findByPedidoId(Long pedidoId);

    List<PedidoItem> findByStatusOrderByIdAsc(StatusItemPedido status);

    List<PedidoItem> findByPedidoIdAndStatusNot(Long pedidoId, StatusItemPedido status);

    @Query(
            """
            SELECT i
            FROM PedidoItem i
            JOIN FETCH i.produto
            JOIN FETCH i.pedido p
            JOIN FETCH p.mesa
            WHERE i.status = :status
            ORDER BY i.id
            """
    )
    List<PedidoItem> buscarItemComProdutoEPedido(StatusItemPedido status);

}
