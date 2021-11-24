package de.htwsaar.steuer_info_service;

import de.htwsaar.steuer_info_service.Naviagtion.Node;
import de.htwsaar.steuer_info_service.http.HttpClient;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class HTTPRequestTests {

    @Autowired
    private HttpClient httpClient;

    @Test
    public void testPostNacigationRequestToService(){
        List<Node> list = httpClient.createNavigationPostRequest(new Coordinate(595.0, 444.0),new Coordinate(1007.0, 444.0));
        assert list.size() == 2;
    }
}
