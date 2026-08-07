package co.com.franchise.api;

import co.com.franchise.api.dto.*;
import co.com.franchise.api.mapper.BranchMapper;
import co.com.franchise.api.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import co.com.franchise.usecase.createfranchise.CreateFranchiseUseCase;
import co.com.franchise.usecase.updatefranchise.UpdateFranchiseUseCase;
import co.com.franchise.usecase.createbranch.CreatebranchUseCase;
import co.com.franchise.usecase.updatebranch.UpdatebranchUseCase;
import co.com.franchise.usecase.createproduct.CreateProductUseCase;
import co.com.franchise.usecase.updateproduct.UpdateproductUseCase;
import co.com.franchise.api.mapper.FranchiseMapper;

@Component
@RequiredArgsConstructor
public class Handler {
private final CreateFranchiseUseCase createFranchiseUseCase;
private final UpdateFranchiseUseCase updateFranchiseUseCase;
private final CreatebranchUseCase createbranchUseCase;
private final UpdatebranchUseCase updatebranchUseCase;
private final CreateProductUseCase createProductUseCase;
private final UpdateproductUseCase updateProductUseCase;

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
                .flatMap(updateFranchis ->
                        ServerResponse.ok().bodyValue(updateFranchis)
                );
    }

    public Mono<ServerResponse> createBranch(ServerRequest serverRequest ) {
        return serverRequest.bodyToMono(CreateBranchRequest.class)
                .map(BranchMapper::toDomain)
                .flatMap(createbranchUseCase::execute)
                .flatMap(branch -> ServerResponse.ok().bodyValue(branch));
    }

    public Mono<ServerResponse> updateBranch(ServerRequest serverRequest ) {
        String id = serverRequest.pathVariable("id");

        return serverRequest.bodyToMono(UpdateBranchRequest.class)
                .map(BranchMapper::toDomain)
                .flatMap(branch -> updatebranchUseCase.execute(id, branch))
                .flatMap(updateBranch -> ServerResponse.ok().bodyValue(updateBranch));
    }

    public Mono<ServerResponse> createProduct(ServerRequest serverRequest ) {
        return serverRequest.bodyToMono(CreateProducRequest.class)
                .map(ProductMapper::toDomain)
                .flatMap(createProductUseCase::execute)
                .flatMap(product-> ServerResponse.ok().bodyValue(product));
    }

    public Mono<ServerResponse> updateProduct(ServerRequest serverRequest ) {
        String id = serverRequest.pathVariable("id");

        return serverRequest.bodyToMono(UpdateProductRequest.class)
                .map(ProductMapper::toDomain)
                .flatMap(product -> updateProductUseCase.execute(id, product))
                .flatMap(updateProduct-> ServerResponse.ok().bodyValue(updateProduct));
    }
}