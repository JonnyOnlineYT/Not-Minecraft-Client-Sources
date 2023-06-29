package ru.rockstar.client.features.impl.combat;

import net.minecraft.util.text.TextFormatting;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class HitBox extends Feature {
    public static NumberSetting hitboxsize;

    public HitBox() {
        super("HitBox", "Увеличивает хитбокс у ентити", 0, Category.COMBAT);
        hitboxsize = new NumberSetting("Size", "Размер хитбокса", 0.2f, 0.1f, 1, 0.1f, () -> true);
        addSettings(hitboxsize);
    }

    @EventTarget
    public void fsdgsd(EventUpdate event) {
        this.setModuleName("HitBox " + TextFormatting.GRAY + "[" + hitboxsize.getNumberValue() + "]");
    }
}