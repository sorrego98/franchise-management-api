package co.com.franchise.usecase.updatestock;

import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

//@RequiredArgsConstructor
public class UpdatestockUseCase {
    private final ProductRepository productRepository;

    public UpdatestockUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<Product> execute(String id, Integer stock){
        if (stock == null || stock < 0) {
            return Mono.error(new IllegalArgumentException(
                            "Product name cannot be null or negative."
                    )
            );
        }

        return productRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new IllegalArgumentException("Product not found."))
                )
                .flatMap(existingProduct ->{
                    existingProduct.setStock(stock);

                    return productRepository.save(existingProduct);
                });
    }
}
