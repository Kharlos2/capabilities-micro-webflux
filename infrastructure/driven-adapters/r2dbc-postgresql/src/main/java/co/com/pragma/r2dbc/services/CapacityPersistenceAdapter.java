package co.com.pragma.r2dbc.services;

import co.com.pragma.model.capacity.models.Capacity;
import co.com.pragma.model.capacity.spi.ICapacityPersistencePort;
import co.com.pragma.r2dbc.mappers.ICapacityMapper;
import co.com.pragma.r2dbc.repositories.ICapacityRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


public class CapacityPersistenceAdapter implements ICapacityPersistencePort{
    private final ICapacityRepository capacityRepository;
    private final ICapacityMapper capacityMapper;

    public CapacityPersistenceAdapter(ICapacityRepository capacityRepository, ICapacityMapper capacityMapper) {
        this.capacityRepository = capacityRepository;
        this.capacityMapper = capacityMapper;
    }

    @Override
    public Mono<Capacity> saveCapacity(Capacity capacity) {
        return capacityRepository.save(capacityMapper.toCapacityEntity(capacity)).map(capacityMapper::toCapacityModel);
    }

    @Override
    public Mono<Void> deleteCapacity(Long id) {
        return capacityRepository.deleteById(id);
    }

    @Override
    public Mono<Capacity> findByName(String name) {
        return capacityRepository.findByName(name).map(capacityMapper::toCapacityModel);
    }



}
