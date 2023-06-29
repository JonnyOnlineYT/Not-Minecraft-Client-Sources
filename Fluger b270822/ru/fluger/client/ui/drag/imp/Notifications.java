/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.drag.imp;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import ru.fluger.client.Fluger;
import ru.fluger.client.ui.drag.Drag;
import ru.fluger.client.ui.notifications.Preview;
import ru.fluger.client.ui.notifications.PreviewInfo;
import ru.fluger.client.ui.notifications.PreviewInitializer;
import ru.fluger.client.ui.notifications.PreviewType;

public class Notifications
extends Drag {
    Preview preview = new Preview(new PreviewInfo(){

        @Override
        public PreviewType type() {
            return null;
        }

        @Override
        public String text() {
            return "\u0412\u044b \u043c\u043e\u0436\u0435\u0442\u0435 \u043c\u0435\u043d\u044f \u0434\u0432\u0438\u0433\u0430\u0442\u044c.";
        }

        @Override
        public int seconds() {
            return 1337;
        }
    });

    public Notifications() {
        this.name = "Notifications";
    }

    @Override
    public void init() {
        this.setWidth(this.preview.getWidth());
        this.setHeight(this.preview.getHeight());
        ScaledResolution rs = new ScaledResolution(this.mc);
        int width = Fluger.scale.calc(rs.getScaledWidth());
        int height = Fluger.scale.calc(rs.getScaledHeight());
        this.x = (float)width - this.width - 5.0f;
        this.y = (float)((double)height - this.getHeight()) - 20.0f;
    }

    @Override
    public void render(int mx, int my) {
        ScaledResolution rs = new ScaledResolution(this.mc);
        int width = Fluger.scale.calc(rs.getScaledWidth());
        int height = Fluger.scale.calc(rs.getScaledHeight());
        ru.fluger.client.feature.impl.hud.Notifications nt = (ru.fluger.client.feature.impl.hud.Notifications)Fluger.instance.featureManager.getFeatureByClass(ru.fluger.client.feature.impl.hud.Notifications.class);
        if (!nt.state) {
            return;
        }
        if (Fluger.instance.notifications.getList().size() > 0) {
            PreviewInitializer.render((int)this.x, (int)this.y);
        } else if (this.mc.currentScreen instanceof GuiChat) {
            this.preview.render(0, (int)this.x, (int)this.y);
        }
    }
}

