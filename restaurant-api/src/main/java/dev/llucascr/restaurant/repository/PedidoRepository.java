package dev.llucascr.restaurant.repository;

import dev.llucascr.restaurant.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
