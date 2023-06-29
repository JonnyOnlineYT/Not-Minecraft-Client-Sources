package org.moonware.client.ui.clickgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.moonware.client.helpers.render.GlowUtil;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWFont;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.clickgui.ClickGuiScreen;
import org.moonware.client.ui.clickgui.Panel;
import org.moonware.client.ui.clickgui.component.AnimationState;
import org.moonware.client.ui.clickgui.component.Component;
import org.moonware.client.ui.clickgui.component.ExpandableComponent;

import java.awt.*;

public final class ModuleComponent extends ExpandableComponent implements Helper {

    public final Feature module;
    private final AnimationState state;
    private boolean binding;
    private int buttonLeft;
    private int buttonTop;
    private int buttonRight;
    private int buttonBottom;
    private float moduleHeightAnim;
    public static int MouseX;
    public static int MouseY;
    private int hoverAnim;



    public ModuleComponent(org.moonware.client.ui.clickgui.component.Component parent, Feature module, int x, int y, int width, int height) {
        super(parent, module.getLabel(), x, y, width, height);
        this.module = module;
        state = AnimationState.STATIC;
        hoverAnim = 0;
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
        //components.add(new VisibleComponent(module, this, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT));
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        //components.sort(new SorterHelper());
        float x = getX();
        float y = getY() - 2;
        int width = getWidth();
        int height = getHeight();
        if (isExpanded()) {
            int childY = Panel.ITEM_HEIGHT;
            for (org.moonware.client.ui.clickgui.component.Component child : components) {
                int cHeight = child.getHeight();
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
                if (child instanceof BooleanSettingComponent) {
                    BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent) child;
                    if (!booleanSettingComponent.booleanSetting.isVisible()) {
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

        if (!ClickGuiScreen.search.getText().isEmpty() && !module.getLabel().toLowerCase().contains(ClickGuiScreen.search.getText().toLowerCase())) {
            return;
        }
        if (ClickGui.glow.getBoolValue()) {
            if (module.getState()) {
                //RectHelper.drawGlow(x + 5, y + height / 2F - 3, mc.montserratRegular.getStringWidth(getName()), mc.montserratRegular.getFontHeight(), color);
                //RectHelper.drawGlow(mc.motBold.getStringWidth(getName()), y + height / 2F - 3, x + 5, y + height / 2F - 3, color);
                GlStateManager.pushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GlStateManager.popMatrix();
                //RectHelper.drawGlow(x + 5, y + height / 2F - 3, mc.montserratRegular.getStringWidth(getName()), 2, new Color(0x1BFFFFFF, true).getRGB());
            }
        }


        int color = 0;
        Color onecolor = new Color(ClickGui.color.getColorValue());
        Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
        double speed = ClickGui.speed.getNumberValue();
        switch (ClickGui.clickGuiColor.currentMode) {
            case "Client":
                color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Fade":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Color Two":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Astolfo":
                color = PaletteHelper.astolfo(true, (int) y).getRGB();
                break;
            case "Rainbow":
                color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                break;
            case "Category":
                org.moonware.client.ui.clickgui.Panel panel = (Panel) parent;
                color = panel.type.getColor();
                break;
        }
        switch (ClickGui.mode.currentMode) {
            case "White":
                color = new Color(255, 255, 255, 186).getRGB();
        }

        boolean hovered = isHovered(mouseX, mouseY);



        if (components.size() > 1) {
            //mc.buttonFontRender.drawString(isExpanded() ? TextFormatting.BOLD + "--" : TextFormatting.BOLD + "--", x + width - 10, y + height / 2F - 8, Color.GRAY.getRGB());
        }

        int middleHeight = getHeight() / 2;
        int btnRight = (int) (x + 3 + middleHeight);

        //RectHelper.drawRect(x - 1, y + height / 1.5F + 5F, x + 20, y, new Color(20, 20, 20, 200).getRGB());

        //gui.drawGradientRect(buttonLeft = (int) (x + 5), buttonTop = (int) (y + middleHeight - (middleHeight / 2 + 1)), buttonRight = btnRight + 3, buttonBottom = (int) (y + middleHeight + (middleHeight / 2) + 1), 0xFF6B6B6B, new Color(0xFF6B6B6B).darker().darker().getRGB());

        //RectHelper.drawRect(buttonLeft + 0.5, buttonTop + 0.5, buttonRight - 0.5, buttonBottom - 0.5, 0xFF3C3F41);

        //if (module.getState()) {
        //    gui.drawGradientRect(buttonLeft = (int) (x + 6.5), buttonTop = (int) (y + middleHeight - (middleHeight / 2)), buttonRight = (int) (btnRight + 2.5), buttonBottom = (int) (y + middleHeight + (middleHeight / 2)), color, new Color(color).darker().darker().getRGB());
        //}
        float xGlow = (float) (((double) ((float) ((int) x + width - 62) - (float) MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth(getName()) / 2.0f + 1.0f)));
        double widthGlow = xGlow + ((double) (MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth(getName()) + 3)) / 0.8F;
        int widthGlowOld = (int) (xGlow + ((double) (MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth(getName()) + 3)));
        if (module.getState() && ClickGui.glow.getBoolValue()) {
            if (ClickGui.glowMode.getCurrentMode().equalsIgnoreCase("Rect")) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.6D,0.6D, 0.6D);
                double xGloww = (((double) ((float) ((int) x + width - 62) - (float) MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth(getName()) / 2.0f + 1.0f)) / 0.6F);
                double yGlow = ((double) ((float) ((int) y) + (float) height / 2.0f - 2.0f - moduleHeightAnim)) / 0.6F;
                widthGlow = ((double) (MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth(getName()) + 3)) / 0.6F;
                double heightGlow = ((double) (MWFont.SF_UI_DISPLAY_REGULAR.get(18).getHeight() + 3)) / 0.6F;
                RenderHelper2.renderBlurredShadow(new Color(color).darker(), xGloww, yGlow, widthGlow, heightGlow, 14);
                GlStateManager.popMatrix();
            }else if (ClickGui.glowMode.getCurrentMode().equalsIgnoreCase("Old")) {
                //RenderHelper2.renderBlurredShadow(new Color(color),(x + (width / 2) - (mc.fontRendererObj.getStringWidth(!binding ? module.getLabel() : "Press a key... Key: " + module.getBind()) / 2)) + 5.6f, (int) (y - 1), (x + (width / 2) + (mc.fontRendererObj.getStringWidth(!binding ? module.getLabel() : "Press a key... Key: " + module.getBind()) / 2)) - 6.95f, (int)(y + height + 0.1),26);
                GlowUtil.drawBlurredGlow( (int) xGlow + 5F, (int) (y - moduleHeightAnim + 4.22F), widthGlowOld - 5,(int) ((y + height + 0.1F) - moduleHeightAnim + 2.22F), color);
            }
        }
        moduleHeightAnim = isHovered(mouseX, mouseY) ? (float)((double) moduleHeightAnim + (double)0.2f * Minecraft.frameTime * (double)0.1f) : (float)((double) moduleHeightAnim - (double)0.2f * Minecraft.frameTime * (double)0.1f);
        moduleHeightAnim = MathHelper.clamp(moduleHeightAnim, 4.0f, 6.0f);
        moduleHeightAnim = 4.0F;
        String name = binding ? "Press a key..." : getName();
        MWFont.GREYCLIFFCF_MEDIUM.get(21).drawCenterShadow(name, x + width /2, y + height / 2.0f - moduleHeightAnim, module.getState() ? new Color(color).brighter().getRGB() : new Color(221,221,221).getRGB());
        /*
        if (module.getState()) {
            mc.motBold.drawCenteredString(binding ? "Press a key... Key: " + Keyboard.getKeyName(module.getBind()) : getName(), width / 2 + x, y + height / 2F - 3, color);

            //mc.motBold.drawString(binding ? "Press a key... Key: " + Keyboard.getKeyName(module.getBind()) : getName(), x + 5, y + height / 2F - 3, color);

        } else {
            mc.motBold.drawCenteredString(binding ? "Press a key... Key: " + Keyboard.getKeyName(module.getBind()) : getName(), width / 2 + x, y + height / 2F - 3, hovered ? Color.LIGHT_GRAY.getRGB() : module.getState() ? Color.LIGHT_GRAY.getRGB() : Color.GRAY.getRGB());
            //mc.motBold.drawString(binding ? "Press a key... Key: " + Keyboard.getKeyName(module.getBind()) : getName(), x + 5, y + height / 2F - 3, hovered ? Color.LIGHT_GRAY.getRGB() : module.getState() ? Color.LIGHT_GRAY.getRGB() : Color.GRAY.getRGB());
        }

         */
        if (hovered) {
            MouseX = mouseX;
            MouseY = mouseY;
            //RectHelper.drawBorderedRect(x + width + 18, y + height / 1.5F + 3.5F, x + width + 25 + mc.fontRendererObj.getStringWidth(module.getDesc()), y + 3.5F, 0.5F, new Color(30, 30, 30, 150).getRGB(), new Color(30,30,30,150).getRGB(), true);
            //mc.fontRendererObj.drawStringWithShadow(module.getDesc(), x + width + 22, y + height / 1.35F - 6F, -1);
            //RectHelper.drawGlowRoundedRect(mouseX + 7, mouseY, mouseY + mc.fontRendererObj.getStringWidth(module.getDesc() + 13), mouseY + 11.5f, 1, 6, 10);
            //RectHelper.drawBorderedRect(mouseX + 7, mouseY, mouseX + mc.font.getStringWidth(module.getDesc()) + 13, mouseY + 11.5F, 0.5F, new Color(30, 30, 30, 150).getRGB(), new Color(30,30,30,150).getRGB(), true);
            //mc.font.drawStringWithShadow(module.getDesc(), mouseX + 9, mouseY + 2, -1);
        }

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
            module.setBind(keyCode == Keyboard.KEY_ESCAPE ? Keyboard.KEY_NONE : keyCode);
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
        if (isExpanded()) {
            hoverAnim = Interpolator.linear(hoverAnim, height, 2f / 20);
        }
        return hoverAnim;
    }

}
