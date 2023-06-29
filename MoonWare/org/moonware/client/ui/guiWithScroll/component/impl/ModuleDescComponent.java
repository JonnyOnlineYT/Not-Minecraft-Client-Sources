package org.moonware.client.ui.guiWithScroll.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.moonware.client.feature.Feature;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.ui.guiWithScroll.ClickGuiScreen;
import org.moonware.client.ui.guiWithScroll.Panel;
import org.moonware.client.ui.guiWithScroll.component.AnimationState;
import org.moonware.client.ui.guiWithScroll.component.Component;
import org.moonware.client.ui.guiWithScroll.component.ExpandableComponent;
import org.moonware.client.utils.Interpolator;

public final class ModuleDescComponent extends ExpandableComponent {
    Minecraft mc = Minecraft.getMinecraft();
    private final Feature module;
    private final AnimationState state;
    private boolean binding;
    private int prevY;
    private int buttonTop;
    private int descTimerAnim;
    private int prevX;
    private int heightAnimation;
    private int circleAnimaion;

    public ModuleDescComponent(Component parent, Feature module, int x, int y, int width, int height) {
        super(parent, module.getLabel(), x, y, width, height);
        this.module = module;
        state = AnimationState.STATIC;
        int propertyX = Panel.X_ITEM_OFFSET;
    }
    private int height = getHeight();
    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        float x = getX();
        boolean a = true;
        float y = a ? ((getY() - 2)) : (-1000);
        int width = getWidth();
        int height = getHeight();
        this.descTimerAnim = (int) AnimationHelper.calculateCompensation(isHovered(mouseX,mouseY) ? 255 : 0, this.descTimerAnim, 6, 8);
//        RoundedUtil.drawRound(x + getWidth() + 15, y + getHeight() / 2 -MWFont.MONTSERRAT_BOLD.get(18).getHeight() + 5,  MWFont.MONTSERRAT_BOLD.get(20).getWidth(module.getDesc()) + 5,MWFont.MONTSERRAT_BOLD.get(18).getHeight() + 5,1.5F, new Color(38,38,38,this.descTimerAnim));
//        MWFont.MONTSERRAT_BOLD.get(20).drawShadow(module.getDesc(), x + getWidth() + 17, y + getHeight() / 2, new Color(255,255,255,this.descTimerAnim).getRGB());
//
    }

    @Override
    public boolean canExpand() {
        return !components.isEmpty();
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
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
        this.height = Interpolator.linear(this.height, height + 5, 2f/10);
        return this.height;
    }

}

