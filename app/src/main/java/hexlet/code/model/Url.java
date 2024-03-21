package hexlet.code.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@RequiredArgsConstructor
public class Url {
    private long id;
    private final String name;
    private Timestamp createdAt;

    public String getCreatedAtFormatted() {
        return createdAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.y H:mm"));
    }
}
