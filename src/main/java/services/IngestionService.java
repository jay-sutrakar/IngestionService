package services;

import exception.IngestionException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

import static constants.Constant.*;

public class IngestionService {

    private final KafkaService kafkaService;
    private final JsonObject applicationProperties;

    public IngestionService(KafkaService kafkaService, JsonObject applicationProperties) {
        this.kafkaService = kafkaService;
        this.applicationProperties = applicationProperties;
    }

    public Single<JsonObject> validatePayload(JsonObject payload) {
        try {
            Objects.requireNonNull(payload.getString(DEVICE_ID), "deviceId is not present");
            Objects.requireNonNull(payload.getString(DEVICE_SYNC), "deviceSync is not present");
            Objects.requireNonNull(payload.getString(STEP_COUNT), "stepCount is not present");
        } catch (NullPointerException e) {
            throw new IngestionException(HttpResponseStatus.BAD_REQUEST.code(), "Payload validation failed, " + e.getMessage(), e);
        }
        return Single.just(payload);
    }

    public Single<JsonObject> sendPayloadToKafka(JsonObject payload) {
        String key = payload.getString(DEVICE_ID);
        return kafkaService.sendPayloadToKafkaTopic(key, payload, applicationProperties.getString(TOPIC_NAME))
                .doOnError(error -> {
                    throw new IngestionException(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), "Failed while uploading payload to kafka : " + error.getMessage(), error);
                });
    }

}
