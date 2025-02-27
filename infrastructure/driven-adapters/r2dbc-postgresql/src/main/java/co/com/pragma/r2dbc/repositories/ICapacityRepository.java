package co.com.pragma.r2dbc.repositories;

import co.com.pragma.r2dbc.entities.CapacityEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapacityRepository extends ReactiveCrudRepository<CapacityEntity,Long> {

    @Query("SELECT * FROM public.capabilities WHERE name ILIKE :name")
    Mono<CapacityEntity> findByName(String name);
    Flux<CapacityEntity> findAllBy(PageRequest pageable);

}
