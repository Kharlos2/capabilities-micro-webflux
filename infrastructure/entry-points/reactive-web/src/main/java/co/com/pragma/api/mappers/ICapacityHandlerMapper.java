package co.com.pragma.api.mappers;

import co.com.pragma.api.dto.CapacityForBootcampDTO;
import co.com.pragma.api.dto.SaveCapacityBootcampDTO;
import co.com.pragma.api.dto.SaveCapacityDTO;
import co.com.pragma.model.capacity.models.Capacity;
import co.com.pragma.model.capacity.models.CapacityBootcamp;
import co.com.pragma.model.capacity.models.CapacityTechnologies;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICapacityHandlerMapper {

    Capacity toModel (SaveCapacityDTO saveCapacityDTO);
    CapacityBootcamp toCapacityBootcampModel (SaveCapacityBootcampDTO saveCapacityBootcampDTO);

    @Mapping(source = "technologies", target = "technologies")
    @Mapping(source = "technologiesCount", target = "quantityTechnologies")
    CapacityForBootcampDTO toCapacityForBootcamp (CapacityTechnologies capacityTechnologies);
}
