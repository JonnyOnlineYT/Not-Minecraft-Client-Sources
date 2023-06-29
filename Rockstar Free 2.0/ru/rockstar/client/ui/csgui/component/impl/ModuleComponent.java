package ru.rockstar.client.ui.csgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import ru.rockstar.Main;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.display.ClickGUI;
import ru.rockstar.client.ui.csgui.ClickGuiScreen;
import ru.rockstar.client.ui.csgui.Panel;
import ru.rockstar.client.ui.csgui.SorterHelper;
import ru.rockstar.client.ui.csgui.component.AnimationState;
import ru.rockstar.client.ui.csgui.component.Component;
import ru.rockstar.client.ui.csgui.component.ExpandableComponent;
import ru.rockstar.client.ui.settings.Setting;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import org.lwjgl.input.Keyboard;

import org.lwjgl.opengl.GL11;

import java.awt.*;


public final class ModuleComponent extends ExpandableComponent  {
    Minecraft mc = Minecraft.getMinecraft();
    private final Feature module;
    private final AnimationState state;
    private boolean binding;
    private int buttonLeft;
    private int buttonTop;
    private int buttonRight;
    private int buttonBottom;

    public ModuleComponent(Component parent, Feature module, int x, int y, int width, int height) {
        super(parent, module.getLabel(), x, y, width, height);
        this.module = module;
        this.state = AnimationState.STATIC;
        int propertyX = Panel.X_ITEM_OFFSET;
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                components.add(new BooleanSettingComponent(this, (BooleanSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 6));
            } else if (setting instanceof ColorSetting) {
                components.add(new ColorPickerComponent(this, (ColorSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT));
            } else if (setting instanceof NumberSetting) {
                components.add(new NumberSettingComponent(this, (NumberSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 5));
            } else if (setting instanceof ListSetting) {
                components.add(new ListSettingComponent(this, (ListSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 7));
            }
        }
    }
    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
         components.sort(new SorterHelper());

        float x = getX();
        float y = getY();
        int width = getWidth();
        int height = getHeight();
        if (isExpanded()) {
            int childY = Panel.ITEM_HEIGHT;
            for (Component child : components) {
                int cHeight = child.getHeight();
                if (child instanceof BooleanSettingComponent) {
                    BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent) child;
                    if (!booleanSettingComponent.booleanSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof NumberSettingComponent) {
                    NumberSettingComponent numberSettingComponent = (NumberSettingComponent) child;
                    if (!numberSettingComponent.numberSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ColorPickerComponent) {
                    ColorPickerComponent colorPickerComponent = (ColorPickerComponent) child;
                    if (!colorPickerComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ListSettingComponent) {
                    ListSettingComponent listSettingComponent = (ListSettingComponent) child;
                    if (!listSettingComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) child;
                    if (expandableComponent.isExpanded())
                        cHeight = expandableComponent.getHeightWithExpand();
                }
                child.setY(childY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                childY += cHeight;
            }
        }

  //     if (!ClickGuiScreen.search.getText().isEmpty() && !this.module.getLabel().toLowerCase().contains(ClickGuiScreen.search.getText().toLowerCase())) {
   //         return;
 //       }

        int color = 0;

        Color onecolor = new Color(ClickGUI.color.getColorValue());
        Color twocolor = new Color(ClickGUI.colorTwo.getColorValue());
        double speed = ClickGUI.speed.getNumberValue();
        switch (ClickGUI.clickGuiColor.currentMode) {
            case "Client":
                color = DrawHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Fade":
                color = DrawHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Color Two":
                color = DrawHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Astolfo":
                color = DrawHelper.astolfo(true, (int) y).getRGB();
                break;
            case "Rainbow":
                color = DrawHelper.rainbow(300, 1, 1).getRGB();
                break;
            case "Category":
                Panel panel = (Panel) parent;
                color = panel.type.getColor();
                break;

        }

        boolean hovered = isHovered(mouseX, mouseY);
        
        DrawHelper.drawNewRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
        if (hovered) {
        	ScaledResolution sr = new ScaledResolution(mc);
            DrawHelper.drawNewRect(33, sr.getScaledHeight() - 23, 25 + mc.fontRendererObj.getStringWidth(module.getDesc())+ 16, sr.getScaledHeight() - 7, new Color(30, 30, 30, 200).getRGB());
            DrawHelper.drawGradientRect(31, sr.getScaledHeight() - 23, 33, sr.getScaledHeight() - 7, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 70);
            DrawHelper.drawGradientRect(31 + mc.fontRendererObj.getStringWidth(module.getDesc())+ 10, sr.getScaledHeight() - 23, 33 + mc.fontRendererObj.getStringWidth(module.getDesc())+ 10, sr.getScaledHeight() - 7, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 70);
            mc.fontRendererObj.drawStringWithShadow(module.getDesc(), 36, sr.getScaledHeight() - 20, -1);
        }


      if (components.size() > 1) {
            mc.neverlose500_15.drawStringWithShadow(isExpanded() ? "<" : ">", x + width - 13, y + height / 2F - 3.5, Color.GRAY.getRGB());
        }

      if (module.isToggled()) {
    	  if (ClickGUI.glow.getBoolValue()) {
    		  DrawHelper.drawGlow(x - mc.neverlose500_15.getStringWidth(getName()) / 2 + 50 + 2, y + height / 2F - 10, x - mc.neverlose500_15.getStringWidth(getName()) / 2 + 40 + mc.neverlose500_15.getStringWidth(getName()) - 2, y + height / 2F + 7, color - new Color(0, 0, 0, 150).getRGB());
    	  }
    	  }
      components.sort(new SorterHelper());
      mc.neverlose500_13.drawCenteredStringWithShadow(binding ? "Press a key.. " + Keyboard.getKeyName(module.getKey()) : getName(), x + 45, y + height / 2F - 3 - ClickGUI.fontY.getNumberValue(), module.isToggled() ? color : Color.GRAY.getRGB());
    }

    @Override
    public boolean canExpand() {
        return !components.isEmpty();
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        switch (button) {
            case 0:
                module.toggle();
                break;
            case 2:
                binding = !binding;
                break;
        }

    }

    @Override
    public void onKeyPress(int keyCode) {
        if (binding) {
            ClickGuiScreen.escapeKeyInUse = true;
            module.setKey(keyCode == Keyboard.KEY_ESCAPE ? Keyboard.KEY_NONE : keyCode);
            binding = false;
        }
    }

    @Override
    public int getHeightWithExpand() {
        int height = getHeight();
        if (isExpanded()) {
            for (Component child : components) {
                int cHeight = child.getHeight();
                if (child instanceof BooleanSettingComponent) {
                    BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent) child;
                    if (!booleanSettingComponent.booleanSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof NumberSettingComponent) {
                    NumberSettingComponent numberSettingComponent = (NumberSettingComponent) child;
                    if (!numberSettingComponent.numberSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ColorPickerComponent) {
                    ColorPickerComponent colorPickerComponent = (ColorPickerComponent) child;
                    if (!colorPickerComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ListSettingComponent) {
                    ListSettingComponent listSettingComponent = (ListSettingComponent) child;
                    if (!listSettingComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) child;
                    if (expandableComponent.isExpanded())
                        cHeight = expandableComponent.getHeightWithExpand();
                }
                height += cHeight;
            }
        }
        return height;
    }

}
