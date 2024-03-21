package hexlet.code.utils;

public class NamedRoutes {
    public static String rootPath() {
        return "/";
    }
    public static String urlsPath() {
        return rootPath() + "urls";
    }
    public static String urlPath() {
        return urlsPath() + "/" + "{id}";
    }
    public static String urlPath(Long id) {
        return urlsPath() + "/" + id.toString();
    }
}
