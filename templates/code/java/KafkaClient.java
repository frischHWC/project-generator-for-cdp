package {{ package_name }}.client;

import {{ package_name }}.config.AppConfig;
import {{ package_name }}.config.Utils;
{% if logger is sameas true %}import org.apache.log4j.Logger;{% endif %}
import org.apache.hadoop.conf.Configuration;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import java.time.Duration;
import java.util.Collections;


public class KafkaClient {

  {% if logger is sameas true %}private static Logger logger = Logger.getLogger(KafkaClient.class);{% endif %}

  Properties props = new Properties();

  public KafkaClient() {

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.getProperty("kafka.brokers"));
    props.put(ProducerConfig.ACKS_CONFIG, "all");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "tester");

    String securityProtocol = AppConfig.getProperty("kafka.security_protocol");
    props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);

    //Kerberos config
        if (securityProtocol.equalsIgnoreCase("SASL_PLAINTEXT") || securityProtocol.equalsIgnoreCase("SASL_SSL")) {
            Utils.createJaasConfigFile("kafka-jaas-randomdatagen.config", "KafkaClient",
                    AppConfig.getProperty("kerberos.keytab"), AppConfig.getProperty("kerberos.user"),
                    true, false, false);
            Utils.createJaasConfigFile("kafka-jaas-randomdatagen.config", "RegistryClient",
                    AppConfig.getProperty("kerberos.keytab"), AppConfig.getProperty("kerberos.user"),
                    true, false, true);
            System.setProperty("java.security.auth.login.config", "kafka-jaas-randomdatagen.config");

            props.put(SaslConfigs.SASL_MECHANISM, AppConfig.getProperty("kafka.sasl.mechanism"));
            props.put(SaslConfigs.SASL_KERBEROS_SERVICE_NAME, AppConfig.getProperty("kafka.sasl.kerberos.service.name"));

            Utils.loginUserWithKerberos(AppConfig.getProperty("kerberos.user"),
                    AppConfig.getProperty("kerberos.keytab"), new Configuration());
        }

            // SSL configs
        if (securityProtocol.equalsIgnoreCase("SASL_SSL") || securityProtocol.equalsIgnoreCase("SSL")) {
            props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, AppConfig.getProperty("keystore.location"));
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, AppConfig.getProperty("truststore.location"));
            props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, AppConfig.getProperty("keystore.keypassword"));
            props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, AppConfig.getProperty("keystore.password"));
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, AppConfig.getProperty("truststore.password"));
        }

  }


  public void write(String key, String value, String topic) {

     Producer<String, String> producer = new KafkaProducer<>(props);

     producer.send(new ProducerRecord<>(topic, key, value));

     producer.close();

  }

  public void read(String topic) {

    Consumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Collections.singleton(topic));

    consumer.poll(Duration.ofSeconds(10)).forEach(cr ->
                logger.info("Consumed value: " + cr.value() + " with key: " + cr.key() + " at offset: "  + cr.offset())
    );

    consumer.close();

  }


}