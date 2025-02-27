package co.com.pragma.model.capacity.models;

import java.util.List;


public class Capacity {

    private Long id;
    private String name;
    private String description;
    private List<Long> technologiesIds;
    private int quantityTechnologies;

    public Capacity(Long id, String name, String description, List<Long> technologiesIds, int quantityTechnologies) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.technologiesIds = technologiesIds;
        this.quantityTechnologies = quantityTechnologies;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getTechnologiesIds() {
        return technologiesIds;
    }

    public void setTechnologiesIds(List<Long> technologiesIds) {
        this.technologiesIds = technologiesIds;
    }

    public int getQuantityTechnologies() {
        return quantityTechnologies;
    }

    public void setQuantityTechnologies(int quantityTechnologies) {
        this.quantityTechnologies = quantityTechnologies;
    }
}
