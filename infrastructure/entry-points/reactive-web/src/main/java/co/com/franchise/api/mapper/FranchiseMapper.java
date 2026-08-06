package co.com.franchise.api.mapper;

import co.com.franchise.api.dto.CreateFranchiseRequest;
import co.com.franchise.model.franchise.Franchise;

public class FranchiseMapper {

    private FranchiseMapper(){
    }

    public static Franchise toDomain(CreateFranchiseRequest request){
        return Franchise.builder()
                .name(request.getName())
                .build();
    }
}
