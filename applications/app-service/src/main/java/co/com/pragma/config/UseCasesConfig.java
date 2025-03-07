package co.com.pragma.config;

import co.com.pragma.model.capacity.api.ICapacityServicePort;
import co.com.pragma.model.capacity.spi.ICapacityPersistencePort;
import co.com.pragma.model.capacity.spi.ITechnologiesPersistencePort;
import co.com.pragma.r2dbc.mappers.ICapacityMapper;
import co.com.pragma.r2dbc.repositories.ICapacityRepository;
import co.com.pragma.r2dbc.services.CapacityPersistenceAdapter;
import co.com.pragma.r2dbc.services.TechnologiesPersistenceAdapter;
import co.com.pragma.usecase.capacity.CapacityUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UseCasesConfig {

        private final ICapacityRepository capacityRepository;
        private final ICapacityMapper capacityMapper;
        private final WebClient webClient;

        public UseCasesConfig(ICapacityRepository capacityRepository, ICapacityMapper capacityMapper, WebClient webClient) {
                this.capacityRepository = capacityRepository;
                this.capacityMapper = capacityMapper;
            this.webClient = webClient;
        }

        @Bean
        public ICapacityPersistencePort capacityPersistencePort(){
                return new CapacityPersistenceAdapter(capacityRepository,capacityMapper);
        }

        @Bean
        public ITechnologiesPersistencePort technologiesPersistencePort(){
                return new TechnologiesPersistenceAdapter(webClient,capacityMapper);
        }

        @Bean
        public ICapacityServicePort capacityServicePort(ICapacityPersistencePort capacityPersistencePort, ITechnologiesPersistencePort technologiesPersistencePort){
                return new CapacityUseCase(capacityPersistencePort,technologiesPersistencePort);
        }

}
