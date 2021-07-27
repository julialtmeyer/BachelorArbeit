package de.htwsaar.backend_service.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.backend_service.Configuration;
import de.htwsaar.backend_service.Naviagtion.Coordinate;
import de.htwsaar.backend_service.Naviagtion.Node;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class HttpClient {

    private final Configuration config;

    public HttpClient(Configuration config) {
        this.config = config;
    }

    public List<Node> createNavigationPostRequest(Coordinate start, Coordinate dest){
        List<Node> nodeList = new LinkedList<>();
        HttpHeaders httpHeaders = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        NavigationPost navigationPost = new NavigationPost(start.getX(), start.getY(), dest.getX(), dest.getY());

        HttpEntity<NavigationPost> entity = new HttpEntity<>(navigationPost, httpHeaders);

        ResponseEntity<String> response = restTemplate.postForEntity(config.getNavigationUrl(), entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            TypeReference<List<Node>> mapType = new TypeReference<List<Node>>() {};
            nodeList = objectMapper.readValue(response.getBody(), mapType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return nodeList;
    }
}
