package org.moonware.client.ui.windowClickGui.implWild;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.render.RenderUtil;
import org.moonware.client.utils.MWFont;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.utils.MWUtils;

import java.awt.*;

public class BooleanSettingComponent {
    private float x;
    private float y;
    private float width;
    private BooleanSetting booleanSetting;
    public BooleanSettingComponent(float x, float y, float width, BooleanSetting setting) {
        this.x = x;
        this.y = y ;
        this.width = width;
        this.booleanSetting = setting;

    }

    public void draw(int mouseX,int mouseY) {
        MWFont.MONTSERRAT_REGULAR.get(15).draw(booleanSetting.getName(), x + 2, y + 5, -1);
        RoundedUtil.drawRound(x + 105 - 25, y + 11 - 8, 20, 8, 4, new Color(255,255,255));
        RenderUtils2.drawBlurredShadow(x + 105 - 25, y + 11 - 8, 20, 8, 8, new Color(255,255,255));
        RenderUtil.drawGoodCircle(x + 105 - 25 + 4 + booleanSetting.getStateGuiAnim(), y + 11 - 8 + 4,  4, booleanSetting.get() ? new Color(150,150,150).getRGB() : new Color(180,180,180).getRGB());
        if (MWUtils.isHovered(x,y,width, 10, mouseX,mouseY) && Mouse.isButtonDown(0) && Minecraft.player.ticksExisted % 4 == 0) {
            booleanSetting.setBoolValue(!booleanSetting.get());
        }
    }
}
