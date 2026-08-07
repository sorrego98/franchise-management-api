package co.com.franchise.usecase.createfranchise;

import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.gateways.FranchiseRepository;
import reactor.core.publisher.Mono;

public class CreateFranchiseUseCase {
    private final FranchiseRepository repository;

    public CreateFranchiseUseCase(FranchiseRepository repository) {
        this.repository = repository;
    }

    public Mono<Franchise>  execute(Franchise franchise){
        if(franchise.getName()==null || franchise.getName().isBlank()){
            return Mono.error(new IllegalArgumentException(
                    ("Franchise name cannot be null or blank"))
            );
        }
        return repository.save(franchise);
    }
}
