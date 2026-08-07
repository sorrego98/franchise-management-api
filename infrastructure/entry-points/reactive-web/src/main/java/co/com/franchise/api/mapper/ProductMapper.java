package co.com.franchise.api.mapper;

import co.com.franchise.api.dto.CreateProducRequest;
import co.com.franchise.api.dto.UpdateProductRequest;
import co.com.franchise.api.dto.UpdateProductStockRequest;
import co.com.franchise.model.product.Product;

public class ProductMapper {

    private ProductMapper() {}

    public static Product toDomain(CreateProducRequest request){
        return Product.builder()
                .name(request.getName())
                .stock(request.getStock())
                .branchId(request.getBranchId())
                .build();
    }

    public static Product toDomain(UpdateProductRequest request){
        return Product.builder()
                .name(request.getName())
                .build();
    }

    public static Product toDomain(UpdateProductStockRequest request){
        return Product.builder()
                .stock(request.getStock())
                .build();
    }
}
