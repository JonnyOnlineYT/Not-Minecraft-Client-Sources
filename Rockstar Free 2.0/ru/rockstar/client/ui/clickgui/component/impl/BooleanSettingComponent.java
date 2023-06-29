package ru.rockstar.client.ui.clickgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.server.network.NetHandlerLoginServer;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RenderUtils;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.client.features.impl.display.ClickGUI;
import ru.rockstar.client.ui.clickgui.ClickGuiScreen;
import ru.rockstar.client.ui.clickgui.component.Component;
import ru.rockstar.client.ui.clickgui.component.PropertyComponent;
import ru.rockstar.client.ui.settings.Setting;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;

import java.awt.*;
import java.awt.color.ColorSpace;

import org.lwjgl.opengl.GL11;

import javafx.animation.Interpolator;

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
        	String mode = ClickGUI.style.getOptions();
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            if (mode.equalsIgnoreCase("Rockstar New")) {
            	GlStateManager.pushMatrix();
            	GlStateManager.enable(GL11.GL_SCISSOR_TEST);
            	DrawHelper.scissorRect(0, getY2() + 47, getX() + 98, getY2() + 253);
            }
            int x = getX();
            boolean a = ClickGuiScreen.progress >= 0.9f;
            int y = a ? ((getY())) : (- 1000);
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
            
            GlStateManager.pushMatrix();
            
            this.textHoverAnimate = (float)Interpolator.LINEAR.interpolate((double)this.textHoverAnimate, booleanSetting.getBoolValue() ? 1 : 0, 0.05);
            this.leftRectAnimation = (float)Interpolator.LINEAR.interpolate((double)this.leftRectAnimation, booleanSetting.getBoolValue() ? 8 : 17, 0.1);
            this.rightRectAnimation = (float)Interpolator.LINEAR.interpolate((double)this.rightRectAnimation, (booleanSetting.getBoolValue() ? 3 : 10), 0.1);
            
            RoundedUtil.drawRound(x + 3, y + middleHeight - 2 - 3.5f, 20, 10, 3, new Color(40,40,40));
            
            Color l1 = ClickGUI.clickGuiColor.currentMode.equalsIgnoreCase("Static") ? onecolor.brighter() : new Color(color).brighter();
            
            Color l2 = ClickGUI.clickGuiColor.currentMode.equalsIgnoreCase("Static") ? onecolor : new Color(color);

            
            	RoundedUtil.drawRound(x + 23 - leftRectAnimation + 2.5f - 4, y + 8.5f + 3 - 4, 8, 8, 2, new Color(60,60,60));
            	
            	
            	RoundedUtil.drawGradientRound(x + 23 - leftRectAnimation + 2.5f - 4 * textHoverAnimate, y + 8.5f + 3 - 4 * textHoverAnimate, 8 * textHoverAnimate, 8 * textHoverAnimate, 2, l2, l1,l1,l2);
                
            	
            	
            	
            	if (mode.equalsIgnoreCase("Rockstar New")) {
          		GlStateManager.disable(GL11.GL_SCISSOR_TEST);
          		GlStateManager.popMatrix();
          	  }
            	
            	 GlStateManager.pushMatrix();
             	GlStateManager.enable(GL11.GL_SCISSOR_TEST);
             	DrawHelper.scissorRect(getX() + 24, getY2() + 47, getX() + 98, getY2() + 253);
                 mc.mntsb.drawString(getName(), x + 90 + 3 - mc.mntsb.getStringWidth(getName()), y + middleHeight - 2, Color.WHITE.getRGB());
                 
                DrawHelper.drawGradientRect1(x + 24, y + middleHeight - 6, x + 40,  y + middleHeight + 3, new Color(60,60,60).getRGB(), new Color(60,60,60,0).getRGB());
                 
             	GlStateManager.disable(GL11.GL_SCISSOR_TEST);
           		GlStateManager.popMatrix();
           		
            GlStateManager.popMatrix();
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
