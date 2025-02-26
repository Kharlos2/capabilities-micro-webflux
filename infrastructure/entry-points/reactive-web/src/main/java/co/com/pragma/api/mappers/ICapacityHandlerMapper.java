package co.com.pragma.api.mappers;

import co.com.pragma.api.dto.SaveCapacityDTO;
import co.com.pragma.model.capacity.models.Capacity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICapacityHandlerMapper {

    Capacity toModel (SaveCapacityDTO saveCapacityDTO);

}
