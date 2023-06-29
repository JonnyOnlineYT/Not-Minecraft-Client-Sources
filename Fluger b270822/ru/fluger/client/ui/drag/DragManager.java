/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.drag;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import ru.fluger.client.Fluger;
import ru.fluger.client.files.impl.HudConfig;
import ru.fluger.client.ui.drag.Drag;
import ru.fluger.client.ui.drag.imp.ArmorHud;
import ru.fluger.client.ui.drag.imp.ArrayList;
import ru.fluger.client.ui.drag.imp.Notifications;
import ru.fluger.client.ui.drag.imp.PotionHud;
import ru.fluger.client.ui.drag.imp.SessionInfo;
import ru.fluger.client.ui.drag.imp.TargetHud;
import ru.fluger.client.ui.drag.imp.Timer;
import ru.fluger.client.ui.drag.imp.WaterMark;
import ru.fluger.client.ui.drag.imp.WorldInfo;

public class DragManager {
    public List<Drag> drags = new java.util.ArrayList<Drag>();

    public DragManager() {
        this.drags.add(new WaterMark());
        this.drags.add(new ArmorHud());
        this.drags.add(new WorldInfo());
        this.drags.add(new PotionHud());
        this.drags.add(new ArrayList());
        this.drags.add(new TargetHud());
        this.drags.add(new SessionInfo());
        this.drags.add(new Notifications());
        this.drags.add(new Timer());
    }

    public void updatePostions(int mouseX, int mouseY) {
        mouseX = Fluger.scale.calc(mouseX);
        mouseY = Fluger.scale.calc(mouseY);
        for (Drag d : this.drags) {
            if (!d.dragging) continue;
            d.x = (float)mouseX + d.x2;
            d.y = (float)mouseY + d.y2;
            try {
                Fluger.instance.fileManager.getFile(HudConfig.class).saveFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            this.border();
        }
    }

    public void init() {
        for (Drag d : Fluger.instance.dragmanager.drags) {
            d.init();
            try {
                Fluger.instance.fileManager.getFile(HudConfig.class).loadFile();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void border() {
        for (Drag d : this.drags) {
            if (d.x <= 0.0f) {
                d.x = 0.0f;
            }
            if (d.y <= 0.0f) {
                d.y = 0.0f;
            }
            ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
            int maxX = (int)((double)Fluger.scale.calc(rs.getScaledWidth()) - d.getWidth());
            int maxY = (int)((double)Fluger.scale.calc(rs.getScaledHeight()) - d.getHeight());
            if (d.x >= (float)maxX) {
                d.x = maxX;
            }
            if (!(d.y >= (float)maxY)) continue;
            d.y = maxY;
        }
    }

    public void render() {
        this.drags.forEach(d -> d.render(0, 0));
    }

    public void mouseClicked(int x, int y, int button) {
        x = Fluger.scale.calc(x);
        y = Fluger.scale.calc(y);
        for (Drag d : this.drags) {
            if (!d.collided(x, y) || button != 0) continue;
            d.x2 = d.x - (float)x;
            d.y2 = d.y - (float)y;
            d.dragging = true;
        }
    }

    public void reased() {
        for (Drag d : this.drags) {
            d.dragging = false;
        }
    }
}

