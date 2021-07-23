package de.htwsaar.verwaltung_ms;

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

    @Value("${subscribeTopic}")
    private String topic;

    @Value("${useCertificate}")
    private boolean use_certificate;

    @Value("${brokerPort}")
    private int brokerPort;

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

    public String getTopic() {
        return topic;
    }

    public int getBrokerPort() {
        return brokerPort;
    }
}
