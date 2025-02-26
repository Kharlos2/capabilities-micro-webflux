package co.com.pragma.r2dbc.repositories;

import co.com.pragma.r2dbc.entities.CapacityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ICapacityRepository extends ReactiveCrudRepository<CapacityEntity,Long> {
}
