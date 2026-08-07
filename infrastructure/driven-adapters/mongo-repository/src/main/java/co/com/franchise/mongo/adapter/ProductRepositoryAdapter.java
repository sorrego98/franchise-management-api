package co.com.franchise.mongo.adapter;

import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.gateways.ProductRepository;
import co.com.franchise.mongo.document.ProductDocument;
import co.com.franchise.mongo.helper.AdapterOperations;
import co.com.franchise.mongo.repository.ProductMongoRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class ProductRepositoryAdapter extends AdapterOperations<
        Product,
        ProductDocument,
        String,
        ProductMongoRepository
        > implements ProductRepository {

    public ProductRepositoryAdapter(
            ProductMongoRepository repository,
            ObjectMapper mapper
            ) {
        super(repository, mapper,
                document -> mapper.map(document, Product.class));

    }

    @Override
    public Flux<Product> findByBranchId(String  branchId) {
        return repository.findByBranchId(branchId)
                .map(document -> mapper.map(document, Product.class));
    }
}
