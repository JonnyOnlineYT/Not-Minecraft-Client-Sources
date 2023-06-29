package de.strafe.modules.render;

import com.eventapi.EventTarget;
import de.strafe.Strafe;
import de.strafe.events.EventRender2D;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.utils.TimeUtil;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;


public class HUD extends Module {

    public HUD() {
        super("HUD", 0, Category.RENDER);
    }

    int r = 255;
    int g = 0;
    int b = 0;
    int a = 255;


    private Color rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f);
    }

    @EventTarget
    public void onUpdate(EventRender2D event) {
        TimeUtil t = new TimeUtil();

        final AtomicInteger y = new AtomicInteger(1);
        mc.fontRendererObj.drawStringWithShadow("S", 1,1, rainbow(1).getRGB());
        mc.fontRendererObj.drawStringWithShadow("trafe", 7,1, Color.GRAY.getRGB());
        Strafe.INSTANCE.moduleManager.modules.stream().filter(Module::isToggled)
                .forEach(m -> mc.fontRendererObj.drawStringWithShadow(m.getName(), event.sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(m.getName()) - 1,
                        y.getAndAdd(mc.fontRendererObj.FONT_HEIGHT), rainbow(12).getRGB()));
            Hotbar();
    }
    public void Hotbar (){
        ScaledResolution sr = new ScaledResolution(mc);
        GuiIngame.drawRect(0, sr.getScaledHeight() - 23, sr.getScaledWidth(), sr.getScaledHeight(), 0x18FFFFFF);

    }


}
