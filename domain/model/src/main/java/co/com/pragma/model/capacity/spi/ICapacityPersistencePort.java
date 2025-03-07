package co.com.pragma.model.capacity.spi;

import co.com.pragma.model.capacity.models.Capacity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapacityPersistencePort {

    Mono<Capacity> saveCapacity (Capacity capacity);
    Mono<Void> deleteCapacity (Long id);
    Mono<Capacity> findByName(String name);

}
