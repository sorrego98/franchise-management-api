package co.com.franchise.mongo.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "branches")
public class BranchDocument {
    @Id
    private String id;
    private String name;
    private String franchiseId;
}
