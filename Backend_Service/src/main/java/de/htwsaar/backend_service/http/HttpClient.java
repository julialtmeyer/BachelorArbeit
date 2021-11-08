package de.htwsaar.backend_service.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.htwsaar.backend_service.Configuration;
import de.htwsaar.backend_service.Naviagtion.Node;
import de.htwsaar.backend_service.model.Robot;
import de.htwsaar.backend_service.model.Robot_Info;
import org.locationtech.jts.geom.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class HttpClient {

    private final Configuration config;

    final Logger logger = LoggerFactory.getLogger(HttpClient.class);

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
            logger.error("Failed to create JSON {}", response.getBody(), e);
        }

        return nodeList;
    }

    public List<Robot> createVerwaltungGetRequest() throws URISyntaxException {
        List<Robot> robotList = new LinkedList<>();
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = config.getVerwaltungUrl() + "/";
        URI uri = new URI(baseUrl);

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        if(response.getStatusCode().is2xxSuccessful()){
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            try {
                robotList = objectMapper.readValue(response.getBody(), new TypeReference<>(){});
            }
            catch (JsonProcessingException e){
                logger.error("Failed to create JSON {}", response.getBody(), e);
            }
        }
        else {
            logger.error("HTTP-Request failed. {}", response.getStatusCode());
        }

        return robotList;
    }
}
