package co.com.franchise.usecase.updatebranch;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.branch.gateways.BranchRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

//@RequiredArgsConstructor
public class UpdatebranchUseCase {
    private final BranchRepository branchRepository;

    public UpdatebranchUseCase(BranchRepository branchRepository){
        this.branchRepository = branchRepository;
    }

    public Mono<Branch> execute(String id, Branch branch){
        if (branch.getName() == null || branch.getName().isBlank()) {
            return Mono.error(new IllegalArgumentException(
                            "Branch name cannot be null or blank."
                    )
            );
        }

        return branchRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new IllegalArgumentException("Branch not found."))
                )
                .flatMap(existingBranch -> {
                    existingBranch.setName(branch.getName());

                    return branchRepository.save(existingBranch);
                });
    }
}
