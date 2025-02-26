package co.com.pragma.api.handlers;

import co.com.pragma.api.dto.SaveCapacityDTO;
import co.com.pragma.api.mappers.ICapacityHandlerMapper;
import co.com.pragma.model.capacity.api.ICapacityServicePort;
import co.com.pragma.model.capacity.models.Capacity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class Handler {

    private final ICapacityServicePort capacityServicePort;
    private final ICapacityHandlerMapper capacityHandlerMapper;

    public Handler(ICapacityServicePort capacityServicePort, ICapacityHandlerMapper capacityHandlerMapper) {
        this.capacityServicePort = capacityServicePort;
        this.capacityHandlerMapper = capacityHandlerMapper;
    }


    public Mono<ServerResponse> saveCapacity (ServerRequest serverRequest) {

        Mono<Capacity> capacityMono = serverRequest.bodyToMono(SaveCapacityDTO.class).map(capacityHandlerMapper::toModel);
        return capacityMono.flatMap(saveCapacity->
            ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(capacityServicePort.saveCapacity(saveCapacity), Capacity.class)
        );
    }
}
