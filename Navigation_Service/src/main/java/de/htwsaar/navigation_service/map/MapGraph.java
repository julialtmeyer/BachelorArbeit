package de.htwsaar.navigation_service.map;

import java.util.List;

public class MapGraph {

    private String name;
    private Integer id;
    private List<Node> nodes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Node getNodeByID(Long id)  throws Exception
    {
        for(Node node : nodes){
            if(id.equals(node.getId())){
                return node;
            }
        }
        throw new Exception("No Node with " + id + " id");

    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
