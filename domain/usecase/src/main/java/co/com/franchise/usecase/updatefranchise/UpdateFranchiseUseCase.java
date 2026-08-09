package co.com.franchise.usecase.updatefranchise;

import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.gateways.FranchiseRepository;
import co.com.franchise.usecase.exception.ResourceNotFoundException;
import reactor.core.publisher.Mono;

public class UpdateFranchiseUseCase {
    private final FranchiseRepository repository;

    public UpdateFranchiseUseCase(FranchiseRepository repository) {
        this.repository = repository;
    }

    public Mono<Franchise> execute(String id, Franchise franchise){
        if(franchise.getName()==null || franchise.getName().isBlank()){
            return Mono.error(new IllegalArgumentException(
                    ("Franchise name cannot be null or blank."))
            );
        }

        return repository.findById(id)
                .switchIfEmpty(
                        Mono.error(new ResourceNotFoundException("Franchise not found."))
                )
                .flatMap(existingFranchise -> {
                   existingFranchise.setName(franchise.getName());

                   return repository.save(existingFranchise);
                });
    }
}
