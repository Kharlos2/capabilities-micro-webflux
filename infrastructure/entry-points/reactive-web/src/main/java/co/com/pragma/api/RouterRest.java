package co.com.pragma.api;

import co.com.pragma.api.dto.CapacityForBootcampDTO;
import co.com.pragma.api.dto.ErrorModel;
import co.com.pragma.api.dto.SaveCapacityBootcampDTO;
import co.com.pragma.api.dto.SaveCapacityDTO;
import co.com.pragma.api.handlers.Handler;
import co.com.pragma.model.capacity.models.Capacity;
import co.com.pragma.model.capacity.models.ValidationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {


    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/capacity",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "saveCapacity",
                    operation = @Operation(
                            operationId = "saveCapacity",
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SaveCapacityDTO.class))),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Create capacity success.",
                                            content = @Content(schema = @Schema(implementation = Capacity.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Solicitud incorrecta.",
                                            content = @Content(schema = @Schema(implementation = ErrorModel.class))

                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/capacity",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listCapacities",
                    operation = @Operation(
                            operationId = "listCapacities",
                            summary = "Listar capacidades con paginación",
                            parameters = {
                                    @Parameter(name = "page", description = "Número de página", schema = @Schema(type = "integer", defaultValue = "0")),
                                    @Parameter(name = "size", description = "Tamaño de la página", schema = @Schema(type = "integer", defaultValue = "10")),
                                    @Parameter(name = "sortBy", description = "Campo para ordenar (name/quantityTechnologies)", schema = @Schema(type = "string", defaultValue = "name")),
                                    @Parameter(name = "sortOrder", description = "Orden de clasificación (asc/desc)", schema = @Schema(type = "string", defaultValue = "asc"))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Lista de capacidades.",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Capacity.class)))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/capacity-bootcamp",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "saveCapacityBootcamp",
                    operation = @Operation(
                            operationId = "saveCapacityBootcamp",
                            summary = "Asociar una lista de capacidades a un bootcamp",
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SaveCapacityBootcampDTO.class))),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Se ejecuto correctamente el proceso",
                                            content = @Content(schema = @Schema(implementation = ValidationResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Error por no encontrar capacidad en base de datos",
                                            content = @Content(schema = @Schema(implementation = ValidationResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/capacity-bootcamp",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "findCapabilitiesByBootcamp",
                    operation = @Operation(
                            operationId = "findCapabilitiesByBootcamp",
                            summary = "Obtener capacidades de un bootcamp",
                            parameters = {
                                    @Parameter(name = "bootcampId", description = "ID del bootcamp", required = true, schema = @Schema(type = "integer"))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Lista de capacidades del bootcamp.",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CapacityForBootcampDTO.class)))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route().POST("/api/capacity", handler::saveCapacity)
                .GET("/api/capacity", handler::listCapacities)
                .POST("/api/capacity-bootcamp", handler::saveCapacityBootcamp)
                .GET("/api/capacity-bootcamp", handler::findCapabilitiesByBootcamp)
                .build()
                ;
    }


    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl("http://localhost:8080/api").build();
    }
}
