package co.com.pragma.api;

import co.com.pragma.api.handlers.Handler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/capacity"), handler::saveCapacity)
                .andRoute(GET("/api/capacity"), handler::listCapacities)

                ;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl("http://localhost:8080/api").build();
    }
}
