package co.com.franchise.usecase.updatestock;

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
public class UpdateStockUseCaseTest {
    @Mock
    private ProductRepository productRepository;

    private UpdatestockUseCase updatestockUseCase;

    @BeforeEach
    void setUp() {
        updatestockUseCase = new UpdatestockUseCase(productRepository);
    }

    @Test
    public void shouldUpdateProductWhenStockIsValid(){
        Product currentProduct = Product.builder()
                .id("p-123")
                .name("Product 1")
                .stock(5)
                .branchId("b-123")
                .build();

        when(productRepository.findById("p-123"))
                .thenReturn(Mono.just(currentProduct));

        when(productRepository.save(currentProduct))
                .thenReturn(Mono.just(currentProduct));

        StepVerifier.create(updatestockUseCase.execute("p-123", 10))
                .expectNextMatches(product ->
                        product.getId().equals("p-123") &&
                                product.getName().equals("Product 1") &&
                                product.getStock().equals(10) &&
                                product.getBranchId().equals("b-123")
                )
                .verifyComplete();

        verify(productRepository).findById("p-123");
        verify(productRepository).save(currentProduct);
    }

    @Test
    void shouldReturnErrorIfProductNotFound(){
        when(productRepository.findById("p-123"))
                .thenReturn(Mono.empty());

        StepVerifier.create(updatestockUseCase.execute("p-123", 10))
                .expectErrorMatches(error ->
                        error instanceof ResourceNotFoundException &&
                                error.getMessage().equals("Product not found."))
                .verify();

        verify(productRepository).findById("p-123");
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldNotUpdateProductWhenStockIsNull(){
        StepVerifier.create(updatestockUseCase.execute("p-123", null))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Stock cannot be null or negative."))
                .verify();

        verify(productRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldNotUpdateProductWhenStockIsNegative(){
        StepVerifier.create(updatestockUseCase.execute("p-123", -5))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Stock cannot be null or negative."))
                .verify();

        verify(productRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }
}
