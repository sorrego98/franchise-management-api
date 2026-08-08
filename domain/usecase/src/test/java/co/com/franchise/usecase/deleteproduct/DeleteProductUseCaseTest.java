package co.com.franchise.usecase.deleteproduct;

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
public class DeleteProductUseCaseTest {
    @Mock
    private ProductRepository productRepository;

    private DeleteproductUseCase deleteProductUseCase;

    @BeforeEach
    void setUp() {
        deleteProductUseCase = new DeleteproductUseCase(productRepository);
    }

    @Test
    public void shouldDeleteProduct() {
        Product product = Product.builder()
                .id("p-123")
                .name("Product 1")
                .stock(5)
                .branchId("b-123")
                .build();

        when(productRepository.findById("p-123"))
                .thenReturn(Mono.just(product));

        when(productRepository.deleteById("p-123"))
                .thenReturn(Mono.empty());

        StepVerifier.create(
                deleteProductUseCase.execute("p-123"))
                .verifyComplete();

        verify(productRepository).findById("p-123");
        verify(productRepository).deleteById("p-123");
    }

    @Test
    void shouldReturnErrorIfProductNotFound(){
        when(productRepository.findById("p-123"))
                .thenReturn(Mono.empty());

        StepVerifier.create(deleteProductUseCase.execute("p-123"))
                .expectErrorMatches(error ->
                        error instanceof ResourceNotFoundException &&
                                error.getMessage().equals("Product not found."))
                .verify();

        verify(productRepository).findById("p-123");
        verify(productRepository, never()).deleteById(any());
    }
}
