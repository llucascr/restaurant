package dev.llucascr.restaurant.repository;

import dev.llucascr.restaurant.domain.entity.CategoriaProduto;
import dev.llucascr.restaurant.domain.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {
}
