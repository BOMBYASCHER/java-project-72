package hexlet.code.repositoty;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UrlRepository {
    public static HikariDataSource connection;

}
