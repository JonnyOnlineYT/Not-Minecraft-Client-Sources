package org.moonware.client.ui.sqgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.render.GLUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.helpers.render.GlowUtil;
import org.moonware.client.helpers.render.RenderHelper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.sqgui.GuiScreen;
import org.moonware.client.ui.sqgui.Panel;
import org.moonware.client.ui.sqgui.component.AnimationState;
import org.moonware.client.ui.sqgui.component.Component;
import org.moonware.client.ui.sqgui.component.ExpandableComponent;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWFont;

import java.awt.*;


public final class ModuleComponent extends ExpandableComponent {
    Minecraft mc = Minecraft.getMinecraft();
    private final Feature module;
    private final AnimationState state;
    private boolean binding;
    private int prevY;
    private int buttonTop;
    private int descTimerAnim;
    private int prevX;
    private int heightAnimation;
    private int rotateAnim;

    public ModuleComponent(Component parent, Feature module, int x, int y, int width, int height) {
        super(parent, module.getLabel(), x, y, width, height);
        this.module = module;
        state = AnimationState.STATIC;
        int propertyX = Panel.X_ITEM_OFFSET;
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                components.add(new BooleanSettingComponent(this, (BooleanSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 6,module));
            } else if (setting instanceof ColorSetting) {
                components.add(new ColorPickerComponent(this, (ColorSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT,module));
            } else if (setting instanceof NumberSetting) {
                components.add(new NumberSettingComponent(this, (NumberSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 5,module));
            } else if (setting instanceof ListSetting) {
                components.add(new ListSettingComponent(this, (ListSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 7,module));
            }
        }
    }
    private int height = getHeight();
    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        float x = getX();
        boolean a = true;
        float y = a ? ((getYModule() - 2)) : (-1000);
        int width = getWidth();
        int height = getHeight();

        //     if (!GuiScreen.search.getText().isEmpty() && !this.module.getLabel().toLowerCase().contains(GuiScreen.search.getText().toLowerCase())) {
        //         return;
        //       }

        int color = 0;

        Color onecolor = new Color(ClickGui.color.getColorValue());
        Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
        double speed = ClickGui.speed.getNumberValue();
        switch (ClickGui.clickGuiColor.currentMode) {
            case "Client":
                color = DrawHelper.fadeColorRich(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Fade":
                color = DrawHelper.fadeColorRich(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Color Two":
                color = DrawHelper.fadeColorRich(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Astolfo":
                color = DrawHelper.astolfo(20, (int) y);
                break;
            case "Rainbow":
                color = DrawHelper.rainbow(300, 1, 1);
                break;
            case "Category":
                Panel panel = (Panel) parent;
                color = panel.type.getColor();
                break;

        }

        boolean hovered = false; //isHovered(mouseX, mouseY);
        ScaledResolution sr = new ScaledResolution(mc);
            GlStateManager.pushMatrix();
            GlStateManager.enable(GL11.GL_SCISSOR_TEST);
            RenderHelper2.scissorRect(0, getY2() + 47, sr.getScaledWidth(), getY2() + 260);
            DrawHelper.drawNewRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());

        this.heightAnimation = Interpolator.linear(this.heightAnimation, isExpanded() ? getHeightWithExpand() - 7 : 0, 2f / 2);
        this.rotateAnim = Interpolator.linear(this.rotateAnim,  isExpanded() ? 180 + 90 : 90    , 2F / 3);
        //RenderUtils2.drawBlurredShadow(0, 0, 0, 0, 7, new Color(0, 0, 0,40));
        if (module.getState()) {
            //RenderHelper2.drawRainbowRoundGui(x - 1, y + 5 - 5 + 2, width + 3, height + 5 - 10 + (heightAnimation) + 3.07f, 0.1F, true, false, false, true, 2, 4);
        } else {
            //RenderHelper2.drawRainbowRoundGui(x - 1, y + 5 - 5 + 2, width + 3, height + 5 - 10 + (heightAnimation) + 3.07f, 0.1F, true, false, false, true, 2, 4);
        }
        if (isExpanded()) {
            GlowUtil.drawBlurredShadow(x - 1.5f, y + 5 - 5 + 2 + height, width + 3, 5 - 10 + (heightAnimation) + 3.5f, 20, new Color(42, 42, 42, 96),0);

        }
        this.buttonTop = Interpolator.linear(this.buttonTop, isExpanded() ? 2 : 0, 2F / 5);

        MWFont.GREYCLIFFCF_MEDIUM.get(16).drawCenterShadow((binding ? "Press a key.. " + Keyboard.getKeyName(module.getBind()) : getName()), x + width / 2, y + height / 2F - 3 + 2, !module.getState() ? Color.WHITE.getRGB() : ClickGui.color.getColorValue());

        if (module.getSettings().size() > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + width - 5, y + height / 2F - 3 - 7  + 11,1.0F);
            GlStateManager.scale(0.5, 0.5, 1.0);
            GLUtil.rotate(1.0F, 1.0F, this.rotateAnim, () -> RenderHelper.renderTriangle(module.getState() ? -1 : -1));
            GlStateManager.popMatrix();
        }
       if (isExpanded()) {
            int childY = 15;
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
        //RenderUtils2.drawShadow(7,8,()-> RenderHelper2.drawRainbowRoundGui(x, y, width,height, 2, true, true,false, true, 2,4));
        if (module.getState()) {

            DrawHelper.drawNewRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
            if (ClickGui.glow.getBoolValue()) {
                RenderUtils2.drawBlurredShadow(x - ClientHelper.getFontRender().getWidth(getName()) / 2 + 50 + 2 - 5, y + ClientHelper.getFontRender().getHeight() / 2F - 4 - 0 - (hovered ? 1 : 0), x - ClientHelper.getFontRender().getWidth(getName()) / 2 + 50 + ClientHelper.getFontRender().getWidth(getName()) - 2 - (x - ClientHelper.getFontRender().getWidth(getName()) / 2 + 50 + 2) + 15 - 2, y + ClientHelper.getFontRender().getHeight() / 2F + 11 - 0 - (hovered ? 1.5f : 0) - (y + ClientHelper.getFontRender().getHeight() / 2F - 4 - 0 - (hovered ? 1 : 0)), 20, new Color(255, 255, 255, 150));
            }
            DrawHelper.drawNewRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());


        }

        DrawHelper.drawNewRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
        this.descTimerAnim = (int) AnimationHelper.calculateCompensation(isHovered(mouseX,mouseY) ? 255 : 0, this.descTimerAnim, 6, 16);
        GlStateManager.disable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        if (mouseY < 260 && mouseY > 47) {
            RoundedUtil.drawRound(x - 5 - MWFont.MONTSERRAT_BOLD.get(16).getWidth(module.getDesc()) - 5, y + getHeight() / 2 - MWFont.MONTSERRAT_BOLD.get(16).getHeight() + 5 - 5, MWFont.MONTSERRAT_BOLD.get(16).getWidth(module.getDesc()) + 5, MWFont.MONTSERRAT_BOLD.get(16).getHeight() + 5, 1.5F, new Color(38, 38, 38, this.descTimerAnim));
            MWFont.MONTSERRAT_BOLD.get(16).drawShadow(module.getDesc(), x - 3 - MWFont.MONTSERRAT_BOLD.get(16).getWidth(module.getDesc()) - 5, y + getHeight() / 2 + 3 - 5, new Color(255, 255, 255, this.descTimerAnim).getRGB());
        }else {
            this.descTimerAnim = 0;
        }
    }

    @Override
    public boolean canExpand() {
        return !components.isEmpty();
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
    	if (true) {
    		if (mouseY > 47 && mouseY <  260) {
        		switch (button) {
                case 0:
                    module.toggle();
                    this.prevX = mouseX;
                    this.prevY = mouseY;
                    break;
                case 2:
                    binding = !binding;
                    break;
        		}
        	}
    	} else {
    		switch (button) {
            case 0:
                module.toggle();
                break;
            case 2:
                binding = !binding;
                break;
    		}
    	}
    }

    @Override
    public void onKeyPress(int keyCode) {
        if (binding) {
            GuiScreen.escapeKeyInUse = true;
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
        this.height = Interpolator.linear(this.height, height + 11, 2f/2);
        return this.height;
    }

    @Override
    public int getHeight() {
        int height = 10;
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
        this.height = Interpolator.linear(this.height, height - 5, 2f/2);
        return super.getHeight();
    }
}
