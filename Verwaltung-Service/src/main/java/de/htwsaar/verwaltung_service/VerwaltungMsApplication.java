package de.htwsaar.verwaltung_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VerwaltungMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(VerwaltungMsApplication.class, args);
    }

}
