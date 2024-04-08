package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controller.CheckController;
import hexlet.code.controller.UrlController;
import hexlet.code.repositoty.BaseRepository;
import hexlet.code.repositoty.CheckRepository;
import hexlet.code.repositoty.UrlRepository;
import io.javalin.http.Context;
import io.javalin.validation.Params;
import io.javalin.validation.Validator;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UrlChecksTest {
    private final Context ctx = mock(Context.class);

    @BeforeEach
    public final void setUp() throws SQLException, IOException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;");
        var database = new HikariDataSource(hikariConfig);
        var databaseSchemaInputStream = UrlChecksTest.class.getClassLoader().getResourceAsStream("h2.sql");
        String sql;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(databaseSchemaInputStream, StandardCharsets.UTF_8)
        )) {
            sql = reader.lines().collect(Collectors.joining("\n"));
        }
        try (var statement = database.getConnection().createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.connection = database;
    }
    @Test
    public void testCreateChecks() throws IOException, SQLException {
        var exampleInputStream = UrlChecksTest.class.getClassLoader().getResourceAsStream("example.html");
        String exampleHtml;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(exampleInputStream, StandardCharsets.UTF_8)
        )) {
            exampleHtml = reader.lines().collect(Collectors.joining("\n"));
        }
        MockWebServer exampleServer = new MockWebServer();
        var exampleUrl = exampleServer.url("/").toString();
        var exampleResponse = new MockResponse();
        exampleResponse.setBody(exampleHtml);
        exampleServer.enqueue(exampleResponse);

        assertThat(CheckRepository.getUrlChecks().size()).isEqualTo(0);
        when(ctx.formParam("url")).thenReturn(exampleUrl);
        UrlController.create(ctx);
        var exampleValidator = new Validator<>(
                new Params<>(
                    "id",
                    Long.class,
                    "1",
                    1L,
                    () -> 1L
                )
        );

        when(ctx.pathParamAsClass("id", Long.class)).thenReturn(exampleValidator);

        var exampleCreateStartAt = new Timestamp(System.currentTimeMillis());
        CheckController.create(ctx);
        var exampleCreateEndAt = new Timestamp(System.currentTimeMillis());
        assertThat(CheckRepository.getUrlChecks().size()).isEqualTo(1);
        assertThat(CheckRepository.getLastCheck(1L).isPresent()).isTrue();
        var exampleCheck1 = CheckRepository.getLastCheck(1L).get();
        assertThat(exampleCheck1.getUrlId()).isEqualTo(1);
        assertThat(exampleCheck1.getId()).isEqualTo(1);
        assertThat(exampleCheck1.getTitle()).isEqualTo("Example Domain");
        assertThat(exampleCheck1.getH1()).isEqualTo("Example Domain");
        assertThat(exampleCheck1.getDescription()).isEmpty();
        assertThat(exampleCheck1.getStatusCode()).isEqualTo(200);
        var exampleFirstCheckTimestamp = exampleCheck1.getCreatedAt();
        assertThat(exampleFirstCheckTimestamp).isBetween(exampleCreateStartAt, exampleCreateEndAt);

        exampleServer.enqueue(exampleResponse);
        CheckController.create(ctx);
        assertThat(CheckRepository.getUrlChecks().size()).isEqualTo(2);
        assertThat(CheckRepository.getLastCheck(1L).isPresent()).isTrue();
        var exampleCheck2 = CheckRepository.getLastCheck(1L).get();
        assertThat(exampleCheck2.getUrlId()).isEqualTo(1);
        assertThat(exampleCheck2.getId()).isEqualTo(2);
        assertThat(exampleCheck2.getTitle()).isEqualTo("Example Domain");
        assertThat(exampleCheck2.getH1()).isEqualTo("Example Domain");
        assertThat(exampleCheck2.getDescription()).isEmpty();
        assertThat(exampleCheck2.getStatusCode()).isEqualTo(200);
        var exampleSecondCheckTimestamp = exampleCheck2.getCreatedAt();
        assertThat(exampleSecondCheckTimestamp).isAfter(exampleFirstCheckTimestamp);

        exampleServer.shutdown();

        var largeInputStream = UrlChecksTest.class.getClassLoader().getResourceAsStream("large.html");
        String largeHtml;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(largeInputStream, StandardCharsets.UTF_8)
        )) {
            largeHtml = reader.lines().collect(Collectors.joining("\n"));
        }
        MockWebServer largeServer = new MockWebServer();
        var largeUrl = largeServer.url("/").toString();
        var largeResponse = new MockResponse();
        largeResponse.setBody(largeHtml);
        largeServer.enqueue(largeResponse);

        assertThat(CheckRepository.getUrlChecks().size()).isEqualTo(2);
        when(ctx.formParam("url")).thenReturn(largeUrl);
        UrlController.create(ctx);
        System.out.println(UrlRepository.getUrls());
        var largeValidator = new Validator<>(
                new Params<>(
                        "id",
                        Long.class,
                        "2",
                        2L,
                        () -> 2L)
        );

        when(ctx.pathParamAsClass("id", Long.class)).thenReturn(largeValidator);

        var largeCreateStartAt = new Timestamp(System.currentTimeMillis());
        CheckController.create(ctx);
        var largeCreateEndAt = new Timestamp(System.currentTimeMillis());
        assertThat(CheckRepository.getUrlChecks().size()).isEqualTo(3);
        assertThat(CheckRepository.getLastCheck(2L).isPresent()).isTrue();
        var largeCheck1 = CheckRepository.getLastCheck(2L).get();
        assertThat(largeCheck1.getUrlId()).isEqualTo(2);
        assertThat(largeCheck1.getId()).isEqualTo(3);
        assertThat(largeCheck1.getTitle()).isEqualTo("Cloud Application Hosting for Developers | Render");
        assertThat(largeCheck1.getH1()).isEqualTo("Your fastest path to production");
        assertThat(largeCheck1.getDescription()).isEqualTo("Render is a unified cloud to build and run"
                + " all your apps and websites with free"
                + " TLS certificates, global CDN, private networks and auto deploys from Git.");
        assertThat(largeCheck1.getStatusCode()).isEqualTo(200);
        var largeFirstCheckTimestamp = largeCheck1.getCreatedAt();
        assertThat(largeFirstCheckTimestamp).isBetween(largeCreateStartAt, largeCreateEndAt);

        largeServer.enqueue(largeResponse);
        CheckController.create(ctx);
        assertThat(CheckRepository.getUrlChecks().size()).isEqualTo(4);
        assertThat(CheckRepository.getLastCheck(2L).isPresent()).isTrue();
        var largeCheck2 = CheckRepository.getLastCheck(2L).get();
        assertThat(largeCheck2.getUrlId()).isEqualTo(2);
        assertThat(largeCheck2.getId()).isEqualTo(4);
        assertThat(largeCheck2.getTitle()).isEqualTo("Cloud Application Hosting for Developers | Render");
        assertThat(largeCheck2.getH1()).isEqualTo("Your fastest path to production");
        assertThat(largeCheck2.getDescription()).isEqualTo("Render is a unified cloud to build and run"
                + " all your apps and websites with free"
                + " TLS certificates, global CDN, private networks and auto deploys from Git.");
        assertThat(largeCheck2.getStatusCode()).isEqualTo(200);
        var largeSecondCheckTimestamp = largeCheck2.getCreatedAt();
        assertThat(largeSecondCheckTimestamp).isAfter(largeFirstCheckTimestamp);

        largeServer.shutdown();
    }

}
