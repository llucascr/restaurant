package dev.llucascr.restaurant.controller;

import dev.llucascr.restaurant.dto.CozinhaItemResponse;
import dev.llucascr.restaurant.service.CozinhaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cozinha")
public class CozinhaController {

    private final CozinhaService cozinhaService;

    public CozinhaController(CozinhaService cozinhaService) {
        this.cozinhaService = cozinhaService;
    }

    @GetMapping("/itens-pendentes")
    public List<CozinhaItemResponse> listaItensPendentes() {
        return cozinhaService.listaItensPendentes();
    }

    @GetMapping("/itens-em-preparo")
    public List<CozinhaItemResponse> listarItensEmPreparo() {
        return cozinhaService.listaItensEmPreparo();
    }

    @PatchMapping("/itens/{itemId}/iniciar-preparo")
    public CozinhaItemResponse iniciarPreparo(@PathVariable Long itemId) {
        return cozinhaService.iniciarPreparo(itemId);
    }

    @PatchMapping("/itens/{itemId}/marcar-pronto")
    public CozinhaItemResponse marcarComoPronto(@PathVariable Long itemId) {
        return cozinhaService.marcarComoPronto(itemId);
    }

    @PatchMapping("/itens/{itemId}/entregar")
    public CozinhaItemResponse entregarItem(@PathVariable Long itemId) {
        return cozinhaService.entregarItem(itemId);
    }



}
