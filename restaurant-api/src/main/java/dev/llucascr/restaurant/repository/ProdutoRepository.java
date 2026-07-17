package dev.llucascr.restaurant.repository;

import dev.llucascr.restaurant.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
