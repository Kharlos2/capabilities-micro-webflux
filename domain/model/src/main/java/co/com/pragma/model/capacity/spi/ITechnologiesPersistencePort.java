package co.com.pragma.model.capacity.spi;

import co.com.pragma.model.capacity.models.Capacity;
import co.com.pragma.model.capacity.models.ValidationResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologiesPersistencePort {

    Mono<ValidationResponse> checkTechnologies (List<Long> technologiesIds);
    Mono<Boolean> saveTechnologiesCapacities(Capacity capacity);

}
