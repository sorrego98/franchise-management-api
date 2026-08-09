package co.com.franchise.mongo.adapter;

import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.gateways.FranchiseRepository;
import co.com.franchise.mongo.document.FranchiseDocument;
import co.com.franchise.mongo.helper.AdapterOperations;
import co.com.franchise.mongo.repository.FranchiseMongoRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class FranchiseRepositoryAdapter extends AdapterOperations<
        Franchise,
        FranchiseDocument,
        String,
        FranchiseMongoRepository> implements FranchiseRepository {

    public FranchiseRepositoryAdapter(
            FranchiseMongoRepository repository,
            ObjectMapper mapper) {

        super(repository, mapper,
                document -> mapper.map(document, Franchise.class));
    }
}
