package ru.rockstar.client.features.impl.visuals;

import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class ScoreBoard extends Feature {
    public static BooleanSetting noScore;
    public NumberSetting y;

    public ScoreBoard() {
        super("Scoreboard", "Позволяет настроить скорборд на сервере", 0, Category.VISUALS);
        y = new NumberSetting("PositionY", "Позиция скорборда по Y", 5, 0, 215, 1, () -> !noScore.getBoolValue());
        noScore = new BooleanSetting("No Scoreboard", false, () -> true);
        addSettings(noScore, y);

    }
}
