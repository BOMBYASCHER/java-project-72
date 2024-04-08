package hexlet.code.dto.urls.checks;

import hexlet.code.dto.BasePage;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckPage extends BasePage {
    private UrlCheck urlCheck;
}
