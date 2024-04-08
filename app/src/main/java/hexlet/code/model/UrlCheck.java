package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public final class UrlCheck {
    private Long id;
    private final Long urlId;
    private final int statusCode;
    private final String h1;
    private final String title;
    private final String description;
    private final Timestamp createdAt;

    public String getCreatedAtFormatted() {
        return createdAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.y H:mm"));
    }
}
