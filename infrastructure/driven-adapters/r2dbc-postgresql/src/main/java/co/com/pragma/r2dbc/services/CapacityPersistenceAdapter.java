package co.com.pragma.r2dbc.services;

import co.com.pragma.model.capacity.models.Capacity;
import co.com.pragma.model.capacity.spi.ICapacityPersistencePort;
import co.com.pragma.r2dbc.mappers.ICapacityMapper;
import co.com.pragma.r2dbc.repositories.ICapacityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
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

    @Override
    public Flux<Capacity> listCapacities(int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return capacityRepository.findAllBy(pageRequest)
                .map(capacityMapper::toCapacityModel);
    }

    @Override
    public Mono<Long> countCapacities() {
        return capacityRepository.count();
    }

    @Override
    public Mono<Capacity> findById(Long id) {
        return capacityRepository.findById(id).map(capacityMapper::toCapacityModel);
    }


}
