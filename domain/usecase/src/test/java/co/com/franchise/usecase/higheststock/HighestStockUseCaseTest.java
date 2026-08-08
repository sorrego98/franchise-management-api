package co.com.franchise.usecase.higheststock;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.branch.gateways.BranchRepository;
import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HighestStockUseCaseTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    private HigheststockUseCase higheststockUseCase;

    @BeforeEach
    void setUp() {
        higheststockUseCase = new HigheststockUseCase(branchRepository,  productRepository);
    }

    @Test
    public void shouldReturnHighestStockPerBranchForAFranchise (){
        Branch branch1= Branch.builder()
                .id("b-101")
                .name("Branch 1")
                .franchiseId("f-987")
                .build();

        Branch branch2= Branch.builder()
                .id("b-102")
                .name("Branch 2")
                .franchiseId("f-987")
                .build();

        Branch branch3= Branch.builder()
                .id("b-103")
                .name("Branch 3")
                .franchiseId("f-987")
                .build();

        Branch branch4= Branch.builder()
                .id("b-104")
                .name("Branch 4")
                .franchiseId("f-997")
                .build();

        Product product1 = Product.builder()
                .id("p-123")
                .name("Agua")
                .stock(7)
                .branchId("b-101")
                .build();

        Product product2 = Product.builder()
                .id("p-121")
                .name("Cola")
                .stock(5)
                .branchId("b-101")
                .build();

        Product product3 = Product.builder()
                .id("p-223")
                .name("Pan")
                .stock(8)
                .branchId("b-102")
                .build();

        Product product4 = Product.builder()
                .id("p-121")
                .name("Sal")
                .stock(15)
                .branchId("b-102")
                .build();

        Product product5 = Product.builder()
                .id("p-123")
                .name("Agua")
                .stock(17)
                .branchId("b-104")
                .build();

        when(branchRepository.findByFranchiseId("f-987"))
                .thenReturn(Flux.just(branch1, branch2, branch3));

        when(branchRepository.findByFranchiseId("f-997"))
                .thenReturn(Flux.just(branch4));

        when(productRepository.findByBranchId("b-101"))
                .thenReturn(Flux.just(product1, product2));

        when(productRepository.findByBranchId("b-102"))
                .thenReturn(Flux.just(product3, product4));

        when(productRepository.findByBranchId("b-103"))
                .thenReturn(Flux.empty());

        when(productRepository.findByBranchId("b-104"))
                .thenReturn(Flux.just(product5));

        StepVerifier.create(higheststockUseCase.execute("f-987"))
                .expectNext(product1)
                .expectNext(product4)
                .verifyComplete();

        StepVerifier.create(higheststockUseCase.execute("f-997"))
                .expectNext(product5)
                .verifyComplete();

        verify(branchRepository).findByFranchiseId("f-987");
        verify(branchRepository).findByFranchiseId("f-997");
        verify(productRepository).findByBranchId("b-101");
        verify(productRepository).findByBranchId("b-102");
        verify(productRepository).findByBranchId("b-103");
        verify(productRepository).findByBranchId("b-104");
    }
}
