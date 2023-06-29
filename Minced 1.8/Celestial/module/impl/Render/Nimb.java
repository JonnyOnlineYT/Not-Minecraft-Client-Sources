package Celestial.module.impl.Render;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.render.EventRender3D;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.render.ColorUtils;
import Celestial.utils.render.RenderUtils;
import Celestial.ui.settings.impl.ColorSetting;
import Celestial.ui.settings.impl.ListSetting;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Nimb extends Module
{
    final ListSetting colorMode;
    final ColorSetting onecolor;
    final ColorSetting twocolor;
    final NumberSetting saturation;

    public Nimb() {
        super("Nimb", "\u0414\u043e\u0431\u0430\u0432\u043b\u044f\u0435\u0442 \u0432\u0430\u043c \u043d\u0438\u043c\u0431", ModuleCategory.Render);
        this.colorMode = new ListSetting("Nimb Color", "Astolfo", () -> true, new String[] { "Astolfo", "Pulse", "Custom", "Static" });
        this.onecolor = new ColorSetting("One Color", new Color(255, 255, 255).getRGB(), () -> this.colorMode.currentMode.equalsIgnoreCase("Static") || this.colorMode.currentMode.equalsIgnoreCase("Custom"));
        this.twocolor = new ColorSetting("Two Color", new Color(255, 255, 255).getRGB(), () -> this.colorMode.currentMode.equalsIgnoreCase("Custom"));
        this.saturation = new NumberSetting("Saturation", 0.7f, 0.1f, 1.0f, 0.1f, () -> this.colorMode.currentMode.equalsIgnoreCase("Astolfo"));
        this.addSettings(this.colorMode, this.onecolor, this.twocolor, this.saturation);
    }

    @EventTarget
    public void asf(final EventRender3D event) {
        double height = 0.0;
        final ItemStack stack = Nimb.mc.player.getEquipmentInSlot(4);
        final double d = (stack.getItem() instanceof ItemArmor) ? (Nimb.mc.player.isSneaking() ? -0.1 : 0.12) : (height = (Nimb.mc.player.isSneaking() ? -0.22 : 0.0));
        if (Nimb.mc.gameSettings.thirdPersonView == 1 || Nimb.mc.gameSettings.thirdPersonView == 2) {
            GlStateManager.pushMatrix();
            GL11.glBlendFunc(770, 771);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            RenderUtils.enableSmoothLine(2.5f);
            GL11.glShadeModel(7425);
            GL11.glDisable(2884);
            GL11.glEnable(3042);
            GL11.glEnable(2929);
            GL11.glTranslatef(0.0f, (float)(Nimb.mc.player.height + height), 0.0f);
            GL11.glRotatef(-Nimb.mc.player.rotationYaw, 0.0f, 1.0f, 0.0f);
            Color color2 = Color.WHITE;
            final Color firstcolor2 = new Color(this.onecolor.getColorValue());
            final String currentMode = this.colorMode.currentMode;
            switch (currentMode) {
                case "Client": {
                    color2 = Color.YELLOW;
                    break;
                }
                case "Astolfo": {
                    color2 = RenderUtils.astolfoColors45(5.0f, 5.0f, this.saturation.getNumberValue(), 10.0f);
                    break;
                }
                case "Pulse": {
                    color2 = ColorUtils.TwoColoreffect(new Color(0, 255, 208), new Color(0, 45, 255), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 0.0);
                    break;
                }
                case "Custom": {
                    color2 = ColorUtils.TwoColoreffect(new Color(this.onecolor.getColorValue()), new Color(this.twocolor.getColorValue()), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 0.0);
                    break;
                }
                case "Static": {
                    color2 = firstcolor2;
                    break;
                }
            }
            GL11.glBegin(6);
            RenderUtils.glColor(color2, 255);
            GL11.glVertex3d(0.0, 0.3, 0.0);
            for (float i = 0.0f; i < 360.5; ++i) {
                Color color3 = Color.WHITE;
                final Color firstcolor3 = new Color(this.onecolor.getColorValue());
                final String currentMode2 = this.colorMode.currentMode;
                switch (currentMode2) {
                    case "Client": {
                        color3 = Color.YELLOW;
                        break;
                    }
                    case "Astolfo": {
                        color3 = RenderUtils.astolfoColors45(i - i + 1.0f, i, this.saturation.getNumberValue(), 10.0f);
                        break;
                    }
                    case "Pulse": {
                        color3 = ColorUtils.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0f * (i / 16.0f) / 60.0f);
                        break;
                    }
                    case "Custom": {
                        color3 = ColorUtils.TwoColoreffect(new Color(this.onecolor.getColorValue()), new Color(this.twocolor.getColorValue()), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 3.0f * (i / 16.0f) / 60.0f);
                        break;
                    }
                    case "Static": {
                        color3 = firstcolor3;
                        break;
                    }
                }
                RenderUtils.glColor(color3, 255);
            }
            GL11.glVertex3d(0.0, 0.3, 0.0);
            GL11.glEnd();
            GL11.glBegin(2);
            for (float i = 0.0f; i < 360.5; ++i) {
                Color color3 = Color.WHITE;
                final Color firstcolor3 = new Color(this.onecolor.getColorValue());
                final String currentMode3 = this.colorMode.currentMode;
                switch (currentMode3) {
                    case "Client": {
                        color3 = Color.YELLOW;
                        break;
                    }
                    case "Astolfo": {
                        color3 = RenderUtils.astolfoColors45(i - i + 1.0f, i, this.saturation.getNumberValue(), 10.0f);
                        break;
                    }
                    case "Pulse": {
                        color3 = ColorUtils.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0f * (i / 16.0f) / 60.0f);
                        break;
                    }
                    case "Custom": {
                        color3 = ColorUtils.TwoColoreffect(new Color(this.onecolor.getColorValue()), new Color(this.twocolor.getColorValue()), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 3.0f * (i / 16.0f) / 60.0f);
                        break;
                    }
                    case "Static": {
                        color3 = firstcolor3;
                        break;
                    }
                }
                RenderUtils.glColor(color3, 255);
                GL11.glVertex3d(Math.cos(i * 3.141592653589793 / 180.0) * 0.45, 0.10, Math.sin(i * 3.141592653589793 / 180.0) * 0.45);
                GL11.glVertex3d(Math.cos(i * 3.141592653589793 / 180.0) * 0.45, 0.10, Math.sin(i * 3.141592653589793 / 180.0) * 0.45);
                GL11.glVertex3d(Math.cos(i * 3.141592653589793 / 180.0) * 0.45, 0.10, Math.sin(i * 3.141592653589793 / 180.0) * 0.45);
            }
            GL11.glEnd();
            GlStateManager.enableAlpha();
            RenderUtils.disableSmoothLine();
            GL11.glShadeModel(7424);
            GL11.glEnable(2884);
            GL11.glDisable(3042);
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.resetColor();
            GlStateManager.popMatrix();
        }
    }
}