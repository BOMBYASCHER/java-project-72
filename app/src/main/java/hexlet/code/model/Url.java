package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public final class Url {
    private long id;
    private final String name;
    private Timestamp createdAt;
    private Optional<UrlCheck> lastCheck;

    public String getCreatedAtFormatted() {
        return createdAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.y H:mm"));
    }
}
