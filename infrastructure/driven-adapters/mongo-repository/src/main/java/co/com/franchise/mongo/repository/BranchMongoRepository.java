package co.com.franchise.mongo.repository;

import co.com.franchise.mongo.document.BranchDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BranchMongoRepository
        extends ReactiveMongoRepository<BranchDocument, String> {
}
