package guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducer;

import java.util.HashMap;
import java.util.Map;

import static constants.Constant.*;
import static constants.Constant.ACKS;

public class KafkaModule extends AbstractModule {

    @Provides
    @Singleton
    KafkaProducer<String, JsonObject> providesKafkaProducer(JsonObject applicationProperties) {
        Map<String, String> kafkaConfig = new HashMap<>();
        kafkaConfig.put(BOOTSTRAP_SERVER, applicationProperties.getString(BOOTSTRAP_SERVER));
        kafkaConfig.put(KEY_SERIALIZER, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaConfig.put(VALUE_SERIALIZER, "io.vertx.kafka.client.serialization.JsonObjectSerializer");
        kafkaConfig.put(ACKS, "1");
        return KafkaProducer.create(Vertx.vertx(), kafkaConfig);
    }
}
