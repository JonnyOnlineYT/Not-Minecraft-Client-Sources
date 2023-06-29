package Celestial.ui.celestun4ik;

import Celestial.module.ModuleCategory;
import Celestial.module.impl.Render.ClickGUI;
import Celestial.utils.math.animations.Animation;
import Celestial.utils.math.animations.impl.DecelerateAnimation;
import Celestial.utils.render.RenderUtils;
import Celestial.ui.celestun4ik.component.Component;
import Celestial.ui.celestun4ik.component.ExpandableComponent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class guiscreencomponent extends GuiScreen {
    public static boolean escapeKeyInUse;
    public float scale = 2;
    public float scrollAnimation = 0;

    public List<panelcomponent> components = new ArrayList<>();
    public screenutil screenHelper;
    public boolean exit = false;
    public ModuleCategory type;
    private Component selectedPanel;
    private Animation initAnimation;

    public guiscreencomponent() {
        int x = 15;
        int y = 10;
        for (ModuleCategory type : ModuleCategory.values()) {
            this.type = type;
            this.components.add(new panelcomponent(type, x, y));
            selectedPanel = new panelcomponent(type, x, y);
            x += width + 110;
        }
        this.screenHelper = new screenutil(0, 0);
    }

    @Override
    public void initGui() {

        ScaledResolution sr = new ScaledResolution(mc);
        initAnimation = new DecelerateAnimation(600, 1);
        this.screenHelper = new screenutil(0, 0);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);

        if (ClickGUI.blur.getCurrentValue()) {
            RenderUtils.drawBlur(15, () -> {
                RenderUtils.drawSmoothRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(20, 20, 20, 190).getRGB());

            });
        }

        for (panelcomponent panel : components) {
            panel.drawComponent(sr, mouseX, (int) (mouseY));
        }
        updateMouseWheel(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    public void updateMouseWheel(int mouseX, int mouseY) {
        int scrollWheel = Mouse.getDWheel();
        for (Component panel : components) {
            if (mouseX < panel.getX() + 100 && mouseX > panel.getX() && mouseY < 240 && mouseY > 24) {
                if (scrollWheel > 0 && panel.getY() < 5) {
                    panel.setY(panel.getY() + 15);
                }
                if (scrollWheel < 0) {
                    panel.setY(panel.getY() - 15);
                }
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.selectedPanel.onKeyPress(keyCode);
        if (!escapeKeyInUse)
            super.keyTyped(typedChar, keyCode);
        escapeKeyInUse = false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
       for (Component component : components) {
            selectedPanel = component;
            int x = component.getX();
            int y = component.getY();
            int cHeight = component.getHeight();
            if (component instanceof ExpandableComponent) {
                ExpandableComponent expandableComponent = (ExpandableComponent) component;
                if (expandableComponent.isExpanded())
                    cHeight = expandableComponent.getHeightWithExpand();
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() && mouseY < y + cHeight && mouseY < 240 && mouseY > 24) {
                component.onMouseClick(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        selectedPanel.onMouseRelease(state);
    }

    @Override
    public void onGuiClosed() {
        this.screenHelper = new screenutil(0, 0);
        mc.entityRenderer.theShaderGroup = null;
        super.onGuiClosed();
    }
}