package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import hexlet.code.controller.CheckController;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlController;
import hexlet.code.utils.NamedRoutes;
import io.javalin.rendering.template.JavalinJte;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.repositoty.BaseRepository;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
public class App {
    public static void main(String[] args) throws SQLException, IOException {
        getApp().start(getPort());
    }

    public static Javalin getApp() throws IOException, SQLException {
        Logger logger = LoggerFactory.getLogger(App.class);
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(loadDatabaseUrl());
        authentication(hikariConfig);
        var database = new HikariDataSource(hikariConfig);
        String sql = loadDatabaseSchema();
        try (var statement = database.getConnection().createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.connection = database;
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get(NamedRoutes.rootPath(), RootController::index);
        app.get(NamedRoutes.urlsPath(), UrlController::index);
        app.get(NamedRoutes.urlPath(), UrlController::show);
        app.post(NamedRoutes.urlsPath(), UrlController::create);
        app.post(NamedRoutes.checksPath(), CheckController::create);
        logger.info("Logger started");
        return app;
    }

    private static String loadDatabaseUrl() {
        String localDatabase = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;";
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", localDatabase);
    }
    private static Integer getPort() {
        var port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }
    private static void authentication(HikariConfig config) {
        String username = System.getenv("JDBC_DATABASE_USERNAME");
        String password = System.getenv("JDBC_DATABASE_PASSWORD");
        config.setUsername(username);
        config.setPassword(password);
    }

    private static String loadDatabaseSchema() throws IOException {
        var name = System.getenv("JDBC_DATABASE_URL") == null ? "h2.sql" : "postgre.sql";
        var inputStream = App.class.getClassLoader().getResourceAsStream(name);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }
}
