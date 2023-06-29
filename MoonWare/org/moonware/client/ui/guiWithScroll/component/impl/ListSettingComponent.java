package org.moonware.client.ui.guiWithScroll.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.FontStorage;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWFont;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.render.GLUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.RenderHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.ui.guiWithScroll.Panel;
import org.moonware.client.ui.guiWithScroll.component.Component;
import org.moonware.client.ui.guiWithScroll.component.ExpandableComponent;
import org.moonware.client.ui.guiWithScroll.component.PropertyComponent;

import java.awt.*;


public class ListSettingComponent extends ExpandableComponent implements PropertyComponent {

    private final ListSetting listSetting;
    Minecraft mc = Minecraft.getMinecraft();
    private Feature feature;
    public ListSettingComponent(Component parent, ListSetting listSetting, int x, int y, int width, int height, Feature feature) {
        super(parent, listSetting.getName(), x, y, width, height);
        this.listSetting = listSetting;
        this.feature = feature;
    }
    private int rotate;
    private int rotateTR;
    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
    	
        super.drawComponent(scaledResolution, mouseX, mouseY);
        String mode = "Rockstar New";
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (mode.equalsIgnoreCase("Rockstar New")) {
        	GlStateManager.pushMatrix();
        	GlStateManager.enable(GL11.GL_SCISSOR_TEST);
            RenderHelper2.scissorRect(0, getY2() + 47, sr.getScaledWidth(), getY2() + 260);
        }
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        String selectedText = listSetting.getCurrentMode();
        int dropDownBoxY = y + 10;
        int textColor = 0xFFFFFF;
        //Gui.drawRect(x, y, x + width, y + height, new Color(15, 15, 15).getRGB());
        //RectHelper.drawGradientRect(x + Panel.X_ITEM_OFFSET, y - 4, x + width - Panel.X_ITEM_OFFSET, y + 12, new Color(15,15,20).darker().getRGB(), new Color(15,15,15).darker().getRGB());

        if (ClickGui.glow.getBoolValue()) {
            //GlowUtil.drawBlurredGlow(x + 2 - ClickGui.glowStrengh.getNumberValue(), y - 1 - ClickGui.glowStrenghver.getNumberValue(), x + mc.circleregularSmall.getStringWidth(getName()) + ClickGui.glowStrengh.getNumberValue(), y + 12 + ClickGui.glowStrenghver.getNumberValue(), ClickGui.listGlowColor.getColorValue());
        }
        //Gui.drawRect(x + 2, dropDownBoxY, x + getWidth() - 2, (int) (dropDownBoxY + 10.5), new Color(30, 30, 30).getRGB());
        //RectHelper.drawRect(x, dropDownBoxY - 11.5f, x + getWidth(), dropDownBoxY + 11.5f, new Color(0x101010).getRGB());
        RoundedUtil.drawRound(x +2, dropDownBoxY - 3, width - 4, 10 + (isExpanded() ? getHeightWithExpand() - 21  : 0), 1, new Color(31,31,31));
        RenderUtils2.drawBlurredShadow(x + 2, dropDownBoxY - 3, width - 4, 10 + (isExpanded() ? getHeightWithExpand() - 21  : 0), 8, new Color(0,0,0));

