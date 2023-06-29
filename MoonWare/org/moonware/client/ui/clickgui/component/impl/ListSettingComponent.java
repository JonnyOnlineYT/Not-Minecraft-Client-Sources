package org.moonware.client.ui.clickgui.component.impl;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.FontStorage;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWFont;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.ui.clickgui.Panel;
import org.moonware.client.ui.clickgui.component.Component;
import org.moonware.client.ui.clickgui.component.ExpandableComponent;
import org.moonware.client.ui.clickgui.component.PropertyComponent;

import java.awt.*;

public class ListSettingComponent extends ExpandableComponent implements PropertyComponent {

    private final ListSetting listSetting;
    private int animhover;

    public ListSettingComponent(Component parent, ListSetting listSetting, int x, int y, int width, int height) {
        super(parent, listSetting.getName(), x, y, width, height);
        this.listSetting = listSetting;
        animhover = 0;
    }

    @Override
    public Setting getSetting() {
        return listSetting;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        String selectedText = listSetting.getCurrentMode();
        int dropDownBoxY = y + 10;
        int textColor = 0xFFFFFF;
        if (ClickGui.colored.get())
            RectHelper.drawRect(x - 2,y,x + width + 2,y + (isExpanded() ? getHeightWithExpand() : height),new Color(51,51,51).getRGB());
        //Gui.drawRect(x, y, x + width, y + height, new Color(15, 15, 15).getRGB());
        //RectHelper.drawGradientRect(x + Panel.X_ITEM_OFFSET, y - 4, x + width - Panel.X_ITEM_OFFSET, y + 12, new Color(15,15,20).darker().getRGB(), new Color(15,15,15).darker().getRGB());

        if (ClickGui.glow.getBoolValue()) {
            //GlowUtil.drawBlurredGlow(x + 2 - ClickGui.glowStrengh.getNumberValue(), y - 1 - ClickGui.glowStrenghver.getNumberValue(), x + mc.circleregularSmall.getStringWidth(getName()) + ClickGui.glowStrengh.getNumberValue(), y + 12 + ClickGui.glowStrenghver.getNumberValue(), ClickGui.listGlowColor.getColorValue());
        }
        //Gui.drawRect(x + 2, dropDownBoxY, x + getWidth() - 2, (int) (dropDownBoxY + 10.5), new Color(30, 30, 30).getRGB());
        //RectHelper.drawRect(x, dropDownBoxY - 11.5f, x + getWidth(), dropDownBoxY + 11.5f, new Color(0x101010).getRGB());
        MWFont.GREYCLIFFCF_MEDIUM.get(18).draw(getName(), x + 2, dropDownBoxY-6, new Color(255, 255, 255,255).getRGB());
        MWFont.GREYCLIFFCF_MEDIUM.get(16).draw(selectedText, x + width - MWFont.GREYCLIFFCF_MEDIUM.get(16).getWidth(selectedText), dropDownBoxY-5, new Color(225, 225, 225,255).getRGB());
        FontStorage.buttonFontRender.draw(isExpanded() ? "⌃" : "⌄",  x + width - 4, dropDownBoxY + 3.0F,isExpanded() ? new Color(132,132,132,255).getRGB() : new Color(156,156,156,255).getRGB());
        if (ClickGui.glow.getBoolValue()) {
            //GlowUtil.drawBlurredGlow(x + (width / 2) - (mc.fontRenderer.getStringWidth(selectedText) / 2) - ClickGui.glowStrengh.getNumberValue(), dropDownBoxY - 0.5f - ClickGui.glowStrenghver.getNumberValue(), x  + (width / 2) + (mc.fontRenderer.getStringWidth(selectedText) / 2) + ClickGui.glowStrengh.getNumberValue(), dropDownBoxY + 14 + ClickGui.glowStrenghver.getNumberValue(), ClickGui.listGlowColor.getColorValue());
        }
        if (isExpanded()) {
            //Gui.drawRect(x + Panel.X_ITEM_OFFSET, y + height, x + width - Panel.X_ITEM_OFFSET, y + getHeightWithExpand(), new Color(117, 117, 117).getRGB());
            handleRender(x, y + getHeight() + 2, width, textColor);
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
            if (e == listSetting.getCurrentMode()) {
                MWFont.GREYCLIFFCF_MEDIUM.get(16).drawCenterShadow(e, x + (width / 2), y + 1.5f, new Color(159, 159, 159, 255).getRGB());
            }else{
                MWFont.GREYCLIFFCF_MEDIUM.get(16).drawCenterShadow(e, x + (width / 2), y + 1.5f, new Color(121, 121, 121, 255).getRGB());
            }
            y += (Panel.ITEM_HEIGHT - 3);
        }
    }

    private void handleClick(int mouseX, int mouseY, int x, int y, int width) {
        for (String e : listSetting.getModes()) {
            if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + Panel.ITEM_HEIGHT - 3) {
                listSetting.setListMode(e);
            }

            y += Panel.ITEM_HEIGHT - 3;
        }
    }

    @Override
    public int getHeight() {
        return 15;
    }

    @Override
    public int getHeightWithExpand() {
        animhover = Interpolator.linear(animhover,getHeight() + listSetting.getModes().toArray().length * (Panel.ITEM_HEIGHT - 3),2f / 15);
        return animhover + 4;
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
    }

    @Override
    public boolean canExpand() {
        return listSetting.getModes().toArray().length > 0;
    }
}
