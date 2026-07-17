package dev.llucascr.restaurant.dto;

import java.math.BigDecimal;

public record FechamentoContaRequest(
        BigDecimal taxaServico,
        BigDecimal desconto
) {
}
