package co.com.pragma.r2dbc.entities;

import java.util.List;

public class TechnologiesCapacityEntity {

    private Long capacityId;
    private List<Long> technologiesIds;

    public TechnologiesCapacityEntity(Long capacityId, List<Long> technologiesIds) {
        this.capacityId = capacityId;
        this.technologiesIds = technologiesIds;
    }

    public Long getCapacityId() {
        return capacityId;
    }

    public void setCapacityId(Long capacityId) {
        this.capacityId = capacityId;
    }

    public List<Long> getTechnologiesIds() {
        return technologiesIds;
    }

    public void setTechnologiesIds(List<Long> technologiesIds) {
        this.technologiesIds = technologiesIds;
    }
}
