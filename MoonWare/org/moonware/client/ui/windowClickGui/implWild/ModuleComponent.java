package org.moonware.client.ui.windowClickGui.implWild;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.Formatting;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.StencilUtil;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.render.GLUtil;
import org.moonware.client.helpers.Utils.render.RenderUtil;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWFont;

import java.awt.*;

public class ModuleComponent implements Helper {

    public int x;
    public int y;
    public Feature feature;
    private int mouseX;
    private int mouseY;
    private boolean drawChar;
    private  int button;
    private int height;
    private int rotVal;
    int trVal;
    int trYVal;
    public ModuleComponent(Feature feature, int x, int y,int mouseX,int mouseY,int height,boolean drawChar,int button) {
        this.x = x;
        this.y = y;
        this.feature = feature;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.drawChar = drawChar;
        this.button = button;
        this.height = height;
        this.rotVal = 0;
        this.trVal = 0;
        this.trYVal = 0;
    }
    public void drawRoundBgCircle(int mouseX, int mouseY, int x, int y, int width, int height, Color color, boolean blurredshadow) {
        if (blurredshadow) {
            StencilUtil.initStencilToWrite();
            RenderUtils2.drawBlurredShadow(x, y, width, height, 3, color);
            StencilUtil.readStencilBuffer(1);
            //RoundedUtil.drawRound(mouseX - 10, mouseY - 10,20,20,1f,new Color(255,255,255));
            //RenderUtil.drawGoodCircle(mouseX + 5,mouseY,16,new Color(255,255,255).getRGB());
            RenderUtils2.drawBlurredShadow(mouseX - 5,mouseY - 5, 10,10,19,new Color(255,255,255));
            StencilUtil.uninitStencilBuffer();
            RoundedUtil.drawRound(x, y, width, height, 3, color);
            RenderUtils2.drawBlur(12,() -> RenderUtils2.drawBlurredShadow(x, y, width, height, 3, color));
            RenderUtils2.drawShadow(12,8,() -> RoundedUtil.drawRound(x, y, width, height, 3, color));
        }else{
            StencilUtil.initStencilToWrite();
            RoundedUtil.drawRound(x,y,width,height,7,color);
            StencilUtil.readStencilBuffer(1);
            RenderUtils2.drawBlurredShadow(mouseX - 25,mouseY - 9, 50,18,35,new Color(255,255,255).brighter());
            StencilUtil.initStencilToWrite();
            RoundedUtil.drawRound(x,y,width,height,5,color);
            StencilUtil.readStencilBuffer(1);
            RenderUtils2.drawBlur(12,() -> RoundedUtil.drawRound(x + 0.5F,y + 0.5F,width - 1,height - 1,7,color));
            RoundedUtil.drawRound(x,y,width,height,5,color);
            StencilUtil.uninitStencilBuffer();
            StencilUtil.initStencilToWrite();
            RoundedUtil.drawRoundOutline(x,y,width,height,5,55,new Color(31,31,31,0),color);
            StencilUtil.readStencilBuffer(1);
           //RenderUtils2.drawBlurredShadow(x, y, width, height, 38, new Color(11,11,11));
            StencilUtil.uninitStencilBuffer();
        }
    }

    public void draw() {
        if (drawChar) {
            RectHelper.drawRect(x, y - 10 + 3, x + 40, y - 9 + 3, new Color(255, 255, 255, 50).getRGB());
            MWFont.MONTSERRAT_REGULAR.get(18).drawCenter(String.valueOf(feature.getLabel().charAt(0)), x + 48, y - 12 + 3, -1);
            RectHelper.drawRect(x + 60, y - 10 + 3, x + 235, y - 9+ 3, new Color(255, 255, 255, 50).getRGB());
            StencilUtil.initStencilToWrite();
            RectHelper.drawRect(x, y - 10 + 3, x + 40, y - 9 + 3, new Color(255, 255, 255, 50).getRGB());
            MWFont.MONTSERRAT_REGULAR.get(18).drawCenter(String.valueOf(feature.getLabel().charAt(0)), x + 48, y - 12 + 3, -1);
            RectHelper.drawRect(x + 60, y - 10 + 3, x + 235, y - 9+ 3, new Color(255, 255, 255, 50).getRGB());
            StencilUtil.readStencilBuffer(1);
            RenderUtils2.drawBlurredShadow(mouseX - 25,mouseY - 9, 50,18,35,new Color(255,255,255).brighter());
            StencilUtil.uninitStencilBuffer();
        }
        StencilUtil.initStencilToWrite();
        //drawRoundBgCircle(mouseX, mouseY, x, y - 1, 105,this.height , new Color(11, 11, 11, 90), false);
        RoundedUtil.drawRound(x + 1,y + 1,105 - 2,this.height - 3L,7,new Color(1));
        StencilUtil.readStencilBuffer(2);
        RenderUtils2.drawBlurredShadow(x,y,105,this.height + 2,14,new Color(1));
        //DrawHelper.drawGlow(x - 2,y - 2, x + 107,y + this.height + 4,-1);
        StencilUtil.uninitStencilBuffer();
        drawRoundBgCircle(mouseX, mouseY, x, y - 1, 105,this.height , new Color(11, 11, 11, 90), false);

        RoundedUtil.drawRound(x + 105 - 25, y + 11 - 8, 20, 8, 4, new Color(255,255,255));
        RenderUtils2.drawBlurredShadow(x + 105 - 25, y + 11 - 8, 20, 8, 8, new Color(255,255,255));
        RenderUtil.drawGoodCircle(x + 105 - 25 + 4 + feature.getStateGuiAnim(), y + 11 - 8 + 4,  4, feature.getState() ? new Color(150,150,150).getRGB() : new Color(180,180,180).getRGB());
        MWFont.MONTSERRAT_REGULAR.get(15).draw(feature.getLabel(), x + 2, y + 5, -1);


        this.rotVal =  Interpolator.linear(this.rotVal,  feature.isHovered ? 180 + 90 : 90    , 2F / 5);
        this.trVal = Interpolator.linear(this.trVal, feature.isHovered ? 5:0    , 2F / 5);;
        this.trYVal = Interpolator.linear(this.trYVal, feature.isHovered ? 2:0   , 2F / 5);;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 105 - 25 + 4 - 15 + feature.gettrVal(), y + 11 - 8 + 4 - feature .gettrYVal(),0);
        GLUtil.rotate((float) 1, (float) 1, (float) feature.getrotVal(), () -> MWFont.GREYCLIFFCF_MEDIUM.get(16).draw(Formatting.BOLD + ">",0,0,-1));
        GlStateManager.popMatrix();
        StencilUtil.initStencilToWrite();
//        drawRoundBgCircle(mouseX, mouseY, x, y - 1, 105,this.height , new Color(11, 11, 11, 90), false);
        RoundedUtil.drawRound(x,y,105, this.height,3, new Color(-1));
        StencilUtil.readStencilBuffer(1);
        if (feature.isHovered()) {
            int offset = 20;
            for(Setting setting : feature.getSettings()) {
                if (!setting.isVisible()) continue;
                if (setting instanceof BooleanSetting) {
                    new BooleanSettingComponent(x, y + offset, 105, (BooleanSetting) setting).draw(mouseX,mouseY);
                    offset+= 15;
                }else if (setting instanceof NumberSetting) {
                    new NumberSettingComponent((float) x, y + offset, 105, (NumberSetting) setting).draw(mouseX,mouseY);
                    offset += 20;
                }
            }
        }
        StencilUtil.uninitStencilBuffer();
    }

}
