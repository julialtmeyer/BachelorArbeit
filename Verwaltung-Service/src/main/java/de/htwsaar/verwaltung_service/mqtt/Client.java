package de.htwsaar.verwaltung_service.mqtt;

import de.htwsaar.verwaltung_service.Configuration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;


/**
 * The type Client.
 */
@Component
@Scope("prototype")
public class Client {

    /**
     * The Client.
     */
    public MqttClient client;

    private final Configuration config;

    final Logger logger = LoggerFactory.getLogger(Client.class);

    /**
     * Instantiates a new Client.
     *
     */
    public Client(Configuration config){
        this.config = config;
        config(config.isUse_certificate());
    }

    private void config(boolean cert){
        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectOptions connectionOptions = new MqttConnectOptions();
        String uri = "tcp://" + config.getBrokerHost() + ":" + config.getBrokerPort();
        try {


            if(cert){
                this.client = new MqttClient(config.getServerUrl(), MqttClient.generateClientId(), persistence);
                connectionOptions.setUserName(config.getUsername());
                connectionOptions.setPassword(config.getPassword().toCharArray());

                SSLSocketFactory socketFactory = getSocketFactory(config.getCaCertificate(), config.getClientCertifiacte(), config.getClientKey());
                connectionOptions.setSocketFactory(socketFactory);
            }
            else {
                this.client = new MqttClient(uri, MqttClient.generateClientId(), persistence);
            }
            connectionOptions.setCleanSession(true);

            logger.info("starting connect the server...");
            client.connect(connectionOptions);
            logger.info("connected!");
        } catch (Exception me) {
            logger.error("Failed to connect to the mqtt-server {}",uri, me);
        }
    }

    private static SSLSocketFactory getSocketFactory(final String caCrtFile, final String crtFile, final String keyFile)
            throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // load CA certificate
        X509Certificate caCert = null;

        InputStream stream = new ByteArrayInputStream(caCrtFile.getBytes(StandardCharsets.UTF_8));
        BufferedInputStream bis = new BufferedInputStream(stream);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        while (bis.available() > 0) {
            caCert = (X509Certificate) cf.generateCertificate(bis);
            // System.out.println(caCert.toString());
        }

        // load client certificate
        bis = new BufferedInputStream(new ByteArrayInputStream(crtFile.getBytes(StandardCharsets.UTF_8)));
        X509Certificate cert = null;
        while (bis.available() > 0) {
            cert = (X509Certificate) cf.generateCertificate(bis);
            // System.out.println(caCert.toString());
        }

        // load client private key
        byte[] keyArray = Base64.getDecoder().decode(keyFile);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyArray);
        PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        // CA certificate is used to authenticate server
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(caKs);

        // client key and certificates are sent to server so it can authenticate
        // us
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("certificate", cert);
        ks.setKeyEntry("private-key", key, "".toCharArray(),
                new java.security.cert.Certificate[] { cert });
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                .getDefaultAlgorithm());
        kmf.init(ks, "".toCharArray());

        // finally, create SSL socket factory
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }
}
