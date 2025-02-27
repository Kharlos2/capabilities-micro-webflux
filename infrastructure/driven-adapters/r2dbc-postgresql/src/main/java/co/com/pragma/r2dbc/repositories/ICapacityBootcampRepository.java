package co.com.pragma.r2dbc.repositories;

import co.com.pragma.r2dbc.entities.CapacityBootcampEntity;
import co.com.pragma.r2dbc.entities.CapacityEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ICapacityBootcampRepository extends ReactiveCrudRepository<CapacityBootcampEntity, Long> {

    @Query("""
            SELECT c.id, c.name, c.description, c.quantity_technologies
            FROM public.capabilities c
            INNER JOIN public.capacity_bootcamp cb ON c.id = cb.capacity_id
            WHERE cb.bootcamp_id = :bootcampId
    """)
    Flux<CapacityEntity> findByBootcampId(Long bootcampId);

}
