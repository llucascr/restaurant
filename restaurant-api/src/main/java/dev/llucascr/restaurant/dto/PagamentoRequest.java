package dev.llucascr.restaurant.dto;

import java.math.BigDecimal;

public record PagamentoRequest(
        BigDecimal valor,
        String formaPagamento
) {
}
