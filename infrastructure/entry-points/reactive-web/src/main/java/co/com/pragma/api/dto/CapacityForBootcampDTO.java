package co.com.pragma.api.dto;

import co.com.pragma.model.capacity.models.Technology;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapacityForBootcampDTO {
    private Long id;
    private String name;
    private List<Technology> technologies;
    private int quantityTechnologies;
}
