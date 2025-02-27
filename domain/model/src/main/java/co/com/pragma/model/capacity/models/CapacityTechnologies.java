package co.com.pragma.model.capacity.models;

import java.util.List;

public class CapacityTechnologies {

    private Long id;
    private String name;
    private List<Technology> technologies;
    private int technologiesCount;

    public CapacityTechnologies(Long id, String name, List<Technology> technologies,int technologiesCount) {
        this.id = id;
        this.name = name;
        this.technologies = technologies;
        this.technologiesCount = technologiesCount;
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

    public int  getTechnologiesCount() {
        return technologiesCount;
    }

    public void setTechnologiesCount(int  technologiesCount) {
        this.technologiesCount = technologiesCount;
    }
}
