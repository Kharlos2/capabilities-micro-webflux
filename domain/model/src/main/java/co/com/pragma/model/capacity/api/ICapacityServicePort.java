package co.com.pragma.model.capacity.api;

import co.com.pragma.model.capacity.models.Capacity;
import co.com.pragma.model.capacity.models.CapacityTechnologies;
import co.com.pragma.model.capacity.models.PagedResponse;
import reactor.core.publisher.Mono;

public interface ICapacityServicePort {

    Mono<Capacity> saveCapacity (Capacity capacity);
    Mono<PagedResponse<CapacityTechnologies>> listCapacities(int page, int size, String sortBy, String sortOrder);
}
