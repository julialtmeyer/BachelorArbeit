package de.htwsaar.backend_service;

import de.htwsaar.backend_service.Naviagtion.Coordinate;
import de.htwsaar.backend_service.Naviagtion.Node;
import de.htwsaar.backend_service.rest.HttpClient;
import de.htwsaar.backend_service.rest.NavigationPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class HTTPRequestTest {

    @Autowired
    private HttpClient httpClient;

    @Test
    public void testPostNacigationRequestToService(){
        List<Node> list = httpClient.createNavigationPostRequest(new Coordinate(595.0, 444.0),new Coordinate(1007.0, 444.0));
        assert list.size() == 2;
    }
}
