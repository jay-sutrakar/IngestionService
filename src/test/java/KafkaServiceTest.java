
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import services.KafkaService;

import java.util.HashMap;
import java.util.Map;

import static constants.Constant.*;

@ExtendWith(VertxExtension.class)
class KafkaServiceTest {
    private static KafkaProducer<String, JsonObject> producer;
    private static KafkaService kafkaService;

    @BeforeAll
    static void setup() {
        Map<String, String> kafkaConfig = new HashMap<>();
        kafkaConfig.put(BOOTSTRAP_SERVER, "localhost:9092");
        kafkaConfig.put(KEY_SERIALIZER, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaConfig.put(VALUE_SERIALIZER, "io.vertx.kafka.client.serialization.JsonObjectSerializer");
        kafkaConfig.put(ACKS, "1");
        producer = KafkaProducer.create(Vertx.vertx(), kafkaConfig);
        kafkaService = new KafkaService(producer);
    }

    @Test
    @DisplayName("Send payload testing")
    void test1() {
        JsonObject payload = new JsonObject();
        payload.put(DEVICE_ID, "TestDevice12354");
        payload.put(DEVICE_SYNC, "somevalue");
        payload.put(STEP_COUNT, "1232");
        kafkaService.sendPayloadToKafkaTopic(payload.getString(DEVICE_ID), payload, "incoming.steps")
                .blockingSubscribe(res -> System.out.println("Producer response : " + res), System.out::println);
    }
}
