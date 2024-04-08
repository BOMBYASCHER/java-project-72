package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repositoty.CheckRepository;
import hexlet.code.repositoty.UrlRepository;
import hexlet.code.utils.NamedRoutes;
import io.javalin.http.Context;
import kong.unirest.core.Unirest;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckController {
    public static void create(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(urlId);
        if (url.isPresent()) {
            var response = Unirest.get(url.get().getName()).asString();
            var statusCode = response.getStatus();
            var body = response.getBody();
            var h1 = getContent(body, Content.H1);
            var title = getContent(body, Content.TITLE);
            var description = getContent(body, Content.DESCRIPTION);
            var createdAt = new Timestamp(System.currentTimeMillis());
            UrlCheck check = new UrlCheck(urlId, statusCode, h1, title, description, createdAt);
            CheckRepository.save(check);
        } else {
            throw new SQLException("Url with this ID doesn't exist");
        }
        ctx.redirect(NamedRoutes.urlPath(urlId));
    }

    private enum Content {
        H1("(?:<h1.*>)(?<snippet>.*)(?=</h1>)"),
        DESCRIPTION("(?:<meta.*?name=\"description\".*?content=\")(?<snippet>.*?)(?=\")"),
        TITLE("(?<=<title>)(?<snippet>.*)(?=</title>)");
        private final String regexp;
        Content(String regexp) {
            this.regexp = regexp;
        }
    }

    private static String getContent(String body, Content content) {
        Pattern pattern = Pattern.compile(content.regexp);
        Matcher matcher = pattern.matcher(body);
        return matcher.find() ? matcher.group("snippet") : "";
    }
}
