package co.com.pragma.usecase.capacity;

import co.com.pragma.model.capacity.exceptions.CustomException;
import co.com.pragma.model.capacity.exceptions.ExceptionsEnum;
import co.com.pragma.model.capacity.exceptions.HttpException;
import co.com.pragma.model.capacity.models.*;
import co.com.pragma.model.capacity.spi.ICapacityBootcampPersistencePort;
import co.com.pragma.model.capacity.spi.ICapacityPersistencePort;
import co.com.pragma.model.capacity.spi.ITechnologiesPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CapacityUseCaseTest {


    @Mock
    private ICapacityPersistencePort capacityPersistencePort;

    @Mock
    private ITechnologiesPersistencePort technologiesPersistencePort;

    @Mock
    private ICapacityBootcampPersistencePort capacityBootcampPersistencePort;

    @InjectMocks
    private CapacityUseCase capacityUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveCapacity_ShouldSaveSuccessfully() {
        // Given
        Capacity capacity = new Capacity(1L,"Backend Development", "a",List.of(1L, 2L, 3L),1);

        when(technologiesPersistencePort.checkTechnologies(capacity.getTechnologiesIds()))
                .thenReturn(Mono.just(new ValidationResponse("All technologies exist", true)));

        when(capacityPersistencePort.findByName(capacity.getName()))
                .thenReturn(Mono.empty());

        when(capacityPersistencePort.saveCapacity(capacity))
                .thenReturn(Mono.just(capacity));

        when(technologiesPersistencePort.saveTechnologiesCapacities(capacity))
                .thenReturn(Mono.just(true));

        // When
        Mono<Capacity> result = capacityUseCase.saveCapacity(capacity);

        // Then
        StepVerifier.create(result)
                .expectNext(capacity)
                .verifyComplete();

        verify(capacityPersistencePort, times(1)).findByName(capacity.getName());
        verify(capacityPersistencePort, times(1)).saveCapacity(capacity);
        verify(technologiesPersistencePort, times(1)).checkTechnologies(capacity.getTechnologiesIds());
        verify(technologiesPersistencePort, times(1)).saveTechnologiesCapacities(capacity);
    }

    @Test
    void saveCapacity_ShouldReturnErrorMinTechnologies() {

        Capacity capacity = new Capacity(1L,"Backend Development", "a",List.of(1L, 2L),1);

        Mono<Capacity> result = capacityUseCase.saveCapacity(capacity);


        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof CustomException customException
                                && customException.getStatus() == ExceptionsEnum.MIN_TECHNOLOGIES.getHttpStatus())
                .verify();
    }

    @Test
    void saveCapacity_ShouldReturnErrorMaxTechnologies() {

        Capacity capacity = new Capacity(1L,"Backend Development", "a",List.of(
                1L, 2L,1L, 2L,1L, 2L,1L, 2L,1L, 2L,1L,
                2L,1L, 2L,1L, 2L,1L, 2L,1L, 2L,1L, 2L,1L, 2L,1L,
                2L,1L, 2L,1L, 2L,1L, 2L),
                1);

        Mono<Capacity> result = capacityUseCase.saveCapacity(capacity);


        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof CustomException customException
                                && customException.getStatus() == ExceptionsEnum.MAX_TECHNOLOGIES.getHttpStatus())
                .verify();
    }


    @Test
    void saveCapacity_ShouldReturnErrorDuplicateTechnologies() {

        Capacity capacity = new Capacity(1L,"Backend Development", "a",List.of(
                1L, 2L,1L),
                1);

        Mono<Capacity> result = capacityUseCase.saveCapacity(capacity);


        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof CustomException customException
                                && customException.getStatus() == ExceptionsEnum.DUPLICATE_TECHNOLOGIES.getHttpStatus())
                .verify();
    }


    @Test
    void saveCapacity_ShouldFail_WhenTechnologiesAreInvalid() {

        Capacity capacity = new Capacity(1L,"Data Science", "a",List.of(10L, 20L,30L),1);

        when(technologiesPersistencePort.checkTechnologies(capacity.getTechnologiesIds()))
                .thenReturn(Mono.just(new ValidationResponse("Missing technologies: [10, 20]", false)));


        Mono<Capacity> result = capacityUseCase.saveCapacity(capacity);


        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof HttpException && throwable.getMessage().contains("Missing technologies"))
                .verify();

        verify(technologiesPersistencePort, times(1)).checkTechnologies(capacity.getTechnologiesIds());
        verify(capacityPersistencePort, never()).findByName(anyString());
        verify(capacityPersistencePort, never()).saveCapacity(any());
    }

    @Test
    void saveCapacity_ShouldFail_WhenCapacityAlreadyExists() {

        Capacity capacity = new Capacity(1L,"Mobile Development", "a",List.of(5L, 6L, 7L),1);

        when(technologiesPersistencePort.checkTechnologies(capacity.getTechnologiesIds()))
                .thenReturn(Mono.just(new ValidationResponse("All technologies exist", true)));

        when(capacityPersistencePort.findByName(capacity.getName()))
                .thenReturn(Mono.just(new Capacity(2L,"Mobile Development", "a",List.of(),1))); // Ya existe


        Mono<Capacity> result = capacityUseCase.saveCapacity(capacity);


        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof CustomException &&
                                throwable.getMessage().equals(ExceptionsEnum.DUPLICATE_CAPACITY.getMessage()))
                .verify();

        verify(capacityPersistencePort, times(1)).findByName(capacity.getName());
        verify(capacityPersistencePort, never()).saveCapacity(any());
    }

    @Test
    void saveCapacity_ShouldRollback_WhenTechnologySaveFails() {

        Capacity capacity = new Capacity(1L,"Cloud Engineering", "a",List.of(7L, 8L, 9L),1);

        when(technologiesPersistencePort.checkTechnologies(capacity.getTechnologiesIds()))
                .thenReturn(Mono.just(new ValidationResponse("All technologies exist", true)));

        when(capacityPersistencePort.findByName(capacity.getName()))
                .thenReturn(Mono.empty());

        when(capacityPersistencePort.saveCapacity(capacity))
                .thenReturn(Mono.just(capacity));

        when(technologiesPersistencePort.saveTechnologiesCapacities(capacity))
                .thenReturn(Mono.just(false));

        when(capacityPersistencePort.deleteCapacity(capacity.getId()))
                .thenReturn(Mono.empty());


        Mono<Capacity> result = capacityUseCase.saveCapacity(capacity);


        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof HttpException &&
                                throwable.getMessage().contains("No fue posible crear la relación"))
                .verify();

        verify(capacityPersistencePort, times(1)).saveCapacity(capacity);
        verify(technologiesPersistencePort, times(1)).saveTechnologiesCapacities(capacity);
        verify(capacityPersistencePort, times(1)).deleteCapacity(capacity.getId());
    }
    @Test
    void listCapacities_shouldReturnPagedResponse() {
        Capacity capacity = new Capacity(1L, "Java Developer", "a", List.of(1L,2L,3L),1);
        Technology technology = new Technology(1L, "Java");
        int page = 0, size = 10;
        String sortBy = "name", sortOrder = "asc";

        when(capacityPersistencePort.listCapacities(page, size, sortBy, sortOrder))
                .thenReturn(Flux.just(capacity));
        when(technologiesPersistencePort.getTechnologiesByCapacity(capacity.getId()))
                .thenReturn(Flux.just(technology));
        when(capacityPersistencePort.countCapacities()).thenReturn(Mono.just(1L));

        Mono<PagedResponseCapabilities> result = capacityUseCase.listCapacities(page, size, sortBy, sortOrder);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getCount() == 1L &&
                                response.getItems().size() == 1 &&
                                response.getItems().get(0).getTechnologies().size() == 1 &&
                                response.getItems().get(0).getTechnologies().get(0).getName().contains("Java")
                )
                .verifyComplete();

        verify(capacityPersistencePort).listCapacities(page, size, sortBy, sortOrder);
        verify(technologiesPersistencePort).getTechnologiesByCapacity(capacity.getId());
        verify(capacityPersistencePort).countCapacities();
    }

    @Test
    void saveCapacityBootcamp_AllIdsExist_ShouldSaveSuccessfully() {
        CapacityBootcamp capacityBootcamp = new CapacityBootcamp(List.of(1L, 2L));
        Capacity capacity1 = new Capacity(1L,"Programacion lineal", "pragma", List.of(1L),1);
        Capacity capacity2 = new Capacity(2L,"Programacion funcional", "pragma", List.of(1L),1);

        when(capacityPersistencePort.findById(1L)).thenReturn(Mono.just(capacity1));
        when(capacityPersistencePort.findById(2L)).thenReturn(Mono.just(capacity2));
        when(capacityBootcampPersistencePort.saveCapacityBootcamp(capacityBootcamp)).thenReturn(Mono.empty());
        Mono<ValidationResponse> result = capacityUseCase.saveCapacityBootcamp(capacityBootcamp);

        StepVerifier.create(result)
                .expectNextMatches(validation ->
                    Boolean.TRUE.equals(validation.getValid()) && validation.getMessage().equals("Save successfully")
                )
                .verifyComplete();

        verify(capacityBootcampPersistencePort).saveCapacityBootcamp(capacityBootcamp);
    }
    @Test
    void saveCapacityBootcamp_IdNotExist_ShouldSaveSuccessfully(){
        CapacityBootcamp capacityBootcamp = new CapacityBootcamp(List.of(1L));

        when(capacityPersistencePort.findById(1L)).thenReturn(Mono.empty());
        when(capacityBootcampPersistencePort.saveCapacityBootcamp(capacityBootcamp)).thenReturn(Mono.empty());
        Mono<ValidationResponse> result = capacityUseCase.saveCapacityBootcamp(capacityBootcamp);

        StepVerifier.create(result)
                .expectNextMatches(validation ->
                        Boolean.FALSE.equals(validation.getValid()) && validation.getMessage().equals("Missing technologies: [1]")
                )
                .verifyComplete();
    }

    @Test
    void findCapabilitiesByBootcamp_ShouldReturnCapacitiesWithTechnologies() {
        Long bootcampId = 1L;

        Capacity capacity1 = new Capacity(1L, "POO", "Desarrollo", List.of(101L,102L),2);
        Capacity capacity2 = new Capacity(2L, "WEB", "SPA", List.of(103L), 1);

        Technology tech1 = new Technology(101L, "Java");
        Technology tech2 = new Technology(102L, "Spring");
        Technology tech3 = new Technology(103L, "React");

        when(capacityBootcampPersistencePort.findCapabilitiesByBootcampId(bootcampId))
                .thenReturn(Flux.just(capacity1, capacity2));

        when(technologiesPersistencePort.getTechnologiesByCapacity(1L))
                .thenReturn(Flux.just(tech1, tech2));

        when(technologiesPersistencePort.getTechnologiesByCapacity(2L))
                .thenReturn(Flux.just(tech3));

        Flux<CapacityTechnologies> result = capacityUseCase.findCapabilitiesByBootcamp(bootcampId);

        StepVerifier.create(result)
                .consumeNextWith(capacityTech -> {
                    assertThat(capacityTech.getId()).isEqualTo(1L);
                    assertThat(capacityTech.getName()).isEqualTo("POO");
                    assertThat(capacityTech.getTechnologies()).containsExactly(tech1, tech2);
                    assertThat(capacityTech.getTechnologiesCount()).isEqualTo(2);
                })
                .consumeNextWith(capacityTech -> {
                    assertThat(capacityTech.getId()).isEqualTo(2L);
                    assertThat(capacityTech.getName()).isEqualTo("WEB");
                    assertThat(capacityTech.getTechnologies()).containsExactly(tech3);
                    assertThat(capacityTech.getTechnologiesCount()).isEqualTo(1);
                })
                .verifyComplete();

        verify(capacityBootcampPersistencePort).findCapabilitiesByBootcampId(bootcampId);
        verify(technologiesPersistencePort).getTechnologiesByCapacity(1L);
        verify(technologiesPersistencePort).getTechnologiesByCapacity(2L);
    }

}