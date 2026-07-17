package dev.llucascr.pagmentofake.dto;

import java.math.BigDecimal;

public record PagamentoRequest(
        BigDecimal valor,
        String formaPagamento
) {
}
