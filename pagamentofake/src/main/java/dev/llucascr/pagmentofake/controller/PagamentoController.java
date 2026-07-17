package dev.llucascr.pagmentofake.controller;

import dev.llucascr.pagmentofake.dto.PagamentoRequest;
import dev.llucascr.pagmentofake.dto.PagamentoResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @PostMapping("/processar")
    public PagamentoResponse processar(@RequestBody PagamentoRequest request) {
        return new PagamentoResponse(
                "APROVADO",
                UUID.randomUUID().toString()
        );
    }

}
