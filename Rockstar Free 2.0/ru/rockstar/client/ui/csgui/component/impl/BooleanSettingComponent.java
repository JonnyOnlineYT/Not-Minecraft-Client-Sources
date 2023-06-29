package ru.rockstar.client.ui.csgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.server.network.NetHandlerLoginServer;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.impl.display.ClickGUI;
import ru.rockstar.client.ui.csgui.component.Component;
import ru.rockstar.client.ui.csgui.component.PropertyComponent;
import ru.rockstar.client.ui.settings.Setting;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;

import java.awt.*;
import java.awt.color.ColorSpace;

public class BooleanSettingComponent extends Component implements PropertyComponent {

    public BooleanSetting booleanSetting;
    public float textHoverAnimate = 0f;
    public float leftRectAnimation = 0;
    public float rightRectAnimation = 0;
    Minecraft mc = Minecraft.getMinecraft();
    public BooleanSettingComponent(Component parent, BooleanSetting booleanSetting, int x, int y, int width, int height) {
        super(parent, booleanSetting.getName(), x, y, width, height);
        this.booleanSetting = booleanSetting;
    }
    

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (booleanSetting.isVisible()) {
            int x = getX();
            int y = getY();
            int width = getWidth();
            int height = getHeight();
            int middleHeight = getHeight() / 2;
            boolean hovered = isHovered(mouseX, mouseY);
            int color = 0;
            Color onecolor = new Color(ClickGUI.color.getColorValue());
            Color twocolor = new Color(ClickGUI.colorTwo.getColorValue());
            double speed = ClickGUI.speed.getNumberValue();
            switch (ClickGUI.clickGuiColor.currentMode) {
                case "Client":
                    color = DrawHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                    break;
                case "Fade":
                    color = DrawHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y + height * 6L / 60F * 2) % 2) - 1));
                    break;
                case "Color Two":
                    color = DrawHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y + height * 6L / 60F * 2) % 2) - 1));
                    break;
                case "Astolfo":
                    color = DrawHelper.astolfo(true, y).getRGB();
                    break;
                case "Static":
                    color = onecolor.getRGB();
                    break;
                case "Rainbow":
                    color = DrawHelper.rainbow(300, 1, 1).getRGB();
                    break;
            }
            

            mc.neverlose500_13.drawString(getName(), x + 3, y + middleHeight - 2, Color.GRAY.getRGB());
            textHoverAnimate = AnimationHelper.animation(textHoverAnimate, hovered ? 2.3f : 2, 0);
            leftRectAnimation = AnimationHelper.animation(leftRectAnimation, booleanSetting.getBoolValue() ? 10 : 17, 0);
            rightRectAnimation = AnimationHelper.animation(rightRectAnimation, (booleanSetting.getBoolValue() ? 3 : 10), 0);
            DrawHelper.drawSmoothRect(x + width - 16, y + 5, x + width - 5f, y + height - 6.4f, new Color(0, 0, 0, 255).getRGB());
            DrawHelper.drawCircle(x + width - 15, y + 8.5f, 0, 360, 3.5f, 2, true, new Color(0,0,0, 255));
            DrawHelper.drawCircle(x + width - 7, y + 8.5f, 0, 360, 3.5f, 2, true, new Color(0,0,0, 255));
            if (booleanSetting.getBoolValue()) {
            	DrawHelper.drawCircle(x + width - leftRectAnimation + 2.5f, y + 8.5f, 0, 360, 4, 1, true, new Color(color));
            	if (ClickGUI.glow.getBoolValue()) {
            		DrawHelper.drawGlow(x - 1 + width - leftRectAnimation + 2.5f, y - 6 + 8.5f, x + 1 + width - leftRectAnimation + 2.5f, y + 6 + 8.5f, new Color(color).getRGB());
            	}
            } else {
            	DrawHelper.drawCircle(x + width - leftRectAnimation + 2.5f, y + 8.5f, 0, 360, 4, 1, true, new Color(100,100,100, 255));
            }
            if (hovered) {
                if (booleanSetting.getDesc() != null) {
                    DrawHelper.drawNewRect(x + 120, y + height / 1.5F + 3.5F, x + 138 + mc.fontRendererObj.getStringWidth(booleanSetting.getDesc()) - 10, y + 3.5F, new Color(30, 30, 30, 255).getRGB());
                    mc.neverlose500_13.drawStringWithShadow(booleanSetting.getDesc(), x + 124, y + height / 1.5F - 6F, -1);
                }
            }
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && isHovered(mouseX, mouseY) && booleanSetting.isVisible()) {
            booleanSetting.setBoolValue(!booleanSetting.getBoolValue());
        }
    }

    @Override
    public Setting getSetting() {
        return booleanSetting;
    }
}
