package co.com.franchise.usecase.createproduct;

import co.com.franchise.model.branch.gateways.BranchRepository;
import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.gateways.ProductRepository;
import reactor.core.publisher.Mono;

//@RequiredArgsConstructor
public class CreateProductUseCase {
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    public CreateProductUseCase(
            ProductRepository productRepository,
            BranchRepository branchRepository
    ) {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
    }

    public Mono<Product> execute(Product product) {
        if (product.getName() == null || product.getName().isBlank()) {
            return Mono.error(new IllegalArgumentException(
                            "Product name cannot be null or blank."
                    )
            );
        }

        if (product.getStock() == null || product.getStock() < 0){
            return Mono.error(new IllegalArgumentException(
                    "Product stock cannot be null or negative."
            ));
        }

        if (product.getBranchId() == null || product.getBranchId().isBlank()) {
            return Mono.error(new IllegalArgumentException(
                            "Branch Id cannot be null or blank."
                    )
            );
        }

        return branchRepository.findById(product.getBranchId())
                .switchIfEmpty(
                        Mono.error(new IllegalArgumentException("Branch not found"))
                )
                .flatMap(branch -> productRepository.save(product));
    }
}
