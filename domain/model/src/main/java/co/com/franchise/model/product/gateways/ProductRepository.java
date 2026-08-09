package co.com.franchise.model.product.gateways;

import co.com.franchise.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> save(Product product);
    Mono<Product> findById(String id);
    Mono<Void> deleteById(String id);
    Flux<Product> findByBranchId(String branchId);
}
