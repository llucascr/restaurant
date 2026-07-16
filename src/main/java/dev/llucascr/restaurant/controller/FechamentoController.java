package dev.llucascr.restaurant.controller;

import dev.llucascr.restaurant.dto.FechamentoContaRequest;
import dev.llucascr.restaurant.dto.FechamentoContaResponse;
import dev.llucascr.restaurant.service.FechamentoContaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos/{pedidoId}/fechamento")
public class FechamentoController {

    private final FechamentoContaService fechamentoContaService;

    public FechamentoController(FechamentoContaService fechamentoContaService) {
        this.fechamentoContaService = fechamentoContaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FechamentoContaResponse fecharConta(@PathVariable Long pedidoId, @RequestBody FechamentoContaRequest request) {
        return fechamentoContaService.fecharConta(pedidoId, request);
    }

    @GetMapping
    public FechamentoContaResponse fecharConta(@PathVariable Long pedidoId) {
        return fechamentoContaService.buscarPorPedido(pedidoId);
    }

}
