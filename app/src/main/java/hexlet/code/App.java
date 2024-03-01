package hexlet.code;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    public static void main(String[] args) {
        getApp().start(7070);
    }
    static Javalin getApp() {
        Logger logger = LoggerFactory.getLogger(App.class);
        var app = Javalin.create();

        app.get("/", ctx -> ctx.result("Hello World"));

        logger.info("Logger started");

        return app;
    }
}
