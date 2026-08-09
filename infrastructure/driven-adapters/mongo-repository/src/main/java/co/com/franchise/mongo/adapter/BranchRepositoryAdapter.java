package co.com.franchise.mongo.adapter;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.branch.gateways.BranchRepository;
import co.com.franchise.mongo.document.BranchDocument;
import co.com.franchise.mongo.helper.AdapterOperations;
import co.com.franchise.mongo.repository.BranchMongoRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class BranchRepositoryAdapter extends AdapterOperations<
        Branch,
        BranchDocument,
        String,
        BranchMongoRepository
        > implements BranchRepository {

    public BranchRepositoryAdapter(
            BranchMongoRepository repository,
            ObjectMapper mapper) {

        super(repository, mapper,
                document -> mapper.map(document, Branch.class));
    }

    @Override
    public Flux<Branch> findByFranchiseId(String franchiseId) {
        return repository.findByFranchiseId(franchiseId)
                .map(document -> mapper.map(document, Branch.class));
    }
}
