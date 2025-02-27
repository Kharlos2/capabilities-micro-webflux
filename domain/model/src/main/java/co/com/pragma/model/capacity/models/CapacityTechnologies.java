package co.com.pragma.model.capacity.models;

import java.util.List;

public class CapacityTechnologies {

    private Long id;
    private String name;
    private List<Technology> technologies;
    private Long countTechnologies;

    public CapacityTechnologies(Long id, String name, List<Technology> technologies, Long countTechnologies) {
        this.id = id;
        this.name = name;
        this.technologies = technologies;
        this.countTechnologies = countTechnologies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Technology> technologies) {
        this.technologies = technologies;
    }

    public Long getCountTechnologies() {
        return countTechnologies;
    }

    public void setCountTechnologies(Long countTechnologies) {
        this.countTechnologies = countTechnologies;
    }
}