        if (feature.getState()) {
            MWFont.MONTSERRAT_BOLD.get(14).draw(getName() + ":", x + 2, dropDownBoxY - 10, new Color(255, 255, 255, 255).getRGB());
            MWFont.MONTSERRAT_BOLD.get(14).draw(selectedText, x +2, dropDownBoxY, new Color(225, 225, 225, 255).getRGB());
            FontStorage.buttonFontRender.draw(isExpanded() ? "⌃" : "⌄", x + width - 4, dropDownBoxY + 3.0F, isExpanded() ? new Color(132, 132, 132, 255).getRGB() : new Color(156, 156, 156, 255).getRGB());
        }else{
            MWFont.MONTSERRAT_BOLD.get(14).draw(getName() + ":", x + 2, dropDownBoxY - 10, new Color(55, 55, 55, 255).getRGB());
            MWFont.MONTSERRAT_BOLD.get(14).draw(selectedText, x +2, dropDownBoxY, new Color(225, 225, 225, 255).getRGB());
            FontStorage.buttonFontRender.draw(isExpanded() ? "⌃" : "⌄", x + width - 4, dropDownBoxY + 3.0F, isExpanded() ? new Color(132, 132, 132, 255).getRGB() : new Color(156, 156, 156, 255).getRGB());
        }
        GlStateManager.pushMatrix();
        this.rotateTR = Interpolator.linear(this.rotateTR, isExpanded() ? -3 : 0 , 2f/5);
        this.rotate = Interpolator.linear(this.rotate, isExpanded() ? 180 + 90 : 90 , 2f/3);
        GlStateManager.translate(x + 4 + width - 12, dropDownBoxY + 2 + this.rotateTR, 1.0);
        GlStateManager.scale(0.5, 0.5, 1.0);
        GLUtil.rotate(1.0F,1.0F,-this.rotate, () -> RenderHelper.renderTriangle(-1));
        //RenderHelper.renderTriangle(-1);

