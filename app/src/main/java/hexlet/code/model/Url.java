package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class Url {
    private long id;
    private String name;
    private Timestamp createdAt;
}
