package services;

import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.RecordMetadata;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducer;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducerRecord;


public class KafkaService {
    private final KafkaProducer<String, JsonObject> kafkaProducer;

    public KafkaService(KafkaProducer<String, JsonObject> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public Single<JsonObject> sendPayloadToKafkaTopic(String key, JsonObject value, String topicName) {
        if(key == null  || value == null || topicName == null) {
            throw new RuntimeException("kafka key, value and topicName can not be null");
        }
        KafkaProducerRecord<String, JsonObject> kafkaProducerRecord = createKafkaProducerRecord(key, value, topicName);
        return kafkaProducer.rxSend(kafkaProducerRecord)
                .map(RecordMetadata::toJson);
    }

    private KafkaProducerRecord<String, JsonObject> createKafkaProducerRecord(String key, JsonObject value, String topicName) {
        return KafkaProducerRecord.create(topicName, key, value);
    }
}