        GlStateManager.popMatrix();
        //RenderUtils2.drawTriangle(x + width - 5,y,1,2,-1);
        if (ClickGui.glow.getBoolValue()) {
            //GlowUtil.drawBlurredGlow(x + (width / 2) - (mc.fontRenderer.getStringWidth(selectedText) / 2) - ClickGui.glowStrengh.getNumberValue(), dropDownBoxY - 0.5f - ClickGui.glowStrenghver.getNumberValue(), x  + (width / 2) + (mc.fontRenderer.getStringWidth(selectedText) / 2) + ClickGui.glowStrengh.getNumberValue(), dropDownBoxY + 14 + ClickGui.glowStrenghver.getNumberValue(), ClickGui.listGlowColor.getColorValue());
        }
        if (isExpanded()) {
            //Gui.drawRect(x + Panel.X_ITEM_OFFSET, y + height, x + width - Panel.X_ITEM_OFFSET, y + getHeightWithExpand(), new Color(117, 117, 117).getRGB());
            handleRender(x, y + getHeight() + 2, width, textColor);
        }
        if (mode.equalsIgnoreCase("Rockstar New")) {
      		GlStateManager.disable(GL11.GL_SCISSOR_TEST);
      		GlStateManager.popMatrix();
      	  }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        super.onMouseClick(mouseX, mouseY, button);
        if (isExpanded()) {
            handleClick(mouseX, mouseY, getX(), getY() + getHeight() + 2, getWidth());
        }
    }

    private void handleRender(int x, int y, int width, int textColor) {

        int color = 0;
        Color onecolor = new Color(ClickGui.color.getColorValue());
        Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
        double speed = ClickGui.speed.getNumberValue();
        switch (ClickGui.clickGuiColor.currentMode) {
            case "Client":
                color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Fade":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60F * 2) % 2) - 1));
                break;
            case "Color Two":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60F * 2) % 2) - 1));
                break;
            case "Astolfo":
                color = PaletteHelper.astolfo(true, y).getRGB();
                break;
            case "Static":
                color = onecolor.getRGB();
                break;
            case "Rainbow":
                color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                break;
        }

        for (String e : listSetting.getModes()) {
            if (ClickGui.glow.getBoolValue()) {
                GlStateManager.pushMatrix();
                GL11.glPushMatrix();
                //RectHelper.drawGlow(x + Panel.X_ITEM_OFFSET + 4 - ClickGui.glowStrengh.getNumberValue(), y - 3 - ClickGui.glowStrenghver.getNumberValue(), x + Panel.X_ITEM_OFFSET + 4 + mc.fontRenderer.getStringWidth(e) + ClickGui.glowStrengh.getNumberValue(), y + 12 + ClickGui.glowStrenghver.getNumberValue(), ClickGui.listGlowColor.getColorValue());
                GL11.glEnable(GL11.GL_BLEND);
                GlStateManager.disableBlend();
                GlStateManager.disableLight(3);
                //RenderHelper.drawImage(new ResourceLocation("moonware/combat.png"), x + Panel.X_ITEM_OFFSET + 8.9f, y + Panel.ITEM_HEIGHT - 3, 32,32,new Color(31,31,31,1));
                //RectHelper.drawGlow(x + Panel.X_ITEM_OFFSET + 8.9f, y + Panel.ITEM_HEIGHT - 3, x + Panel.X_ITEM_OFFSET + 4.3f, y + Panel.ITEM_HEIGHT - 18.4f, new Color(color).getRGB());
                //RectHelper.drawGlow(x + Panel.X_ITEM_OFFSET + 9.1f, y + Panel.ITEM_HEIGHT - 4.5f, x + Panel.X_ITEM_OFFSET + 4.0f, y + Panel.ITEM_HEIGHT - 19.9f, new Color(color).getRGB());
                GL11.glPopMatrix();
                GlStateManager.popMatrix();
            }
            //RectHelper.drawGradientRect(x + Panel.X_ITEM_OFFSET, y - 3, x + width - Panel.X_ITEM_OFFSET, y + Panel.ITEM_HEIGHT - 6, new Color(15,15,20).darker().getRGB(), new Color(15,15,15).darker().getRGB());
            //RectHelper.drawCircle( x + Panel.X_ITEM_OFFSET + 7, y + Panel.ITEM_HEIGHT - 11, 365, 0, 4.2f, 2, true, new Color(38, 38, 38, 255));
            if (listSetting.currentMode.equals(e)) {
                //RectHelper.drawGradientRect(x + Panel.X_ITEM_OFFSET, y - 2, x + width - Panel.X_ITEM_OFFSET, y + Panel.ITEM_HEIGHT - 6, Cli, new Color(color).darker().getRGB());
                //RectHelper.drawCircle( x + Panel.X_ITEM_OFFSET + 7, y + Panel.ITEM_HEIGHT - 11, 365, 0, 4.3f, 2, true, new Color(color));
            }
            //RectHelper.drawCircle(x + Panel.X_ITEM_OFFSET + 7, y + Panel.ITEM_HEIGHT - 11, 365, 0, 4.2f, 2, false, new Color(color).darker());

            int f = 5;
            if (!feature.getState()) {
                if (e == listSetting.getCurrentMode()) {
                    MWFont.MONTSERRAT_BOLD.get(14).drawCenterShadow(e, x + (width / 2), y - f + 1.5f, new Color(color).getRGB());
                } else {
                    MWFont.MONTSERRAT_BOLD.get(14).drawCenterShadow(e, x + (width / 2), y - f + 1.5f, new Color(121, 121, 121, 255).getRGB());
                }
            }else{
                if (e == listSetting.getCurrentMode()) {
                    MWFont.MONTSERRAT_BOLD.get(14).drawCenterShadow(e, x + (width / 2), y - f + 1.5f, new Color(color).getRGB());
                } else {
                    MWFont.MONTSERRAT_BOLD.get(14).drawCenterShadow(e, x + (width / 2), y - f + 1.5f, new Color(121, 121, 121, 255).getRGB());
                }
            }
            y += (org.moonware.client.ui.clickgui.Panel.ITEM_HEIGHT - 3);
        }
    }

    private void handleClick(int mouseX, int mouseY, int x, int y, int width) {
        for (String e : listSetting.getModes()) {
            if (mouseX >= x && mouseY >= y - 5 && mouseX <= x + width && mouseY <= y - 5 + Panel.ITEM_HEIGHT - 3) {
                listSetting.setListMode(e);
            }

            y += org.moonware.client.ui.clickgui.Panel.ITEM_HEIGHT - 3;
        }
    }
    private int height = getHeight();
    @Override
    public int getHeightWithExpand() {
        this.height = Interpolator.linear(this.height, getHeight() + (listSetting.modes.toArray().length) * (Panel.ITEM_HEIGHT - 3) + 2, 2f/10);
        return getHeight() + (listSetting.modes.toArray().length) * (15 - 3) + 2 - 2;
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
    }

    @Override
    public boolean canExpand() {
        return listSetting.modes.toArray().length > 0;
    }

    @Override
    public Setting getSetting() {
        return listSetting;
    }
}