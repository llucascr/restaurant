package dev.llucascr.restaurant.dto;

public record PedidoItemRequest(
    Long produtoId,
    Integer quantidade,
    String observacao
) {
}
