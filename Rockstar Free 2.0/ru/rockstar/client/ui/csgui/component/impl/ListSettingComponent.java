package ru.rockstar.client.ui.csgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.impl.display.ClickGUI;
import ru.rockstar.client.ui.csgui.Panel;
import ru.rockstar.client.ui.csgui.component.Component;
import ru.rockstar.client.ui.csgui.component.ExpandableComponent;
import ru.rockstar.client.ui.csgui.component.PropertyComponent;
import ru.rockstar.client.ui.settings.Setting;
import ru.rockstar.client.ui.settings.impl.ListSetting;

import java.awt.*;


public class ListSettingComponent extends ExpandableComponent implements PropertyComponent {

    private final ListSetting listSetting;
    Minecraft mc = Minecraft.getMinecraft();

    public ListSettingComponent(Component parent, ListSetting listSetting, int x, int y, int width, int height) {
        super(parent, listSetting.getName(), x, y, width, height);
        this.listSetting = listSetting;
    }
    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        String selectedText = listSetting.currentMode;
        int dropDownBoxY = y + 10;
        int textColor = new Color(180, 180, 180).getRGB();
        mc.neverlose500_13.drawCenteredString(getName(), x + width - 46, y + 3.5F, new Color(200, 200, 200).getRGB());
        Gui.drawRect(x + 2, dropDownBoxY, x + getWidth() - 2, (int) (dropDownBoxY + 9.5), new Color(20, 20, 20, 100).getRGB());
        Gui.drawRect(x + 1.5, dropDownBoxY + 0.5, x + getWidth() - 1.5, dropDownBoxY + 9, new Color(20, 20, 20, 100).getRGB());
        mc.neverlose500_15.drawCenteredString(selectedText, x + width / 2 + Panel.X_ITEM_OFFSET, dropDownBoxY + 2.5F, new Color(200, 200, 200).getRGB());
        mc.neverlose500_18.drawString(isExpanded() ? "<" : ">", x + width - Panel.X_ITEM_OFFSET - 8, y + height - 9, new Color(200, 200, 200).getRGB());
        if (isExpanded()) {
            Gui.drawRect(x + Panel.X_ITEM_OFFSET, y + height, x + width - Panel.X_ITEM_OFFSET, y + getHeightWithExpand(), new Color(50,  50, 50, 100).getRGB());
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

        for (String e : listSetting.modes) {
            if(!e.equalsIgnoreCase(listSetting.currentMode)) {
                mc.neverlose500_13.drawCenteredString(e, x + Panel.X_ITEM_OFFSET + width / 2, y + 2.5F, listSetting.currentMode.equals(e) ? textColor : Color.GRAY.getRGB());
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
        return getHeight() + (listSetting.modes.toArray().length - 1) * (Panel.ITEM_HEIGHT - 3);
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