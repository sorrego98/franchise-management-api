package co.com.franchise.usecase.updatebranch;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.branch.gateways.BranchRepository;
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
public class UpdateBranchUseCaseTest {
    @Mock
    private BranchRepository branchRepository;

    private UpdatebranchUseCase updatebranchUseCase;

    @BeforeEach
    public void setUp() {
        updatebranchUseCase= new UpdatebranchUseCase(branchRepository);
    }

    @Test
    public void shouldUpdateBranchWhenNameIsValid(){
        Branch currentBranch = Branch.builder()
                .id("b-123")
                .name("Branch 1")
                .franchiseId("f-987")
                .build();

        Branch branchToUpdate = Branch.builder()
                .name("Branch 2")
                .build();

        when(branchRepository.findById("b-123"))
                .thenReturn(Mono.just(currentBranch));

        when(branchRepository.save(currentBranch))
                .thenReturn(Mono.just(currentBranch));

        StepVerifier.create(updatebranchUseCase.execute("b-123", branchToUpdate))
                .expectNextMatches(branch ->
                        branch.getId().equals("b-123") &&
                                branch.getName().equals("Branch 2") &&
                        branch.getFranchiseId().equals("f-987"))
                .verifyComplete();

        verify(branchRepository).findById("b-123");
        verify(branchRepository).save(currentBranch);
    }

    @Test
    public void shouldReturnErrorIfBranchNotFound(){
        Branch branchToUpdate = Branch.builder()
                .name("Branch 2")
                .build();

        when(branchRepository.findById("b-123"))
                .thenReturn(Mono.empty());

        StepVerifier.create(updatebranchUseCase.execute("b-123", branchToUpdate))
                .expectErrorMatches(error ->
                        error instanceof ResourceNotFoundException &&
                                error.getMessage().equals("Branch not found."))
                .verify();

        verify(branchRepository).findById("b-123");
        verify(branchRepository, never()).save(any());
    }

    @Test
    void shouldNotUpdateBranchWhenNameIsNull(){
        Branch branchToUpdate = Branch.builder()
                .name(null)
                .build();

        StepVerifier.create(updatebranchUseCase.execute("b-123", branchToUpdate))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Branch name cannot be null or blank."))
                .verify();

        verify(branchRepository, never()).findById(any());
        verify(branchRepository, never()).save(any());
    }

    @Test
    void shouldNotUpdateBranchWhenNameIsBlank(){
        Branch branchToUpdate = Branch.builder()
                .name("")
                .build();

        StepVerifier.create(updatebranchUseCase.execute("b-123", branchToUpdate))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Branch name cannot be null or blank."))
                .verify();

        verify(branchRepository, never()).findById(any());
        verify(branchRepository, never()).save(any());
    }
}
