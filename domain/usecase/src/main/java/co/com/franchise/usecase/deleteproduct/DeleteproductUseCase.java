package co.com.franchise.usecase.deleteproduct;

import co.com.franchise.model.product.gateways.ProductRepository;
import co.com.franchise.usecase.exception.ResourceNotFoundException;
import reactor.core.publisher.Mono;

public class DeleteproductUseCase {
    private final ProductRepository productRepository;

    public DeleteproductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<Void> execute(String id){
        return productRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new ResourceNotFoundException("Product not found."))
                )
                .flatMap(product -> productRepository.deleteById(id));
    }
}
