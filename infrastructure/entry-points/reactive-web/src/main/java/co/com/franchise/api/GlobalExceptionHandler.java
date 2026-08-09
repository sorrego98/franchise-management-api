package co.com.franchise.api;

import co.com.franchise.usecase.exception.ResourceNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Order(-1)
public class GlobalExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(
            ServerWebExchange exchange,
            Throwable ex) {
        HttpStatus status;

        if(ex instanceof ResourceNotFoundException){
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String body = """
                {
                    "status": %d,
                    "error": %s
                }
                """.formatted(
                        status.value(),
                        ex.getMessage()
        );

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);

        return exchange.getResponse()
                .writeWith(
                        Mono.just(
                                exchange.getResponse()
                                        .bufferFactory()
                                        .wrap(bytes)
                        )
                );
    }
}
