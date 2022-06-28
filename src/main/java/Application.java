import io.vertx.rxjava3.core.Vertx;
import verticles.ApplicationVerticle;

public class Application {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ApplicationVerticle())
                .subscribe();

    }
}
