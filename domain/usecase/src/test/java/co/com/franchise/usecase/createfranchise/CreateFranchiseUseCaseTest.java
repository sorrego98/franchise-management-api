package co.com.franchise.usecase.createfranchise;

import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.gateways.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateFranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;
    private CreateFranchiseUseCase createFranchiseUseCase;

    @BeforeEach
    void setUp() {
        createFranchiseUseCase = new CreateFranchiseUseCase(franchiseRepository);
    }

    @Test
    void shouldCreateFranchiseWhenNameIsValid(){
        Franchise franchise = Franchise.builder()
                .id("f-987")
                .name("Franchise 1")
                .build();

        when(franchiseRepository.save(franchise))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(createFranchiseUseCase.execute(franchise))
                .expectNext(franchise)
                .verifyComplete();

        verify(franchiseRepository).save(franchise);
    }

    @Test
    void shouldNotCreateFranchiseWhenNameIsNull(){
        Franchise franchise = Franchise.builder()
                .id("987")
                .name(null)
                .build();

        StepVerifier.create(createFranchiseUseCase.execute(franchise))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                        error.getMessage().equals("Franchise name cannot be null or blank."))
                .verify();

        verify(franchiseRepository, never()).save(franchise);
    }

    @Test
    void shouldNotCreateFranchiseWhenNameIsBlank(){
        Franchise franchise = Franchise.builder()
                .id("987")
                .name("")
                .build();

        StepVerifier.create(createFranchiseUseCase.execute(franchise))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Franchise name cannot be null or blank."))
                .verify();

        verify(franchiseRepository,  never()).save(franchise);
    }
}
