package co.com.franchise.mongo.repository;

import co.com.franchise.mongo.document.ProductDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProductMongoRepository
extends ReactiveMongoRepository<ProductDocument, String> {
    Flux<ProductDocument> findByBranchId(String  branchId);
}
