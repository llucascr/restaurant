package dev.llucascr.restaurant.repository;

import dev.llucascr.restaurant.domain.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
}
