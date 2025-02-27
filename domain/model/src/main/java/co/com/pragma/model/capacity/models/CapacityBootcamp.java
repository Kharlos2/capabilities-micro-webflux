package co.com.pragma.model.capacity.models;

import java.util.List;

public class CapacityBootcamp {

    private Long id;
    private List<Long> capacities;
    private Long bootcampId;

    public CapacityBootcamp(Long id, List<Long> capacities, Long bootcampId) {
        this.id = id;
        this.capacities = capacities;
        this.bootcampId = bootcampId;
    }


    public CapacityBootcamp() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getCapacities() {
        return capacities;
    }

    public void setCapacities(List<Long> capacities) {
        this.capacities = capacities;
    }

    public Long getBootcampId() {
        return bootcampId;
    }

    public void setBootcampId(Long bootcampId) {
        this.bootcampId = bootcampId;
    }
}
