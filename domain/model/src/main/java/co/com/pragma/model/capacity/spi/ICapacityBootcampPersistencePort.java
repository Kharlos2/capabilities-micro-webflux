package co.com.pragma.model.capacity.spi;

import co.com.pragma.model.capacity.models.Capacity;
import co.com.pragma.model.capacity.models.CapacityBootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapacityBootcampPersistencePort {

    Mono<Void>  saveCapacityBootcamp(CapacityBootcamp capacityBootcamp);
    Flux<Capacity> findCapabilitiesByBootcampId(Long bootcampId);
}
