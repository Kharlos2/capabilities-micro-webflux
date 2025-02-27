package co.com.pragma.r2dbc.services;

import co.com.pragma.model.capacity.models.Capacity;
import co.com.pragma.model.capacity.models.CapacityBootcamp;
import co.com.pragma.model.capacity.spi.ICapacityBootcampPersistencePort;
import co.com.pragma.r2dbc.entities.CapacityBootcampEntity;
import co.com.pragma.r2dbc.mappers.ICapacityMapper;
import co.com.pragma.r2dbc.repositories.ICapacityBootcampRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class CapacityBootcampPersistenceAdapter implements ICapacityBootcampPersistencePort {


    private final ICapacityBootcampRepository capacityBootcampRepository;
    private final ICapacityMapper capacityMapper;

    public CapacityBootcampPersistenceAdapter(ICapacityBootcampRepository capacityBootcampRepository, ICapacityMapper capacityMapper) {
        this.capacityBootcampRepository = capacityBootcampRepository;
        this.capacityMapper = capacityMapper;
    }

    @Override
    public Mono<Void> saveCapacityBootcamp(CapacityBootcamp capacityBootcamp) {

        List<CapacityBootcampEntity> entities = capacityBootcamp.getCapacities()
                .stream()
                .map(capacityId -> new CapacityBootcampEntity(capacityId, capacityBootcamp.getBootcampId()))
                .toList();

        return capacityBootcampRepository.saveAll(entities).then();
    }


    @Override
    public Flux<Capacity> findCapabilitiesByBootcampId(Long bootcampId) {
        return capacityBootcampRepository.findByBootcampId(bootcampId).map(capacityMapper::toCapacityModel);
    }
}
