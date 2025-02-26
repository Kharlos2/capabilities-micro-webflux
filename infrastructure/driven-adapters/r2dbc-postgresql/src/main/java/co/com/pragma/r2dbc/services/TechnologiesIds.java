package co.com.pragma.r2dbc.services;

import java.util.List;

public class TechnologiesIds {
    private List<Long> technologiesIds;

    public TechnologiesIds(List<Long> technologiesIds) {
        this.technologiesIds = technologiesIds;
    }

    public List<Long> getTechnologiesIds() {
        return technologiesIds;
    }

    public void setTechnologiesIds(List<Long> technologiesIds) {
        this.technologiesIds = technologiesIds;
    }
}
