package co.com.franchise.api;

import co.com.franchise.api.dto.CreateBranchRequest;
import co.com.franchise.api.mapper.BranchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import co.com.franchise.usecase.createfranchise.CreateFranchiseUseCase;
import co.com.franchise.usecase.updatefranchise.UpdateFranchiseUseCase;
import co.com.franchise.usecase.createbranch.CreatebranchUseCase;
import co.com.franchise.api.dto.CreateFranchiseRequest;
import co.com.franchise.api.dto.UpdateFranchiseRequest;
import co.com.franchise.api.mapper.FranchiseMapper;

@Component
@RequiredArgsConstructor
public class Handler {
private final CreateFranchiseUseCase createFranchiseUseCase;
private final UpdateFranchiseUseCase updateFranchiseUseCase;
private final CreatebranchUseCase createbranchUseCase;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> createFranchise(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateFranchiseRequest.class)
                .map(FranchiseMapper::toDomain)
                .flatMap(createFranchiseUseCase::execute)
                .flatMap(franchise -> ServerResponse.ok().bodyValue(franchise));
    }

    public Mono<ServerResponse> updateFranchise(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return serverRequest.bodyToMono(UpdateFranchiseRequest.class)
                .map(FranchiseMapper::toDomain)
                .flatMap(franchise -> updateFranchiseUseCase.execute(id, franchise))
                .flatMap(updateFranchises ->
                        ServerResponse.ok().bodyValue(updateFranchises)
                );
    }

    public Mono<ServerResponse> createBranch(ServerRequest serverRequest ) {
        return serverRequest.bodyToMono(CreateBranchRequest.class)
                .map(BranchMapper::toDomain)
                .flatMap(createbranchUseCase::execute)
                .flatMap(branch -> ServerResponse.ok().bodyValue(branch));
    }

}