package co.com.franchise.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBranchRequest {
    private String name;
    private String franchiseId;
}
