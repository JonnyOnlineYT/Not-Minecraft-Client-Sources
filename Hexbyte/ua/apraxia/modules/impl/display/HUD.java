package ua.apraxia.modules.impl.display;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.text.TextFormatting;
import ua.apraxia.Hexbyte;
import ua.apraxia.draggable.component.impl.WaterMark;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.render.EventRender2D;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.ColorSetting;
import ua.apraxia.utility.Utility;
import ua.apraxia.utility.font.Fonts;
import ua.apraxia.utility.math.MathUtility;
import ua.apraxia.utility.other.MoveUtility;
import ua.apraxia.utility.render.ColorUtilityAlt;
import ua.apraxia.utility.render.RenderUtility;
import ua.apraxia.utility.render.RoundedUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

import java.awt.*;


public class HUD extends Module {
    public static BooleanSetting waterMark = new BooleanSetting("Water Mark", true);
    public static BooleanSetting healthHUD = new BooleanSetting("Self Health", false);
    public static BooleanSetting armor = new BooleanSetting("Armor HUD", true);
    public static BooleanSetting info = new BooleanSetting("User information", true);
    public static ColorSetting hudColor = new ColorSetting("HUD Color",  new Color(3, 167, 243, 255).getRGB());
    public float animWidth;
    public float scale = 2;
   float anim;

    public HUD() {
        super("HUD", Categories.Display);
        addSetting(waterMark, info,   armor, healthHUD, hudColor);
    }

    @EventTarget
    public void onRender2D(final EventRender2D event) {
            WaterMark dwm = (WaterMark) Hexbyte.getInstance().draggable.getDraggableComponentByClass(WaterMark.class);
            dwm.setWidth(150);
            dwm.setHeight(17);
            String text = "" + Minecraft.getDebugFPS() + " fps" + "    " + "apraxia1337" +  "    " + "beta";
            RoundedUtility.drawRound(dwm.getX() + 3.0F, dwm.getY() + 4.0F, (float) (Fonts.nl14.getStringWidth(text) + 56), 12, 2, new Color(9, 9, 9, 255));
            Fonts.nl14.drawString(text, dwm.getX() + 55.0F, dwm.getY() + 8.5f, -1);
            Fonts.nlbolt18.drawString("HEXBYTE", dwm.getX() + 5.7F, dwm.getY() + 8, new Color(hudColor.color).getRGB());
            Fonts.nlbolt18.drawString("HEXBYTE", dwm.getX() + 6.0F, dwm.getY() + 8.4f, -1);
        if (armor.value) {
            ScaledResolution sr = new ScaledResolution(mc);
            int count = 0;
            GlStateManager.pushMatrix();
            for (ItemStack is : Utility.mc.player.inventory.armorInventory) {
                ++count;
                int x = 90 + (9 - count) * 20 + 250;
                GlStateManager.enableDepth();
                Utility.mc.getRenderItem().zLevel = 200.0f;
                Utility.mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, 420);
                Utility.mc.getRenderItem().renderItemOverlayIntoGUI(Utility.mc.fontRenderer, is, x, 420, "");
                Utility.mc.getRenderItem().zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
        }
        if (healthHUD.value) {
            ScaledResolution sr = new ScaledResolution(mc);
            float health = mc.player.getHealth();
            Fonts.sfbolt14.drawCenteredStringWithOutline(String.format("%.1f", health), sr.getScaledWidth() / 2 + 10,  sr.getScaledHeight() / 2 + 10, ColorUtilityAlt.getHealthColor(mc.player.getHealth(), 20));
            Fonts.sfbolt14.drawCenteredStringWithOutline("HP: ", sr.getScaledWidth() / 2 - 5, sr.getScaledHeight() / 2 +10, -1);
        }
        if (info.value) {
            ScaledResolution sr = new ScaledResolution(mc);
            String speed = String.format("%.2f ", MoveUtility.getSpeed() * 16.0F * mc.timer.timerSpeed);
            String fps = "" + Minecraft.getDebugFPS();
            String x = "" + Math.round(mc.player.posX);
            String y = "" + Math.round(mc.player.posY);
            String z = "" + Math.round(mc.player.posZ);
            String coords1 = "XYZ: ";
            String coords = "          " + mc.player.getPosition().getX() + ", " + mc.player.getPosition().getY() + ", " + mc.player.getPosition().getZ();
            int hach = mc.currentScreen instanceof GuiChat ? sr.getScaledHeight() - 7 - 15 : sr.getScaledHeight() - 9;
            Fonts.sfbolt14.drawStringWithOutline(coords, 5, (float)hach,(new Color(69, 255, 84, 255)).getRGB());
            Fonts.sfbolt14.drawStringWithOutline(coords1, 5, (float)hach,(new Color(255, 255, 255, 255)).getRGB());
            Fonts.sfbolt14.drawStringWithOutline("FPS:", 5.0F, (float)(hach - 9), -1);
            Fonts.sfbolt14.drawStringWithOutline(fps, 23.0F, (float)(hach - 9),  (new Color(182, 182, 182, 255)).getRGB());
            Fonts.sfbolt14.drawStringWithShadow("UID: ", (float)(sr.getScaledWidth() - 25), (float)hach, -1);
            Fonts.sfbolt14.drawStringWithShadow(" 1", (float)(sr.getScaledWidth() - 10), (float)hach, (new Color(182, 182, 182, 255)).getRGB());
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        anim = 0;
    }
}

