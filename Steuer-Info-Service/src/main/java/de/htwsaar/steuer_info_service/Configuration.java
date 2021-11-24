package de.htwsaar.steuer_info_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;


@org.springframework.context.annotation.Configuration
@PropertySource("classpath:/application.properties")
public class Configuration {

    @Value("${clientKey}")
    private String clientKey;

    @Value("${caCertificate}")
    private String caCertificate;

    @Value("${clientCertificate}")
    private String clientCertifiacte;

    @Value("${brokeruser}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${serverUrl}")
    private String serverUrl;

    @Value("${brokerHost}")
    private String brokerHost;

    @Value("${brokerPort}")
    private Integer brokerPort;

    @Value("${topicRoot}")
    private String topicRoot;

    @Value("${subTopicInformation}")
    private String subTopicInformation;

    @Value("${subTopicDrive}")
    private String subTopicDrive;

    @Value("${useCertificate}")
    private boolean use_certificate;

    @Value("${navigationURL}")
    private String navigationUrl;

    @Value("${verwaltungURL}")
    private String verwaltungUrl;

    @Value("${mapScaleFactor}")
    private Double mapScaleFactor;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getBrokerHost() {
        return brokerHost;
    }

    public int getBrokerPort() {
        return brokerPort;
    }

    public boolean isUse_certificate() {
        return use_certificate;
    }

    public String getClientKey() {
        return clientKey;
    }

    public String getCaCertificate() {
        return caCertificate;
    }

    public String getClientCertifiacte() {
        return clientCertifiacte;
    }

    public String getTopicRoot() {
        return topicRoot;
    }

    public String getSubTopicInformation() {
        return subTopicInformation;
    }

    public String getSubTopicDrive() {
        return subTopicDrive;
    }

    public String getNavigationUrl(){
        return navigationUrl;
    }

    public String getVerwaltungUrl() {
        return verwaltungUrl;
    }

    public Double getMapScaleFactor() {
        return mapScaleFactor;
    }
}
