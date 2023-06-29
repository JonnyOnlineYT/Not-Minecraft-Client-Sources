package ru.rockstar.client.ui.clickgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.client.features.impl.display.ClickGUI;
import ru.rockstar.client.ui.clickgui.ClickGuiScreen;
import ru.rockstar.client.ui.clickgui.Panel;
import ru.rockstar.client.ui.clickgui.component.Component;
import ru.rockstar.client.ui.clickgui.component.ExpandableComponent;
import ru.rockstar.client.ui.clickgui.component.PropertyComponent;
import ru.rockstar.client.ui.settings.Setting;
import ru.rockstar.client.ui.settings.impl.ListSetting;

import java.awt.*;

import org.lwjgl.opengl.GL11;

import javafx.animation.Interpolator;


public class ListSettingComponent extends ExpandableComponent implements PropertyComponent {

    private final ListSetting listSetting;
    private int h;
    Minecraft mc = Minecraft.getMinecraft();

    public ListSettingComponent(Component parent, ListSetting listSetting, int x, int y, int width, int height) {
        super(parent, listSetting.getName(), x, y, width, height);
        this.listSetting = listSetting;
    }
    public float progress;
    private long lastMS;
    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
    	
        super.drawComponent(scaledResolution, mouseX, mouseY);
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
        String selectedText = listSetting.currentMode;
        int dropDownBoxY = y + 10;
        int textColor = new Color(180, 180, 180).getRGB();
        
        
        RoundedUtil.drawRound(x + Panel.X_ITEM_OFFSET - 3 + 5, y + height - 25, x + width - Panel.X_ITEM_OFFSET - 5 + 3 - (x + Panel.X_ITEM_OFFSET - 3 + 5), y + (isExpanded() ? 0 : 23) + getHeightWithExpand() - (y + height - 25), 3, new Color(0, 0, 0, 55));
        
        mc.mntsb_13.drawCenteredString(getName(), x + width - 46, y + 3.5F + 1, -1);
        
        
        mc.mntsb_15.drawCenteredString(selectedText, x + width / 2 + Panel.X_ITEM_OFFSET, dropDownBoxY + 2.5F + 1, Color.WHITE.getRGB());
        mc.neverlose500_18.drawString(isExpanded() ? "<" : ">", x + width - Panel.X_ITEM_OFFSET - 9, y + 20.5f - 8, Color.WHITE.getRGB());
        
        
        if (isExpanded()) {
        	
        	DrawHelper.drawTribleGradient(x + Panel.X_ITEM_OFFSET - 3 + 5 + 10 + 35.5f - 35.5f * progress, y + height  - 5 + 2, 71 * progress, 0.5f, new Color(255,255,255,0), new Color(255,255,255), new Color(255,255,255,0));
        	   
        	
        	Gui.drawRect(x, y, x, y, new Color(0, 0, 0, 55).getRGB());
        	DrawHelper.drawGradientRect(x, y, x, y, new Color(0, 0, 0, 55).getRGB(), new Color(0, 0, 0, 0).getRGB());
        	DrawHelper.drawGradientRect(x, y, x, y, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 55).getRGB());
        	
        	
        	
        	handleRender(x, y + getHeight() + 2, width, textColor);
        } else {
        	this.lastMS = System.currentTimeMillis();
        	this.progress = 0;
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

        for (String e : listSetting.modes) {
            if(!e.equalsIgnoreCase(listSetting.currentMode)) {
                mc.neverlose500_13.drawCenteredStringWithShadow(e, x + Panel.X_ITEM_OFFSET + width / 2, y + 2.5F, listSetting.currentMode.equals(e) ? textColor : Color.WHITE.getRGB());
                y += (Panel.ITEM_HEIGHT - 3);
            }
        }
    }

    private void handleClick(int mouseX, int mouseY, int x, int y, int width) {
        for (String e : this.listSetting.modes) {
            if(!e.equalsIgnoreCase(listSetting.currentMode)) {
                if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + Panel.ITEM_HEIGHT - 3) {
                    listSetting.setListMode(e);
                }

                y += Panel.ITEM_HEIGHT - 3;
            }
        }
    }

    @Override
    public int getHeightWithExpand() {
    	this.progress = (float)Interpolator.LINEAR.interpolate((double)this.progress, isExpanded() ? 1 : 0, 0.01 * ClickGUI.animateSpeed.getNumberValue());

    	h = 0;
    	int cHeight = getHeight() + (listSetting.modes.toArray().length - 1) * (Panel.ITEM_HEIGHT - 3);
                h = (int) (cHeight * progress);
        return h;
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