package co.com.franchise.mongo.repository;

import co.com.franchise.mongo.document.FranchiseDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FranchiseMongoRepository
    extends ReactiveMongoRepository<FranchiseDocument, String> {
}
