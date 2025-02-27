package co.com.pragma.r2dbc.services;

import co.com.pragma.model.capacity.models.Capacity;
import co.com.pragma.model.capacity.models.TechnologiesIds;
import co.com.pragma.model.capacity.models.ValidationResponse;
import co.com.pragma.model.capacity.spi.ITechnologiesPersistencePort;
import co.com.pragma.r2dbc.mappers.ICapacityMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

public class TechnologiesPersistenceAdapter implements ITechnologiesPersistencePort {

    private final WebClient webClient;
    private final ICapacityMapper capacityMapper;

    public TechnologiesPersistenceAdapter(WebClient webClient, ICapacityMapper capacityMapper) {
        this.webClient = webClient;
        this.capacityMapper = capacityMapper;
    }


    @Override
    public Mono<ValidationResponse> checkTechnologies(List<Long> technologiesIds) {
        return webClient.post().uri("/validation-technologies")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TechnologiesIds(technologiesIds))
                .retrieve()
                .bodyToMono(ValidationResponse.class);
    }

    @Override
    public Mono<Boolean> saveTechnologiesCapacities(Capacity capacity) {
        System.out.println(capacity.getTechnologiesIds().isEmpty());
        return webClient.post()
                .uri("/technologies-capacity")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(capacityMapper.toTechnologiesCapacity(capacity))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.just(new RuntimeException("Error 4xx en la petición")))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.just(new RuntimeException("Error 5xx en la petición")))
                .bodyToMono(Void.class) // No nos interesa el cuerpo de la respuesta
                .thenReturn(Boolean.TRUE) // Si llega aquí, la operación fue exitosa
                .onErrorResume(error -> Mono.just(Boolean.FALSE)); //
    }
}
