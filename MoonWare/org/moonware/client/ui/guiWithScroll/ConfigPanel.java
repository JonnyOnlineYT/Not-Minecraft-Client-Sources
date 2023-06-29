package org.moonware.client.ui.guiWithScroll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.MWFont;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.StencilUtil;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.ui.guiWithScroll.component.AnimationState;
import org.moonware.client.ui.guiWithScroll.component.Component;
import org.moonware.client.ui.guiWithScroll.component.DraggablePanel;
import org.moonware.client.ui.guiWithScroll.component.ExpandableComponent;

import java.awt.*;

public final class ConfigPanel extends DraggablePanel {
    Minecraft mc = Minecraft.getMinecraft();
    public static final int PANEL_WIDTH = 100;
    public static final int X_ITEM_OFFSET = 1;
    public double scissorBoxHeight;

    public static final int ITEM_HEIGHT = 22;
    public static final int HEADER_HEIGHT = 17;
    public Type type;
    public AnimationState state;
    private int prevX;
    private int prevY;
    private boolean dragging;
    static boolean inPanel;
    int y2;
    public static GuiTextField search;
    public ConfigPanel(Type category, int x, int y) {
        super(null, category.getName(), x, y, PANEL_WIDTH, HEADER_HEIGHT);
        search = new GuiTextField(228, Minecraft.font, x - 1, y + 155, 85, 13);
        int moduleY = HEADER_HEIGHT;

        state = AnimationState.STATIC;
//        for (Config module : MoonWare.configManager.getContents()) {
//            //components.add(new ModuleComponent(this, module, X_ITEM_OFFSET, moduleY, PANEL_WIDTH - (X_ITEM_OFFSET * 2), ITEM_HEIGHT));
//
//            moduleY += ITEM_HEIGHT;
//        }
//        for (Feature module : features) {
//            components2.add(new ModuleDescComponent(this, module, X_ITEM_OFFSET, moduleY, PANEL_WIDTH - (X_ITEM_OFFSET * 2), ITEM_HEIGHT));
//        }
        type = category;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getY2() {
        return y2;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        components.sort(new SorterHelper());
        components2.sort(new SorterHelper());
        TimerHelper timerHelper = new TimerHelper();
        int color = 0;
        int x = getX();
        int y = getY();
        setExpanded(true);
        if (!true) {
            if (dragging) {
                setX(mouseX - prevX);
                setY(mouseY - prevY);
            }
        }
        if (Keyboard.isKeyDown(205)) {
            setX(getX() + 4);
        }
        if (Keyboard.isKeyDown(203)) {
            setX(getX() - 4);
        }
        int width = getWidth();
        int height = getHeight();
        int heightWithExpand = getHeightWithExpand();
        String mode = "Rockstar New";
        GlStateManager.pushMatrix();
        if (mode.equalsIgnoreCase("Rockstar New")) {
            RenderUtils2.drawBlur(4,() ->  RoundedUtil.drawRound((x - 1 + 1 - 1), y2 + 30 + 1 - 3, (width + 1 - 5 + 3) + (1 + 1) + 2, 1 - 3 + 18 + (y2 + 260 + 4) - 48 - 80, 7, new Color(255, 255, 255, 215)));
            RoundedUtil.drawRound((x - 1 + 1 - 1), y2 + 30 + 1 - 3, (width + 1 - 5 + 3) + (1 + 1) + 2, 1 - 3 + 18 + (y2 + 260 + 4) - 48 - 80, 7, new Color(255, 255, 255, 215));
            StencilUtil.uninitStencilBuffer();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderHelper2.scissorRect(x - 1 + 1 - 80, y2 + 30 + 1 - 3 - 8, Minecraft.width, y2 + 30 + 1 - 3 + 20 - 1);
            RenderHelper2.drawRainbowRoundGui((x - 1 + 1 - 1), y2 + 30 + 1 - 3.5f, (width + 1 - 5 + 3) + (1 + 1) + 2, 1 - 3 + 18 + (y2 + 260 + 4) - 52, 7, true, true, true, true, 2, 4);
            GlStateManager.disable(GL11.GL_SCISSOR_TEST);

            MWFont.MONTSERRAT_BOLD.get(24).drawCenter(getCName(), (x + width / 2), y2 + 30 + HEADER_HEIGHT / 2F - 5.5f + 1, Color.WHITE.getRGB());
        }
        GlStateManager.popMatrix();
        boolean b = mouseY > 47 && mouseY < 260;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderHelper2.scissorRect(0, 47, scaledResolution.getScaledWidth(), 150);
        //RoundedUtil.drawRound((x - 1 + 1 - 1), y2 + 30 + 1 - 3, (width + 1 - 5 + 3) + (1 + 1) + 2, 1 - 3 + 18 + (y2 + 260 + 4) - 48 - 80, 7, new Color(255, 5, 5, 215));
        //RoundedUtil.drawRound((x - 1), y2 + 28, (width - 1) + (1 + 1) + 2, (y2 + 152), 7, new Color(255, 0, 0, 215));
        GlStateManager.disable(GL11.GL_SCISSOR_TEST);
        search.drawCelestialTextBox(MWFont.MONTSERRAT_BOLD.get(16), new Color(231,231,231));
        if (search.getText().isEmpty() && !search.isFocused()) {
            Minecraft.font.drawStringWithOutline("...", width - 97, height + 65, PaletteHelper.getColor(200));
        }
        inPanel = mouseY < y2 + 260 || mouseY > y2 + 30;

        super.drawComponent(scaledResolution, mouseX, mouseY);
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        if (button == 0 && !dragging) {
            dragging = true;
            prevX = mouseX - getX();
            prevY = mouseY - getY();
        }
    }


    @Override
    public void onMouseRelease(int button) {
        super.onMouseRelease(button);
        dragging = false;
    }

    @Override
    public boolean canExpand() {
        return true;
    }

    @Override
    public int getHeightWithExpand() {
        int height = getHeight();
        if (isExpanded()) {
            for (Component component : components) {
                int cHeight = component.getHeight();
                if (component instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) component;
                    if (expandableComponent.isExpanded())
                        cHeight = expandableComponent.getHeightWithExpand() + 5;
                }
                height += cHeight;
            }
        }
        return height;
    }
}
