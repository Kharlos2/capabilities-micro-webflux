package co.com.pragma.r2dbc.services;

import co.com.pragma.model.capacity.models.CapacityBootcamp;
import co.com.pragma.model.capacity.spi.ICapacityBootcampPersistencePort;
import co.com.pragma.r2dbc.entities.CapacityBootcampEntity;
import co.com.pragma.r2dbc.repositories.ICapacityBootcampRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public class CapacityBootcampPersistenceAdapter implements ICapacityBootcampPersistencePort {


    private final ICapacityBootcampRepository capacityBootcampRepository;

    public CapacityBootcampPersistenceAdapter(ICapacityBootcampRepository capacityBootcampRepository) {
        this.capacityBootcampRepository = capacityBootcampRepository;
    }

    @Override
    public Mono<Void> saveCapacityBootcamp(CapacityBootcamp capacityBootcamp) {

        List<CapacityBootcampEntity> entities = capacityBootcamp.getCapacities()
                .stream()
                .map(capacityId -> new CapacityBootcampEntity(capacityId, capacityBootcamp.getBootcampId()))
                .toList();

        return capacityBootcampRepository.saveAll(entities).then();
    }
}
