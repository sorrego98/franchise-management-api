package co.com.franchise.api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateProducRequest {
    private String name;
    private Integer stock;
    private String branchId;
}
