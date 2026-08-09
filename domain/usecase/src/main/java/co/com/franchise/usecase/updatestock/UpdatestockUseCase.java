package co.com.franchise.usecase.updatestock;

import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.gateways.ProductRepository;
import co.com.franchise.usecase.exception.ResourceNotFoundException;
import reactor.core.publisher.Mono;

public class UpdatestockUseCase {
    private final ProductRepository productRepository;

    public UpdatestockUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<Product> execute(String id, Integer stock){
        if (stock == null || stock < 0) {
            return Mono.error(new IllegalArgumentException(
                            "Stock cannot be null or negative."
                    )
            );
        }

        return productRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new ResourceNotFoundException("Product not found."))
                )
                .flatMap(existingProduct ->{
                    existingProduct.setStock(stock);

                    return productRepository.save(existingProduct);
                });
    }
}
