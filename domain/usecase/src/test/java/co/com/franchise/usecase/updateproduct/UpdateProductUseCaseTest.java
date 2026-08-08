package co.com.franchise.usecase.updateproduct;

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
public class UpdateProductUseCaseTest {
    @Mock
    private ProductRepository productRepository;

    private UpdateproductUseCase updateProductUseCase;

    @BeforeEach
    void setUp() {
        updateProductUseCase = new UpdateproductUseCase(productRepository);
    }

    @Test
    public void shouldUpdateProductWhenNameIsValid(){
        Product currentProduct = Product.builder()
                .id("p-123")
                .name("Product 1")
                .stock(5)
                .branchId("b-123")
                .build();

        Product productToUpdate = Product.builder()
                .name("Product 2")
                .build();

        when(productRepository.findById("p-123"))
                .thenReturn(Mono.just(currentProduct));

        when(productRepository.save(currentProduct))
                .thenReturn(Mono.just(currentProduct));

        StepVerifier.create(updateProductUseCase.execute("p-123", productToUpdate))
                .expectNextMatches(product ->
                        product.getId().equals("p-123") &&
                                product.getName().equals("Product 2") &&
                                product.getStock().equals(5) &&
                                product.getBranchId().equals("b-123")
                        )
                .verifyComplete();

        verify(productRepository).findById("p-123");
        verify(productRepository).save(currentProduct);
    }

    @Test
    void shouldReturnErrorIfProductNotFound(){
        Product productToUpdate = Product.builder()
                .name("Product 2")
                .build();

        when(productRepository.findById("p-123"))
                .thenReturn(Mono.empty());

        StepVerifier.create(updateProductUseCase.execute("p-123", productToUpdate))
                .expectErrorMatches(error ->
                        error instanceof ResourceNotFoundException &&
                                error.getMessage().equals("Product not found."))
                .verify();

        verify(productRepository).findById("p-123");
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldNotUpdateProductWhenNameIsNull(){
        Product productToUpdate = Product.builder()
                .name(null)
                .build();

        StepVerifier.create(updateProductUseCase.execute("p-123", productToUpdate))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Product name cannot be null or blank."))
                .verify();

        verify(productRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldNotUpdateProductWhenNameIsBlank(){
        Product productToUpdate = Product.builder()
                .name("")
                .build();

        StepVerifier.create(updateProductUseCase.execute("p-123", productToUpdate))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Product name cannot be null or blank."))
                .verify();

        verify(productRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }
}
