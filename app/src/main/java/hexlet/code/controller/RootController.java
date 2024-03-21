package hexlet.code.controller;

import hexlet.code.dto.flash.Flash;
import hexlet.code.dto.MainPage;
import io.javalin.http.Context;

import java.util.Collections;

public class RootController {
    public static void index(Context ctx) {
        var page = new MainPage();
        Flash flash = ctx.consumeSessionAttribute("flash");
        page.setFlash(flash);
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }
}
