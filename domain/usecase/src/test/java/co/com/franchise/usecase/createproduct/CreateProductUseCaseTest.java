package co.com.franchise.usecase.createproduct;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.branch.gateways.BranchRepository;
import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.gateways.ProductRepository;
import co.com.franchise.usecase.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateProductUseCaseTest {
    @Mock
    private ProductRepository  productRepository;

    @Mock
    private BranchRepository branchRepository;

    private CreateProductUseCase createProductUseCase;

    @BeforeEach
    public void setUp() {
        createProductUseCase = new CreateProductUseCase(productRepository, branchRepository);
    }

    @Test
    public void shouldCreateProduct() {
        Product product = Product.builder()
                .id("p-123")
                .name("Product 1")
                .stock(5)
                .branchId("b-123")
                .build();

        Branch branch = Branch.builder()
                .id("b-123")
                .name("Branch 1")
                .franchiseId("f-987")
                .build();

        when(branchRepository.findById("b-123"))
                .thenReturn(Mono.just(branch));

        when(productRepository.save(product))
                .thenReturn(Mono.just(product));

        StepVerifier.create(createProductUseCase.execute(product))
                .expectNext(product)
                .verifyComplete();

        verify(branchRepository).findById("b-123");
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowExceptionWhenBranchNotFound(){
        Product product = Product.builder()
                .name("Product 1")
                .stock(5)
                .branchId("b-123")
                .build();

        when(branchRepository.findById("b-123"))
                .thenReturn(Mono.empty());

        StepVerifier.create(createProductUseCase.execute(product))
                .expectErrorMatches(error ->
                        error instanceof ResourceNotFoundException &&
                                error.getMessage().equals("Branch not found."))
                .verify();

        verify(branchRepository).findById("b-123");
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldNotCreateProductWhenBranchIdIsNull(){
        Product product = Product.builder()
                .name("Product 1")
                .stock(5)
                .branchId(null)
                .build();

        StepVerifier.create(createProductUseCase.execute(product))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Branch Id cannot be null or blank."))
                .verify();

        verify(branchRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldNotCreateProductWhenBranchIdIsBlank(){
        Product product = Product.builder()
                .name("Product 1")
                .stock(5)
                .branchId("")
                .build();

        StepVerifier.create(createProductUseCase.execute(product))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Branch Id cannot be null or blank."))
                .verify();

        verify(branchRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldNotCreateProductWhenNameIsNull(){
        Product product = Product.builder()
                .name(null)
                .stock(5)
                .branchId("b-123")
                .build();

        StepVerifier.create(createProductUseCase.execute(product))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Product name cannot be null or blank."))
                .verify();

        verify(branchRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldNotCreateProductWhenNameIsBlank(){
        Product product = Product.builder()
                .name("")
                .stock(5)
                .branchId("b-123")
                .build();

        StepVerifier.create(createProductUseCase.execute(product))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Product name cannot be null or blank."))
                .verify();

        verify(branchRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldNotCreateProductWhenStockIsNull(){
        Product product = Product.builder()
                .name("Product 1")
                .stock(null)
                .branchId("b-123")
                .build();

        StepVerifier.create(createProductUseCase.execute(product))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Product stock cannot be null or negative."))
                .verify();

        verify(branchRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldNotCreateProductWhenStockIsNegative(){
        Product product = Product.builder()
                .name("Product 1")
                .stock(-5)
                .branchId("b-123")
                .build();

        StepVerifier.create(createProductUseCase.execute(product))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Product stock cannot be null or negative."))
                .verify();

        verify(branchRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }
}
