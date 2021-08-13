package de.htwsaar.navigation_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;


@org.springframework.context.annotation.Configuration
@PropertySource("classpath:/application.properties")
public class Configuration {

    @Value("${nodeToleranz}")
    private Double nodeToleranz;

    @Value("${mapId}")
    private Long mapId;

    @Value("${gridSpacing}")
    private Integer gridSpacing;

    public Double getNodeToleranz() {
        return nodeToleranz;
    }

    public Long getMapId() {
        return mapId;
    }

    public Integer getGridSpacing() {
        return gridSpacing;
    }
}
