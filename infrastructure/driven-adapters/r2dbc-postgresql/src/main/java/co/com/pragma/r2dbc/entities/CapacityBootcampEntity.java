package co.com.pragma.r2dbc.entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("public.capacity_bootcamp")
public class CapacityBootcampEntity {

    @Id
    private Long id;
    private Long capacityId;
    private Long bootcampId;

    public CapacityBootcampEntity(Long id, Long capacityId, Long bootcampId) {
        this.id = id;
        this.capacityId = capacityId;
        this.bootcampId = bootcampId;
    }

    public CapacityBootcampEntity(Long capacityId, Long bootcampId) {
        this.capacityId = capacityId;
        this.bootcampId = bootcampId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCapacityId() {
        return capacityId;
    }

    public void setCapacityId(Long capacityId) {
        this.capacityId = capacityId;
    }

    public Long getBootcampId() {
        return bootcampId;
    }

    public void setBootcampId(Long bootcampId) {
        this.bootcampId = bootcampId;
    }
}

