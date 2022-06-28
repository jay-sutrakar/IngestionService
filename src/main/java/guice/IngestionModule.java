package guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducer;
import services.IngestionService;
import services.KafkaService;

public class IngestionModule extends AbstractModule {

    @Provides
    @Singleton
    IngestionService provideIngestionService(KafkaProducer<String, JsonObject> producer, JsonObject applicationProperties) {
        KafkaService kafkaService = new KafkaService(producer);
        return new IngestionService(kafkaService, applicationProperties);
    }

}
