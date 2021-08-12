package de.htwsaar.navigation_service.map;


import javax.persistence.*;

@Entity
@Table(name = "MAPS")
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "map_json", unique = false, columnDefinition = "json")
    private String mapJSON;

    public Map(Long id, String mapJSON) {
        this.id = id;
        this.mapJSON = mapJSON;
    }

    public Map() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMapJSON() {
        return mapJSON;
    }

    public void setMapJSON(String mapJSON) {
        this.mapJSON = mapJSON;
    }
}
