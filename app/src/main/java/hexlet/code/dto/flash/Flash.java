package hexlet.code.dto.flash;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Flash {
    private String text;
    private FlashType type;
}
