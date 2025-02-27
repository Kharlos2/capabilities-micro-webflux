package co.com.pragma.usecase.capacity;

import co.com.pragma.model.capacity.api.ICapacityServicePort;
import co.com.pragma.model.capacity.exceptions.CustomException;
import co.com.pragma.model.capacity.exceptions.ExceptionsEnum;
import co.com.pragma.model.capacity.exceptions.HttpException;
import co.com.pragma.model.capacity.models.*;
import co.com.pragma.model.capacity.spi.ICapacityBootcampPersistencePort;
import co.com.pragma.model.capacity.spi.ICapacityPersistencePort;
import co.com.pragma.model.capacity.spi.ITechnologiesPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CapacityUseCase implements ICapacityServicePort {

    private final ICapacityPersistencePort capacityPersistencePort;
    private final ITechnologiesPersistencePort technologiesPersistencePort;
    private final ICapacityBootcampPersistencePort capacityBootcampPersistencePort;

    public CapacityUseCase(ICapacityPersistencePort capacityPersistencePort, ITechnologiesPersistencePort technologiesPersistencePort, ICapacityBootcampPersistencePort capacityBootcampPersistencePort) {
        this.capacityPersistencePort = capacityPersistencePort;
        this.technologiesPersistencePort = technologiesPersistencePort;
        this.capacityBootcampPersistencePort = capacityBootcampPersistencePort;
    }


    @Override
    public Mono<Capacity> saveCapacity(Capacity capacity) {



        return validateAcceptanceCriteria(capacity).then(
                technologiesPersistencePort.checkTechnologies(capacity.getTechnologiesIds())
                .flatMap(validationResponse -> {
                    if (!Boolean.TRUE.equals(validationResponse.getValid())) {
                        return Mono.error(new HttpException(400, validationResponse.getMessage()));
                    }
                    return capacityPersistencePort.findByName(capacity.getName())
                            .hasElement()
                            .flatMap(exists -> {
                                if (Boolean.TRUE.equals(exists)) {
                                    return Mono.error(new CustomException(ExceptionsEnum.DUPLICATE_CAPACITY));
                                }
                                capacity.setQuantityTechnologies(capacity.getTechnologiesIds().size());
                                return capacityPersistencePort.saveCapacity(capacity)
                                        .flatMap(savedCapacity -> {
                                            savedCapacity.setTechnologiesIds(capacity.getTechnologiesIds());
                                            return technologiesPersistencePort.saveTechnologiesCapacities(savedCapacity)
                                                    .flatMap(saveSuccess -> Boolean.TRUE.equals(saveSuccess)
                                                            ? Mono.just(savedCapacity)
                                                            : capacityPersistencePort.deleteCapacity(savedCapacity.getId())
                                                            .then(Mono.error(new HttpException(409, "No fue posible crear la relación")))
                                                    );
                                        });
                            });

                }));
    }


    @Override
    public Mono<PagedResponse<CapacityTechnologies>> listCapacities(int page, int size, String sortBy, String sortOrder) {
        return capacityPersistencePort.listCapacities(page, size, sortBy, sortOrder)
                .flatMap(capacity ->
                        technologiesPersistencePort.getTechnologiesByCapacity(capacity.getId())
                                .collectList()  // Convertimos el Flux<Technology> a List<Technology>
                                .zipWith(technologiesPersistencePort.getTechnologiesByCapacity(capacity.getId()).count()) // Obtenemos el conteo
                                .map(tuple -> new CapacityTechnologies(capacity.getId(), capacity.getName(), tuple.getT1(), capacity.getQuantityTechnologies())) // Asignamos correctamente
                )
                .collectList()
                .flatMap(capacities -> capacityPersistencePort.countCapacities()
                        .map(totalElements -> new PagedResponse<>(totalElements, page, size, capacities))
                );
    }

    @Override
    public Mono<ValidationResponse> saveCapacityBootcamp(CapacityBootcamp capacityBootcamp) {
        List<Long> requestedIds = capacityBootcamp.getCapacities();

        return Flux.fromIterable(requestedIds)
                .flatMap(capacityPersistencePort::findById) // Buscar en la BD
                .map(Capacity::getId) // Obtener solo los IDs encontrados
                .collectList()
                .flatMap(foundIds -> {
                    List<Long> missingIds = requestedIds.stream()
                            .filter(id -> !foundIds.contains(id))
                            .toList();

                    if (!missingIds.isEmpty()) {
                        return Mono.just(new ValidationResponse("Missing technologies: " + missingIds, false));
                    }

                    // Si todas existen, guardar y devolver mensaje exitoso
                    return capacityBootcampPersistencePort.saveCapacityBootcamp(capacityBootcamp)
                            .thenReturn(new ValidationResponse("Save successfully", true));
                });
    }

    private Mono<Void> validateAcceptanceCriteria(Capacity capacity) {
        if (capacity.getTechnologiesIds().size() < 3) {
            return Mono.error(new CustomException(ExceptionsEnum.MIN_TECHNOLOGIES));
        }
        if (capacity.getTechnologiesIds().size() > 20) {
            return Mono.error(new CustomException(ExceptionsEnum.MAX_TECHNOLOGIES));
        }

        Set<Long> uniqueIds = new HashSet<>();
        List<Long> duplicates = capacity.getTechnologiesIds().stream()
                .filter(id -> !uniqueIds.add(id))
                .distinct()
                .toList();

        if (!duplicates.isEmpty()) {
            return Mono.error(new CustomException(ExceptionsEnum.DUPLICATE_TECHNOLOGIES));
        }

        return Mono.empty();
    }
}
