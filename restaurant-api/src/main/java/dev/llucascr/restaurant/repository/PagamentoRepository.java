package dev.llucascr.restaurant.repository;

import dev.llucascr.restaurant.domain.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
