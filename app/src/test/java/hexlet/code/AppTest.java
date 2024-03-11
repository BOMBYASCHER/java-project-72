package hexlet.code;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
public class AppTest {

    Javalin app;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @Test
    public void testApp() {
        JavalinTest.test(app, (server, client) -> {
            assertThat(client.get("/").code()).isEqualTo(200);
            assertThat(client.get("/").body().string()).contains("Page analyzer");
        });
    }
}
