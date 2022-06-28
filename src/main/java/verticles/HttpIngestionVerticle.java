package verticles;

import exception.IngestionException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;
import lombok.RequiredArgsConstructor;
import services.IngestionService;

import static constants.Constant.HOST;
import static constants.Constant.PORT;

@RequiredArgsConstructor
public class HttpIngestionVerticle extends AbstractVerticle {

    private final JsonObject applicationProperties;
    private final IngestionService ingestionService;

    @Override
    public Completable rxStart() {
        Router router = getRouter();
        return vertx.createHttpServer()
                .requestHandler(router)
                .rxListen(applicationProperties.getInteger(PORT), applicationProperties.getString(HOST))
                .ignoreElement();
    }

    private Router getRouter() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.post("/ingest").handler(this::handleIngestionRequest);
        return router;
    }

    void handleIngestionRequest(RoutingContext rc) {
        JsonObject payload = rc.body().asJsonObject();
        ingestionService.validatePayload(payload)
                .flatMap(ingestionService::sendPayloadToKafka)
                .subscribe(res -> {
                    rc.response().setStatusCode(HttpResponseStatus.OK.code()).end();
                },  error -> {
                    IngestionException e = (IngestionException) error;
                    rc.response().setStatusCode(e.getStatusCode()).end(e.getDescription());
                });
    }
}
