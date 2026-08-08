package co.com.franchise.usecase.higheststock;

import co.com.franchise.model.branch.gateways.BranchRepository;
import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.gateways.ProductRepository;
import reactor.core.publisher.Flux;

public class HigheststockUseCase {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public HigheststockUseCase(
            BranchRepository branchRepository,
            ProductRepository productRepository
    ) {
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
    }

    public Flux<Product> execute(String franchiseId) {
        return branchRepository.findByFranchiseId(franchiseId)
                .flatMap(branch -> productRepository.findByBranchId(branch.getId())
                .sort((p1, p2) -> Integer.compare(p2.getStock(), p1.getStock()))
                .next()
                );
    }
}
