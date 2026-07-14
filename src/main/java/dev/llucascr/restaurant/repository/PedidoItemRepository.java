package dev.llucascr.restaurant.repository;

import dev.llucascr.restaurant.domain.entity.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
}
