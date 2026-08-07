package co.com.franchise.usecase.updateproduct;

import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

//@RequiredArgsConstructor
public class UpdateproductUseCase {
    private final ProductRepository productRepository;

    public UpdateproductUseCase(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Mono<Product> execute(String id, Product product){
        if (product.getName() == null || product.getName().isBlank()) {
            return Mono.error(new IllegalArgumentException(
                            "Product name cannot be null or blank."
                    )
            );
        }

        return productRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new IllegalArgumentException("Product not found."))
                )
                .flatMap(existingProduct ->{
                    existingProduct.setName(product.getName());

                    return productRepository.save(existingProduct);
                });
    }
}
