package de.htwsaar.navigation_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;


@org.springframework.context.annotation.Configuration
@PropertySource("classpath:/application.properties")
public class Configuration {

    @Value("${nodeToleranzY}")
    private Double nodeToleranceY;

    @Value("${nodeToleranzX}")
    private Double nodeToleranceX;

    @Value("${mapId}")
    private Long mapId;

    @Value("${gridSpacing}")
    private Integer gridSpacing;

    public Double getNodeToleranceY() {
        return nodeToleranceY;
    }

    public Double getNodeToleranceX() {
        return nodeToleranceX;
    }

    public Long getMapId() {
        return mapId;
    }

    public Integer getGridSpacing() {
        return gridSpacing;
    }
}
