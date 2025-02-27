package co.com.pragma.api.handlers;

import co.com.pragma.api.dto.CapacityForBootcampDTO;
import co.com.pragma.api.dto.SaveCapacityBootcampDTO;
import co.com.pragma.api.dto.SaveCapacityDTO;
import co.com.pragma.api.mappers.ICapacityHandlerMapper;
import co.com.pragma.model.capacity.api.ICapacityServicePort;
import co.com.pragma.model.capacity.models.Capacity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
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
    public Mono<ServerResponse> listCapacities(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        String sortBy = request.queryParam("sortBy").orElse("name");
        String sortOrder = request.queryParam("sortOrder").orElse("asc");

        return capacityServicePort.listCapacities(page, size, sortBy, sortOrder)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
                );
    }

    public Mono<ServerResponse> saveCapacityBootcamp(ServerRequest request) {
        return request.bodyToMono(SaveCapacityBootcampDTO.class)
                .flatMap(capacityBootcamp ->
                        capacityServicePort.saveCapacityBootcamp(capacityHandlerMapper.toCapacityBootcampModel(capacityBootcamp))
                                .flatMap(response -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response)
                                )
                );
    }
    public Mono<ServerResponse> findCapabilitiesByBootcamp (ServerRequest request){
        Long capacityId = Long.parseLong(request.queryParam("bootcampId").orElse("0"));

        Flux<CapacityForBootcampDTO> technologyFlux =
                capacityServicePort.findCapabilitiesByBootcamp(capacityId)
                        .map(capacityHandlerMapper::toCapacityForBootcamp);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(technologyFlux, CapacityForBootcampDTO.class);
    }

}
