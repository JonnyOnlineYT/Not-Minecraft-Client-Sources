/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.notifications;

import java.util.TimerTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import ru.fluger.client.Fluger;
import ru.fluger.client.UIRender;
import ru.fluger.client.ui.notifications.Preview;

public class PreviewInitializer {
    private Minecraft mc = Minecraft.getMinecraft();
    private UIRender render = new UIRender();

    public static void render(int x, int y) {
        ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        int offset = 0;
        for (final Preview p : Fluger.instance.notifications.getList()) {
            boolean yReverse;
            p.render(offset, x, y);
            boolean bl = yReverse = p.y > (float)(Fluger.scale.calc(rs.getScaledHeight()) / 2);
            offset = yReverse ? (offset -= (int)(p.getHeight() + 3.0f)) : (offset += (int)(p.getHeight() + 3.0f));
            if (Fluger.instance.notifications.getList().size() > 5) {
                p.done = true;
            }
            if (p.removed) {
                Fluger.instance.notifications.remove(p.getUuid());
            }
            p.getTimer().schedule(new TimerTask(){

                @Override
                public void run() {
                    p.done = true;
                }
            }, 1000 * p.getSeconds());
        }
    }
}

