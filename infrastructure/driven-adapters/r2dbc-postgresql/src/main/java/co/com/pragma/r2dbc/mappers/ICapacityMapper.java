package co.com.pragma.r2dbc.mappers;

import co.com.pragma.model.capacity.models.Capacity;
import co.com.pragma.r2dbc.entities.CapacityEntity;
import co.com.pragma.r2dbc.entities.TechnologiesCapacityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICapacityMapper {

    CapacityEntity toCapacityEntity(Capacity capacity);

    Capacity toCapacityModel(CapacityEntity capacityEntity);

    @Mapping(source = "id", target = "capacityId")
    @Mapping(source = "technologiesIds", target = "technologiesIds")
    TechnologiesCapacityEntity toTechnologiesCapacity(Capacity capacity);

}
