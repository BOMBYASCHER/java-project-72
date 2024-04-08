package hexlet.code;

import hexlet.code.utils.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
public class AppTest {

    static Javalin app;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @Test
    @DisplayName("Check app is starting")
    public void testApp() {
        JavalinTest.test(app, (server, client) -> {
            assertThat(client.get("/").code()).isEqualTo(200);
            assertThat(client.get("/").body().string()).contains("Page analyzer");
        });
    }

    @Nested
    @DisplayName("Connection every route")
    class ConnectionRoutesTest {
        @Test
        @DisplayName("Connecting root path")
        public void testRoot() {
            JavalinTest.test(app, (server, client) -> {
                assertThat(client.get(NamedRoutes.rootPath()).code()).isEqualTo(200);
            });
        }

        @Test
        @DisplayName("Connecting urls path")
        public void testUrls() {
            JavalinTest.test(app, (server, client) -> {
                assertThat(client.get(NamedRoutes.urlsPath()).code()).isEqualTo(200);
            });
        }

        @Test
        @DisplayName("Connecting added url path")
        public void testUrl() {
            JavalinTest.test(app, (server, client) -> {
                try (var postResponse = client.post(NamedRoutes.urlsPath(), "url=https://www.youtube.com")) {
                    assertThat(postResponse.code()).isEqualTo(200);

                    var getResponse2 = client.get(NamedRoutes.urlPath(1L));
                    assertThat(getResponse2.code()).isEqualTo(200);
                    assertThat(getResponse2.body().string()).contains("https://www.youtube.com");
                }
            });
        }
    }

    @Nested
    class PostRequestsTest {
        @Test
        @DisplayName("Add correct short url to check")
        public void testPostWithCorrectURL1() {
            JavalinTest.test(app, (server, client) -> {
                try (var postResponse = client.post(NamedRoutes.urlsPath(), "url=https://www.youtube.com")) {
                    assertThat(postResponse.code()).isEqualTo(200);

                    var getResponse1 = client.get(NamedRoutes.urlsPath());
                    assertThat(getResponse1.code()).isEqualTo(200);
                    assertThat(getResponse1.body().string()).contains("https://www.youtube.com");

                    var getResponse2 = client.get(NamedRoutes.urlPath(1L));
                    assertThat(getResponse2.code()).isEqualTo(200);
                    assertThat(getResponse2.body().string()).contains("https://www.youtube.com");
                }
            });
        }
        @Test
        @DisplayName("Add correct long url to check")
        public void testPostWithCorrectURL2() {
            JavalinTest.test(app, (server, client) -> {
                try (var postResponse = client.post(NamedRoutes.urlsPath(),
                        "url=https://some-domain.org/example/path")) {
                    assertThat(postResponse.code()).isEqualTo(200);

                    var getResponse1 = client.get(NamedRoutes.urlsPath());
                    assertThat(getResponse1.code()).isEqualTo(200);
                    assertThat(getResponse1.body().string()).contains("https://some-domain.org");

                    var getResponse2 = client.get(NamedRoutes.urlPath(1L));
                    assertThat(getResponse2.code()).isEqualTo(200);
                    assertThat(getResponse2.body().string()).contains("https://some-domain.org");
                }
            });
        }

        @Test
        @DisplayName("Add correct long url with port to check")
        public void testPostWithCorrectURL3() {
            JavalinTest.test(app, (server, client) -> {
                try (var postResponse = client.post(NamedRoutes.urlsPath(),
                        "url=https://some-domain.org:8080/example/path")) {
                    assertThat(postResponse.code()).isEqualTo(200);

                    var getResponse1 = client.get(NamedRoutes.urlsPath());
                    assertThat(getResponse1.code()).isEqualTo(200);
                    assertThat(getResponse1.body().string()).contains("https://some-domain.org:8080");

                    var getResponse2 = client.get(NamedRoutes.urlPath(1L));
                    assertThat(getResponse2.code()).isEqualTo(200);
                    assertThat(getResponse2.body().string()).contains("https://some-domain.org:8080");
                }
            });
        }

        @Test
        @DisplayName("Try add incorrect url to check")
        public void testPostWIthIncorrectURL() {
            JavalinTest.test(app, (server, client) -> {
                try (var postResponse = client.post(NamedRoutes.urlsPath(), "url=incorrectURL")) {
                    assertThat(postResponse.code()).isEqualTo(200);

                    var getResponse1 = client.get(NamedRoutes.urlsPath());
                    assertThat(getResponse1.body().string()).doesNotContain("incorrectURL");

                    var getResponse2 = client.get(NamedRoutes.urlPath(1L));
                    System.out.println(getResponse2.body().string());
                    assertThat(getResponse2.code()).isEqualTo(404);
                }
            });
        }

        @Test
        @DisplayName("Try add multiple identical urls")
        public void testPostMultipleIdenticalUrls() {
            JavalinTest.test(app, (server, client) -> {
                try (var postResponse = client.post(NamedRoutes.urlsPath(),
                        "url=https://some-domain.org")) {
                    assertThat(postResponse.code()).isEqualTo(200);

                    var getResponse = client.get(NamedRoutes.urlsPath());
                    assertThat(getResponse.body().string()).containsOnlyOnce("https://some-domain.org");
                }
                try (var postResponse = client.post(NamedRoutes.urlsPath(),
                        "url=https://some-domain.org/example/path")) {
                    assertThat(postResponse.code()).isEqualTo(200);

                    var getResponse = client.get(NamedRoutes.urlsPath());
                    assertThat(getResponse.body().string()).containsOnlyOnce("https://some-domain.org");
                }
                try (var postResponse = client.post(NamedRoutes.urlsPath(),
                        "url=https://some-domain.org:8080/example/path")) {
                    assertThat(postResponse.code()).isEqualTo(200);

                    var getResponse = client.get(NamedRoutes.urlsPath());
                    assertThat(getResponse.body().string()).containsOnlyOnce("https://some-domain.org:8080");
                }
                try (var postResponse = client.post(NamedRoutes.urlsPath(),
                        "url=https://some-domain.org:8080/example/path")) {
                    assertThat(postResponse.code()).isEqualTo(200);

                    var getResponse = client.get(NamedRoutes.urlsPath());
                    assertThat(getResponse.body().string()).containsOnlyOnce("https://some-domain.org:8080");
                }
            });
        }
    }
    @AfterAll
    public static void stop() {
        app.stop();
    }
}
