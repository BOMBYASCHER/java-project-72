package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controller.UrlController;
import hexlet.code.repositoty.BaseRepository;
import hexlet.code.repositoty.UrlRepository;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UrlValidateTest {
    private final Context ctx = mock(Context.class);
    @BeforeEach
    public final void setUp() throws SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;");
        var database = new HikariDataSource(hikariConfig);
        String sql = """
                DROP TABLE IF EXISTS urls;

                CREATE TABLE urls (
                    id bigint PRIMARY KEY AUTO_INCREMENT,
                    name varchar(255) NOT NULL,
                    created_at timestamp
                );""";
        try (var statement = database.getConnection().createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.connection = database;
    }
    @Test
    public void postToAddCorrectUrlIsOk() throws SQLException {
        var correctUrl = "https://www.youtube.com";
        when(ctx.formParam("url")).thenReturn(correctUrl);
        UrlController.create(ctx);
        var savedUrl = UrlRepository.find(1L).orElseThrow(SQLException::new).getName();
        assertThat(correctUrl).isEqualTo(savedUrl);
    }

    @Test
    public void postToAddCorrectLongUrlIsOk() throws SQLException {
        var correctLongUrl = "https://some-domain.org/example/path";
        when(ctx.formParam("url")).thenReturn(correctLongUrl);
        UrlController.create(ctx);
        var savedUrl = UrlRepository.find(1L).orElseThrow(SQLException::new).getName();
        assertThat(savedUrl).isEqualTo("https://some-domain.org");
    }

    @Test
    public void postToAddCorrectLongUrlWithPortIsOk() throws SQLException {
        var correctLongWithPortUrl = "https://some-domain.org:8080/example/path";
        when(ctx.formParam("url")).thenReturn(correctLongWithPortUrl);
        UrlController.create(ctx);
        var savedUrl = UrlRepository.find(1L).orElseThrow(SQLException::new).getName();
        assertThat(savedUrl).isEqualTo("https://some-domain.org:8080");
    }

    @Test
    public void postToAddIncorrectUrlIsOk() throws SQLException {
        var incorrectUrl1 = "incorrectURL";
        when(ctx.formParam("url")).thenReturn(incorrectUrl1);
        UrlController.create(ctx);
        var hasUrl1 = UrlRepository.find(1L).isPresent();
        assertThat(hasUrl1).isFalse();

        var incorrectUrl2 = "";
        when(ctx.formParam("url")).thenReturn(incorrectUrl2);
        UrlController.create(ctx);
        var hasUrl2 = UrlRepository.find(1L).isPresent();
        assertThat(hasUrl2).isFalse();

        var incorrectUrl3 = "   ";
        when(ctx.formParam("url")).thenReturn(incorrectUrl3);
        UrlController.create(ctx);
        var hasUrl3 = UrlRepository.find(1L).isPresent();
        assertThat(hasUrl3).isFalse();

        var incorrectUrl4 = "  W#$%^&vq@://#E ";
        when(ctx.formParam("url")).thenReturn(incorrectUrl4);
        UrlController.create(ctx);
        var hasUrl4 = UrlRepository.find(1L).isPresent();
        assertThat(hasUrl4).isFalse();
    }

    @Test
    public void postToAddMultipleIdenticalUrlIsOk() throws SQLException {
        var url1 = "https://some-domain.org";
        when(ctx.formParam("url")).thenReturn(url1);
        UrlController.create(ctx);

        var url2 = "https://some-domain.org/example/path";
        when(ctx.formParam("url")).thenReturn(url2);
        UrlController.create(ctx);

        assertThat(UrlRepository.getUrls().size()).isEqualTo(1);

        var url3 = "https://some-domain.org:8080";
        when(ctx.formParam("url")).thenReturn(url3);
        UrlController.create(ctx);

        var url4 = "https://some-domain.org:8080/example/path";
        when(ctx.formParam("url")).thenReturn(url4);
        UrlController.create(ctx);

        assertThat(UrlRepository.getUrls().size()).isEqualTo(2);
    }
}
