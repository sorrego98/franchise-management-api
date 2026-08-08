package co.com.franchise.usecase.updatefranchise;

import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.gateways.FranchiseRepository;
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
public class UpdateFranchiseUseCaseTest {
    @Mock
    private FranchiseRepository franchiseRepository;
    private UpdateFranchiseUseCase updateFranchiseUseCase;

    @BeforeEach
    void setUp() {
        updateFranchiseUseCase = new UpdateFranchiseUseCase(franchiseRepository);
    }

    @Test
    void shouldUpdateFranchiseWhenNameIsValid(){
        Franchise currentFranchise = Franchise.builder()
                .id("987")
                .name("Franchise 1")
                .build();

        Franchise franchiseToUpdate = Franchise.builder()
                .name("Franchise 2")
                .build();

        when(franchiseRepository.findById("987"))
                .thenReturn(Mono.just(currentFranchise));

        when(franchiseRepository.save(currentFranchise))
                .thenReturn(Mono.just(currentFranchise));

        StepVerifier.create(updateFranchiseUseCase.execute("987", franchiseToUpdate))
                .expectNextMatches(franchise ->
                        franchise.getId().equals("987") &&
                                franchise.getName().equals("Franchise 2"))
                .verifyComplete();

        verify(franchiseRepository).findById("987");
        verify(franchiseRepository).save(currentFranchise);
    }

    @Test
    void shouldReturnErrorIfFranchiseNotFound(){
        Franchise franchiseToUtpdate = Franchise.builder()
                .name("Franchise 2")
                .build();

        when(franchiseRepository.findById("987"))
                .thenReturn(Mono.empty());

        StepVerifier.create(updateFranchiseUseCase.execute("987", franchiseToUtpdate))
                .expectErrorMatches(error ->
                        error instanceof ResourceNotFoundException &&
                        error.getMessage().equals("Franchise not found."))
                .verify();

        verify(franchiseRepository).findById("987");
        verify(franchiseRepository, never()).save(any());
    }

    @Test
    void shouldNotUpdateFranchiseWhenNameIsNull(){
        Franchise franchiseToUpdate = Franchise.builder()
                .name(null)
                .build();

        StepVerifier.create(updateFranchiseUseCase.execute("987", franchiseToUpdate))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Franchise name cannot be null or blank."))
                .verify();

        verify(franchiseRepository, never()).findById(any());
        verify(franchiseRepository, never()).save(any());
    }

    @Test
    void shouldNotUpdateFranchiseWhenNameIsBlank(){
        Franchise franchiseToUpdate = Franchise.builder()
                .name("")
                .build();

        StepVerifier.create(updateFranchiseUseCase.execute("987", franchiseToUpdate))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Franchise name cannot be null or blank."))
                .verify();

        verify(franchiseRepository, never()).findById(any());
        verify(franchiseRepository, never()).save(any());
    }
}
