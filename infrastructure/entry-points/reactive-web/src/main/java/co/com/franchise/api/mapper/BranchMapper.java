package co.com.franchise.api.mapper;

import co.com.franchise.api.dto.CreateBranchRequest;
import co.com.franchise.model.branch.Branch;

public class BranchMapper {

    private BranchMapper()
    {
    }

    public static Branch toDomain(CreateBranchRequest request){
        return Branch.builder()
                .name(request.getName())
                .franchiseId(request.getFranchiseId())
                .build();
    }
}
