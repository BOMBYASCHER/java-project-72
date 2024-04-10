package hexlet.code.controller;

import hexlet.code.dto.flash.Flash;
import hexlet.code.dto.flash.FlashType;
import hexlet.code.model.UrlCheck;
import hexlet.code.repositoty.CheckRepository;
import hexlet.code.repositoty.UrlRepository;
import hexlet.code.utils.NamedRoutes;
import io.javalin.http.Context;
import kong.unirest.core.Unirest;
import org.jsoup.Jsoup;

import java.sql.SQLException;
import java.sql.Timestamp;

public class CheckController {
    public static void create(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(urlId);
        if (url.isPresent()) {
            try {
                var response = Unirest.get(url.get().getName()).asString();
                var statusCode = response.getStatus();
                var page = Jsoup.parse(response.getBody());
                String title = page.title();
                String h1;
                String description;
                try {
                    h1 = page.selectFirst("h1").text();
                } catch (NullPointerException e) {
                    h1 = "";
                }
                try {
                    description = page.selectFirst("meta[name=description]").attr("content");
                } catch (NullPointerException e) {
                    description = "";
                }
                var createdAt = new Timestamp(System.currentTimeMillis());
                UrlCheck check = new UrlCheck(urlId, statusCode, h1, title, description, createdAt);
                CheckRepository.save(check);
            } catch (Exception e) {
                var flash = new Flash(
                        "Failed to establish a connection to the site, verification is not possible.",
                        FlashType.DANGER
                );
                ctx.sessionAttribute("flash", flash);
            }
        } else {
            throw new SQLException("Url with this ID doesn't exist");
        }
        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}
