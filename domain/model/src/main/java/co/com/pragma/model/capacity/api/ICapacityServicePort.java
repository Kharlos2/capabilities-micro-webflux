package co.com.pragma.model.capacity.api;

import co.com.pragma.model.capacity.models.Capacity;
import reactor.core.publisher.Mono;

public interface ICapacityServicePort {

    Mono<Capacity> saveCapacity (Capacity capacity);

}
