package co.com.franchise.usecase.createbranch;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.branch.gateways.BranchRepository;
import co.com.franchise.model.franchise.gateways.FranchiseRepository;
import co.com.franchise.usecase.exception.ResourceNotFoundException;
import reactor.core.publisher.Mono;

public class CreatebranchUseCase {
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public CreatebranchUseCase(
            BranchRepository branchRepository,
            FranchiseRepository franchiseRepository
            ) {
        this.branchRepository = branchRepository;
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<Branch> execute(Branch branch) {
        if (branch.getName() == null || branch.getName().isBlank()) {
            return Mono.error(new IllegalArgumentException(
                    "Branch name cannot be null or blank."
                    )
            );
        }

        if (branch.getFranchiseId() == null || branch.getFranchiseId().isBlank()) {
            return Mono.error(new IllegalArgumentException(
                            "Franchise Id cannot be null or blank."
                    )
            );
        }

        return franchiseRepository.findById(branch.getFranchiseId())
                .switchIfEmpty(
                        Mono.error(new ResourceNotFoundException("Franchise nor found."))
                )
                .flatMap(franchise -> branchRepository.save(branch));
    }
}
