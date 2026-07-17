package dev.llucascr.restaurant.client;

import dev.llucascr.restaurant.dto.PagamentoRequest;
import dev.llucascr.restaurant.dto.PagamentoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pagamento-client", url = "${pagamento.api.url}")
public interface PagamentoClient {

    @PostMapping("/pagamentos/processar")
    PagamentoResponse processar(@RequestBody PagamentoRequest request);

}
