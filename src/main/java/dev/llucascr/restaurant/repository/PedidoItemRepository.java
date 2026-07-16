package dev.llucascr.restaurant.repository;

import dev.llucascr.restaurant.domain.entity.PedidoItem;
import dev.llucascr.restaurant.domain.enums.StatusItemPedido;
import dev.llucascr.restaurant.domain.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {

    List<PedidoItem> findByPedidoId(Long pedidoId);

    List<PedidoItem> findByStatusOrderByIdAsc(StatusItemPedido status);

}
