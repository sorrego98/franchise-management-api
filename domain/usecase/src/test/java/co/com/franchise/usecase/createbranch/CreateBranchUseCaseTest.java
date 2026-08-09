package co.com.franchise.usecase.createbranch;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.branch.gateways.BranchRepository;
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
public class CreateBranchUseCaseTest {
    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    private CreatebranchUseCase createBranchUseCase;

    @BeforeEach
    public void setUp(){
        createBranchUseCase = new CreatebranchUseCase(branchRepository, franchiseRepository);
    }

    @Test
    void shouldCreateBranch(){
        Branch branch = Branch.builder()
                .id("b-123")
                .name("Branch 1")
                .franchiseId("f-987")
                .build();

        Franchise franchise = Franchise.builder()
                .id("f-987")
                .name("Franchise 1")
                .build();

        when(franchiseRepository.findById("f-987"))
                .thenReturn(Mono.just(franchise));

        when(branchRepository.save(branch))
                .thenReturn(Mono.just(branch));

        StepVerifier.create(createBranchUseCase.execute(branch))
                .expectNext(branch)
                .verifyComplete();

        verify(franchiseRepository).findById("f-987");
        verify(branchRepository).save(branch);
    }

    @Test
    void shouldThrowExceptionWhenFranchiseNotFound(){
        Branch branch= Branch.builder()
                .name("Branch 1")
                .franchiseId("f-987")
                .build();

        when(franchiseRepository.findById("f-987"))
                .thenReturn(Mono.empty());

        StepVerifier.create(createBranchUseCase.execute(branch))
                .expectErrorMatches(error ->
                        error instanceof ResourceNotFoundException &&
                                error.getMessage().equals("Franchise not found."))
                .verify();

        verify(franchiseRepository).findById("f-987");
        verify(branchRepository, never()).save(any());
    }

    @Test
    void shouldNotCreateBranchWhenFranchiseIdIsNull(){
        Branch branch= Branch.builder()
                .name("Branch 1")
                .franchiseId(null)
                .build();

        StepVerifier.create(createBranchUseCase.execute(branch))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Franchise Id cannot be null or blank."))
                .verify();

        verify(franchiseRepository, never()).findById(any());
        verify(branchRepository, never()).save(any());
    }

    @Test
    void shouldNotCreateBranchWhenFranchiseIdIsBlank(){
        Branch branch= Branch.builder()
                .name("Branch 1")
                .franchiseId("")
                .build();

        StepVerifier.create(createBranchUseCase.execute(branch))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Franchise Id cannot be null or blank."))
                .verify();

        verify(franchiseRepository, never()).findById(any());
        verify(branchRepository, never()).save(any());
    }

    @Test
    void shouldNotCreateBranchWhenNameIsNull(){
        Branch branch= Branch.builder()
                .name(null)
                .franchiseId("f-987")
                .build();

        StepVerifier.create(createBranchUseCase.execute(branch))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Branch name cannot be null or blank."))
                .verify();

        verify(franchiseRepository, never()).findById(any());
        verify(branchRepository, never()).save(any());
    }

    @Test
    void shouldNotCreateBranchWhenNameIsBlank(){
        Branch branch= Branch.builder()
                .name("")
                .franchiseId("f-987")
                .build();

        StepVerifier.create(createBranchUseCase.execute(branch))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Branch name cannot be null or blank."))
                .verify();

        verify(franchiseRepository, never()).findById(any());
        verify(branchRepository, never()).save(any());
    }
}
