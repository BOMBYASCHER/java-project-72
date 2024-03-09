package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.repositoty.UrlRepository;
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

    private static String loadDatabaseUrl() {
        String localDatabase = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;";
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", localDatabase);
    }
    private static Integer getPort() {
        var port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }
    private static void authentication(HikariDataSource database) {
        String username = System.getenv("JDBC_DATABASE_USERNAME");
        String password = System.getenv("JDBC_DATABASE_PASSWORD");
        database.setUsername(username);
        database.setPassword(password);
    }
    public static Javalin getApp() throws IOException, SQLException {
        Logger logger = LoggerFactory.getLogger(App.class);
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(loadDatabaseUrl());
        var database = new HikariDataSource(hikariConfig);
        authentication(database);
        String sql = loadDatabaseSchema(database);
        try (var statement = database.getConnection().createStatement()) {
            statement.execute(sql);
        }
        UrlRepository.connection = database;
        var app = Javalin.create(config -> config.bundledPlugins.enableDevLogging());
        app.get("/", ctx -> ctx.result("Hello World"));
        logger.info("Logger started");
        return app;
    }

    private static String loadDatabaseSchema(HikariDataSource database) throws IOException {
        var name = database.getDataSourceProperties().isEmpty() ? "h2.sql" : "postgre.sql";
        var inputStream = App.class.getClassLoader().getResourceAsStream(name);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

}
