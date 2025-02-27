package co.com.pragma.model.capacity.api;

import co.com.pragma.model.capacity.models.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapacityServicePort {

    Mono<Capacity> saveCapacity (Capacity capacity);
    Mono<PagedResponse<CapacityTechnologies>> listCapacities(int page, int size, String sortBy, String sortOrder);
    Mono<ValidationResponse> saveCapacityBootcamp (CapacityBootcamp capacityBootcamp);
    Flux<CapacityTechnologies> findCapabilitiesByBootcamp (Long bootcampId);
}
