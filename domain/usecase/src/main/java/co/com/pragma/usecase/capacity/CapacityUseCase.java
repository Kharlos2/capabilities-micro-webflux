package co.com.pragma.usecase.capacity;

import co.com.pragma.model.capacity.api.ICapacityServicePort;
import co.com.pragma.model.capacity.exceptions.CustomException;
import co.com.pragma.model.capacity.exceptions.ExceptionsEnum;
import co.com.pragma.model.capacity.exceptions.HttpException;
import co.com.pragma.model.capacity.models.Capacity;
import co.com.pragma.model.capacity.spi.ICapacityPersistencePort;
import co.com.pragma.model.capacity.spi.ITechnologiesPersistencePort;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CapacityUseCase implements ICapacityServicePort {

    private final ICapacityPersistencePort capacityPersistencePort;
    private final ITechnologiesPersistencePort technologiesPersistencePort;

    public CapacityUseCase(ICapacityPersistencePort capacityPersistencePort, ITechnologiesPersistencePort technologiesPersistencePort) {
        this.capacityPersistencePort = capacityPersistencePort;
        this.technologiesPersistencePort = technologiesPersistencePort;
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
