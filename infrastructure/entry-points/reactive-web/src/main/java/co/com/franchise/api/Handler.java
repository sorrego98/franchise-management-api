package co.com.franchise.api;

import co.com.franchise.api.dto.UpdateFranchiseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import co.com.franchise.usecase.createfranchise.CreateFranchiseUseCase;
import co.com.franchise.usecase.updatefranchise.UpdateFranchiseUseCase;
import co.com.franchise.api.dto.CreateFranchiseRequest;
import co.com.franchise.api.mapper.FranchiseMapper;

@Component
@RequiredArgsConstructor
public class Handler {
private final CreateFranchiseUseCase createFranchiseUseCase;
private final UpdateFranchiseUseCase updateFranchiseUseCase;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateFranchiseRequest.class)
                .map(FranchiseMapper::toDomain)
                .flatMap(createFranchiseUseCase::execute)
                .flatMap(franchise -> ServerResponse.ok().bodyValue(franchise));
    }

    public Mono<ServerResponse> listenPUTUseCase(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return serverRequest.bodyToMono(UpdateFranchiseRequest.class)
                .map(FranchiseMapper::toDomain)
                .flatMap(franchise -> updateFranchiseUseCase.execute(id, franchise))
                .flatMap(updateFranchises ->
                        ServerResponse.ok().bodyValue(updateFranchises)
                );
    }

}