package co.com.pragma.r2dbc.repositories;

import co.com.pragma.r2dbc.entities.CapacityBootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ICapacityBootcampRepository extends ReactiveCrudRepository<CapacityBootcampEntity, Long> {
}
