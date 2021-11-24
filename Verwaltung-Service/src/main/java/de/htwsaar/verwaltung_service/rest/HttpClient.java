package de.htwsaar.verwaltung_service.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.verwaltung_service.Configuration;
import de.htwsaar.verwaltung_service.data.Robot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Component
public class HttpClient {

    private final Configuration config;

    final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    public HttpClient(Configuration config) {
        this.config = config;
    }

    public Optional<Robot> createPositionGetRequest(Long id) {
        Optional<Robot> robot = Optional.empty();
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = config.getControlInfoServiceUrl() + "/robots/" + id;
        URI uri = null;
        try {
            uri = new URI(baseUrl);
        } catch (URISyntaxException e) {
            logger.error("Failed to create URI! {}", baseUrl, e);
        }

        assert uri != null;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            if(response.getStatusCode().is2xxSuccessful()){
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.findAndRegisterModules();
                try {
                    robot = Optional.of(objectMapper.readValue(response.getBody(), new TypeReference<>(){}));
                }
                catch (JsonProcessingException e){
                    logger.error("Failed to create JSON {}", response.getBody(), e);
                }
            }
            else {
                logger.error("HTTP-Request failed. {}", response.getStatusCode());
            }
        }
        catch (RestClientException e){
            logger.error("Error connecting to resource at url {}. {}", baseUrl, e.getCause());
        }

        return robot;
    }
}
