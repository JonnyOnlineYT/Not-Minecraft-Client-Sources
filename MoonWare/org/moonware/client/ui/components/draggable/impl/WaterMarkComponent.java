package org.moonware.client.ui.components.draggable.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Formatting;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.feature.impl.hud.WaterMark;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.GlowUtil;
import org.moonware.client.helpers.render.RenderHelper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.ui.components.draggable.HudComponent;
import org.moonware.client.utils.MWFont;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WaterMarkComponent extends HudComponent {
    public WaterMarkComponent() {
        super(4, 8, 135, 15);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        setVisible(HUD.timerhud.get());
        super.draw(mouseX, mouseY, partialTick);
        if (Minecraft.getConnection() == null || Minecraft.player == null || Minecraft.gameSettings.showDebugInfo) return;
        String mode = WaterMark.logoMode.getCurrentMode();
        if (mode.equalsIgnoreCase("Up-Gradient")) {
            int x = this.x - 4;
            int y = this.y - 8;
            String serverr;
            if (Minecraft.isSingleplayer()) {
                serverr = "localhost";
            } else {
                serverr = Minecraft.getServerData().ip.toLowerCase();
            }
            boolean set0 = WaterMark.clientname.getBoolValue();
            boolean set1 = WaterMark.username.getBoolValue();
            boolean set2 = WaterMark.servers.getBoolValue();
            boolean set3 = WaterMark.fps.getBoolValue();
            boolean set4 = WaterMark.time.getBoolValue();
            boolean set5 = WaterMark.ping.getBoolValue();
            String time = (new SimpleDateFormat("HH:mm:ss")).format(Calendar.getInstance().getTime());
            String ping =  5 + "ms";
            String text = (set0 ? "MoonWare" : "") + Formatting.RESET + (set1 ? ("§f | §f" + MoonWare.LICENSE) : "") + (set2 ? " §f| §f" + serverr : "") + (set3 ? (" §f| §f" + (Minecraft.getDebugFPS())) + "fps§f | §f" : "") + (set4 ? ("" + time) : "") + (set5 ? " §f| §f" + ping : "");
            DrawHelper.enableGL2D();
            //DrawHelper.startSmooth();
            DrawHelper.enableSmoothLine(2);
            if (WaterMark.backgroundGradient.get()) {
                Color colorrightdown = Color.cyan;
                Color colorrightup = Color.BLUE;
                Color colorleftdown = Color.DARK_GRAY;
                Color colorleftup = Color.cyan;

                switch (WaterMark.backgroundGradientColorList.getCurrentMode()) {
                    case "Rainbow --> Astolfo":
                        colorleftup = new Color(PaletteHelper.rainbow(WaterMark.colorTime.getCurrentIntValue() * 100, WaterMark.rainbowSaturation.getCurrentIntValue(), WaterMark.rainbowBright.getCurrentIntValue()).getRGB());
                        colorleftdown = new Color(PaletteHelper.rainbow(WaterMark.colorTime.getCurrentIntValue() * 100, WaterMark.rainbowSaturation.getCurrentIntValue(), WaterMark.rainbowBright.getCurrentIntValue()).getRGB());
                        colorrightup = new Color(PaletteHelper.astolfoarray2(false, y * 4).getRGB());
                        colorrightdown = new Color(PaletteHelper.astolfoarray2(false, y * 4).getRGB());
                        break;
                    case "Astolfo --> Rainbow":
                        colorleftup = new Color(PaletteHelper.astolfoarray2(false, y * 4).getRGB());
                        colorleftdown = new Color(PaletteHelper.astolfoarray2(false, y * 4).getRGB());
                        colorrightup = new Color(PaletteHelper.rainbow(WaterMark.colorTime.getCurrentIntValue() * 100, WaterMark.rainbowSaturation.getCurrentIntValue(), WaterMark.rainbowBright.getCurrentIntValue()).getRGB());
                        colorrightdown = new Color(PaletteHelper.rainbow(WaterMark.colorTime.getCurrentIntValue() * 100, WaterMark.rainbowSaturation.getCurrentIntValue(), WaterMark.rainbowBright.getCurrentIntValue()).getRGB());
                        break;
                    default:
                        colorrightup = new Color(WaterMark.right_up_color.getColor());
                        colorrightdown = new Color(WaterMark.right_down_color.getColor());
                        colorleftup = new Color(WaterMark.left_up_color.getColor());
                        colorleftdown = new Color(WaterMark.left_down_color.getColor());
                }
                //GlowUtil.drawBlurredGlow(x + 4.5f, y + 5.5f, x + 4.5f + 5.5f +Minecraft.getMinecraft().motBold.getStringWidth(text) + 30 - 17, y + 5.5F + 21f, new Color(colorleftdown.getRed()).getRGB());
                Color colorkaA = new Color(ColorUtil.interpolateColor(colorrightdown, colorrightup, 10));
                Color colorkaA2 = new Color(ColorUtil.interpolateColor(colorleftdown, colorleftup, 30));
                Color colorkaA3 = new Color(ColorUtil.interpolateColorsBackAndForth(4, 60, colorkaA2, colorleftup, false).getRGB());
                Color colorka = ColorUtil.interpolateColorsBackAndForth(11, 10, colorkaA2, colorkaA, false);
                Color colorka2 = ColorUtil.interpolateColorsBackAndForth(7, 30, colorkaA, colorkaA2, false);
                Color colorka3 = ColorUtil.interpolateColorsBackAndForth(9, 60, colorka2, colorkaA3, false);
                Color colorka4 = ColorUtil.interpolateColorsBackAndForth(11, 90, colorka3, colorka, false);
                Color shadowColor = mixColors(colorka, colorka2, 1);
                //RoundedUtil.drawGradientRound(x + 4.5f, y + 5.5f, 5.5f + Minecraft.getMinecraft().motBold.getStringWidth(text) + 30 - 17, 21f, 6, colorleftdown, colorleftup, colorrightdown, colorrightup);
                double yg = y + 10.5f;
                double heightT = 16;
                if (!HUD.testAstolfoColors.get()) {
                    RenderHelper2.renderBlurredShadow(shadowColor, x + 4.5f, yg, 5.5f + MWFont.GREYCLIFFCF_MEDIUM.get(18).getWidth(text) + 30 - 17, heightT, 16);
                    RoundedUtil.drawGradientRound(x + 4.5f, (float) yg, 5.5f + MWFont.GREYCLIFFCF_MEDIUM.get(18).getWidth(text) + 30 - 17, (float) heightT, 6, colorka, colorka2, colorka3, colorka4);
                } else {
                    if (HUD.colored.get()) {
                        RenderHelper2.drawRainbowRound(x + 4.5f, yg, 5.5F + MWFont.GREYCLIFFCF_MEDIUM.get(18).getWidth(text) + 30 - 17, heightT, 6, true, true, WaterMark.outline.get(), true, 2, 4);
                    }else {
                        //RoundedUtil.drawRound(x + 4.5f, (float) yg, 5.5F + MWFont.GREYCLIFFCF_MEDIUM.get(18).getWidth(text) + 30 - 17, (float) heightT, 1.4f,new Color(31,31,31,HUD.nurikAlpha.getCurrentIntValue()));
                        RoundedUtil.drawRound(x + 4.5f, (float) yg, 5.5F + MWFont.GREYCLIFFCF_MEDIUM.get(18).getWidth(text) + 30 - 32, (float) heightT, 0.1f,new Color(31,31,31,HUD.nurikAlpha.getCurrentIntValue()));
                        GlowUtil.drawBlurredShadow(x + 4.5f, (float) yg, 5.5F + MWFont.GREYCLIFFCF_MEDIUM.get(18).getWidth(text) + 30 -32, (float) heightT, 14,new Color(31,31,31, MathHelper.clamp(HUD.nurikAlpha.getCurrentIntValue(),1,255) ),2);

                        RenderHelper2.drawRainbowRound(x + 4.5f, (float) yg, 5.5F + MWFont.GREYCLIFFCF_MEDIUM.get(18).getWidth(text) + 30 - 32, 0.1f, 0.01f, true, false, true, true, 2, 4);
                    }

                }
            }
            DrawHelper.disableSmoothLine();
            DrawHelper.disableGL2D();
            GlStateManager.enableBlend();
            if (HUD.colored.get()) {
                MWFont.GREYCLIFFCF_MEDIUM.get(18).draw(text, x + 30 - 17, y + 15.5f, -1);
            }else{
                MWFont.GREYCLIFFCF_MEDIUM.get(18).draw(text, x + 30 - 25, y + 15.5f, -1);
            }
            GlStateManager.disableBlend();
            this.width = (int) (5.5f + MWFont.GREYCLIFFCF_MEDIUM.get(18).getWidth(text) + 30 - 17) + 10;
        } else if (mode.equalsIgnoreCase("DefMoon")) {
            String serverr;
            if (Minecraft.isSingleplayer()) {
                serverr = "localhost";
            } else {
                serverr = Minecraft.getServerData().ip.toLowerCase();
            }
            boolean set0 = WaterMark.clientname.getBoolValue();
            boolean set1 = WaterMark.username.getBoolValue();
            boolean set2 = WaterMark.servers.getBoolValue();
            boolean set3 = WaterMark.fps.getBoolValue();
            boolean set4 = WaterMark.time.getBoolValue();
            boolean set5 = WaterMark.ping.getBoolValue();
            String time = (new SimpleDateFormat("HH:mm:ss")).format(Calendar.getInstance().getTime());
            NetworkPlayerInfo info = Minecraft.getConnection().getPlayerInfo(Minecraft.player.getUniqueID());
            String ping = (info != null ? info.getResponseTime() : 0) + "ms";
            String text = (set0 ? Formatting.BOLD + "moonware" : "") + Formatting.RESET + (set1 ? ("§8 | §f" + Minecraft.getSession().getUsername()) : "") + (set2 ? "§8 | §f" + serverr : "") + (set3 ? (" §8 | §f" + (Minecraft.getDebugFPS())) + "§f fps " : "") + (set4 ? ("§8 | §f" + time) : "") + (set5 ? "§8 | §f" + ping : "");

            RenderHelper2.renderBlurredShadow(new Color(31, 31, 31, 180), x + 4.5f, y + 5.5f, 5.5f + MWFont.SF_UI_DISPLAY_REGULAR.get(16).getWidth(text) + 30, 21f, 6);
            //renderShadow( x + 4.5f, y + 5.5f, 5.5f + Minecraft.getMinecraft().circleregularSmall.getStringWidth(text) + 30, 21f,new Color(31, 31, 31, 180).getRGB(), ArrayList.glowRadius.getCurrentIntValue());

            //RectHelper.drawRectWithGlow(x + 4.5f, y + 7.5f, x + 5.5f + Minecraft.getMinecraft().circleregularSmall.getStringWidth(text) + 30, y + 23f, 5, 15, new Color(31, 31, 31, 180));
            GlStateManager.enableBlend();
            //DrawHelper.drawGradientRect(5, 185, 95, 198, new Color(22, 22, 22, 205).getRGB(), new Color(21, 21, 21, 0).getRGB());
            //RectHelper.drawSmoothRect1(4, 5, Minecraft.getMinecraft().circleregularSmall.getStringWidth(text) + 32, 26, new Color(1, 1, 1, 179).getRGB(), 7, 4);
            for (float l = 0; l < 270; l += 1f) {
                //DrawHelper.drawCircle(156, 124, 1445, 5,5f, 1, isVisible(), DrawHelper.astolfoColors5(l - l + 15, l, 0.5f, 10f));
            }
            MWFont.SF_UI_DISPLAY_REGULAR.get(16).draw(text, x + 30, y + 13.5f, -1);
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableBlend();
            RenderHelper.drawImage(new Namespaced("moonware/icon256.png"), x + 8.5f, y + 3.2f, 20, 25, Color.WHITE);
            //код
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            this.width = (int) (5.5f + MWFont.SF_UI_DISPLAY_REGULAR.get(16).getWidth(text) + 30 - 17) + 10;
        }
        this.height = 27;
    }

    public Color mixColors(Color color1, Color color2, double percent) {
        double inverse_percent = 1.0 - percent;
        int redPart = (int) (color1.getRed() * percent + color2.getRed() * inverse_percent);
        int greenPart = (int) (color1.getGreen() * percent + color2.getGreen() * inverse_percent);
        int bluePart = (int) (color1.getBlue() * percent + color2.getBlue() * inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }

    public Color mixColors(Color color1, Color color2, Color color3, Color color4, int percent) {
        double inverse_percent = 1.0 - percent;
        int redPart = (int) (color1.getRed() * percent + color2.getRed() + color3.getRed() * percent * inverse_percent);
        int greenPart = (int) (color1.getGreen() * percent + color2.getGreen() + color3.getGreen() * percent * inverse_percent);
        int bluePart = (int) (color1.getBlue() * percent + color2.getBlue() + color3.getBlue() * percent * inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }
//    private final List<EntityLivingBase> entities = new java.util.ArrayList<>();
//    public void getEntities() {
//        entities.clear();
//        for (Entity entity : Minecraft.world.loadedEntityList) {
//            if (entity instanceof EntityLivingBase) {
//                if (entity instanceof EntityPlayer && entity != null && !entity.isInvisible()) {
//                    entities.add((EntityLivingBase) entity);
//                }
//            }
//        }
//    }
//    @Override
//    public void draw() {
//        if (!Minecraft.gameSettings.showDebugInfo) {
//            String server;
//            String mode = WaterMark.logoMode.getCurrentMode();
//            Color color = Color.BLACK;
//            switch (WaterMark.logoColor.currentMode) {
//                case "Gradient": {
//                    for (int i = x; i < x + getWidth(); i++) {
//                        color = new Color(PaletteHelper.fadeColor(WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue(), i));
//                    }
//                    break;
//                }
//                case "Client": {
//                    color = ClientHelper.getClientColor();
//                    break;
//                }
//                case "Rainbow": {
//                    color = PaletteHelper.rainbow(300, 1, 1);
//                    break;
//                }
//                case "Default": {
//                    color = WaterMark.logoMode.currentMode.equals("OneTap v2") ? new Color(161, 0, 255) : Color.RED;
//                }
//            }
//            if (mode.equalsIgnoreCase("Heaven")) {
//                String username = Minecraft.getSession().getUsername();
//                String playerPing = getPing(Minecraft.player) + " ms";
//                String serverr = Helper.mc.getServerData() == null ? "singleplayer"
//                        : Helper.mc.getServerData().serverIP.toLowerCase();
//                String fps = Minecraft.getDebugFPS() + " fps";
//
//                //ArrayListMod list = Wrapper.getModule().get("Arraylist");
//
//                CustomFont font = MWFont.MONTSERRAT_BOLD.get(12);
//
//                int x = 5;
//                int y = 5;
//
//                int height = 12;
//                float offsetX = 3;
//
//                float fullWidth = 0;
//                String[] arrayInfo = { "MoonWare", MoonWare.BUILD, username, playerPing, fps, serverr};
//                boolean def = mode.equalsIgnoreCase("Heaven");
//                //RenderHelper2.drawBlurredShadow(x, y - 1, (int) fullWidth - 2, height + 2, 8, new Color(46, 46, 46, 250));
//                RoundedUtil.drawGradientHorizontal(x - 1, y - 1, fullWidth, height + 2, 4,
//                        def ? ColorUtil.rainbow((int) ArrayList.time.getValue(), 300, .6f, 1, 1)
//                                : ColorUtil.rainbow((int) ArrayList.time.getValue(), 0, .6f, 1, 1),
//                        ColorUtil.rainbow((int) ArrayList.time.getValue(), 300, .6f, 1, 1));
//
////                if (def)
////                    RoundedUtil.drawRound(x, y, fullWidth - 2, height, 3, new Color(54, 54, 54, 255));
////
//                for (int i = 0; i < arrayInfo.length; i++) {
//                    String info = arrayInfo[i];
//
//                     font.drawCenterShadow(info, x + offsetX, y, -1);
////
//                    if (i != arrayInfo.length - 1)
//                        RenderHelper2.drawRectWH(x + 2 + offsetX + (4 / 2) + font.getWidth(info), y + 4, 0.5, height - 8,
//                                Colors.DIVIDER_COLOR.getRGB());
//
//                    offsetX += font.getWidth(info) + 8;
//                }
//
////                watermarkHeight = y + height + 7;
//
//            }
//             else if (mode.equalsIgnoreCase("Skeet")) {
//                if (Helper.mc.isSingleplayer()) {
//                    server = "localhost";
//                } else {
//                    server = Helper.mc.getServerData().serverIP.toLowerCase();
//                }
//                String text = "moon" + Formatting.GREEN + "ware" + Formatting.RESET + " | " + (MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getBoolValue() ? Formatting.LIGHT_PURPLE + "Protected" + Formatting.RESET : Formatting.RESET + Minecraft.getSession().getUsername()) + " | " + Minecraft.getDebugFPS() + "fps | " + server;
//                float width = MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth(text) + 6;
//                int height = 20;
//                int posX = x;
//                int posY = y;
//                RectHelper.drawRect(posX, posY, posX + width + 2.0f, posY + height, new Color(5, 5, 5, 255).getRGB());
//                RectHelper.drawBorderedRect(posX + 0.5f, posY + 0.5f, posX + width + 1.5f, posY + height - 0.5f, 0.5f, new Color(40, 40, 40, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
//                RectHelper.drawBorderedRect(posX + 2, posY + 2, posX + width, posY + height - 2, 0.5f, new Color(22, 22, 22, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
//
//                switch (WaterMark.logoColor.currentMode) {
//                    case "Default": {
//                        RenderHelper.drawImage(new Namespaced("moonware/skeet.png"), posX + 2.5f, posY + 2.5f, width - 3.0f, 1.0f, Color.WHITE);
//                        break;
//                    }
//                    case "Custom": {
//                        RectHelper.drawGradientRect(posX + 2.75, posY + 3, x + width - 1, posY + 4, new Color(WaterMark.customRectTwo.getColorValue()).getRGB(), color.getRGB());
//                        break;
//                    }
//                    case "Client": {
//                        RectHelper.drawRect(posX + 3, posY + 3, x + width - 0.7, posY + 4, color.getRGB());
//                    }
//                    case "Rainbow": {
//                        Color rainbow;
//                        for (int i = x + 3; i < x + width; i++) {
//                            rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
//                            RectHelper.drawRect(i, posY + 3, x + width - 0.7, posY + 4, rainbow.getRGB());
//                        }
//                    }
//                    case "Gradient": {
//                        RectHelper.drawGradientRect(x + 3, posY + 3, x + width - 0.7, posY + 4, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
//                    }
//                    case "Static": {
//                        RectHelper.drawRect(x + 3, posY + 3, x + width - 0.7, posY + 4, WaterMark.customRect.getColorValue());
//                    }
//                }
//                MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawShadow(text, posX + 4, posY + 7, -1);
//            } else if (mode.equalsIgnoreCase("OneTap v2")) {
//                if (Helper.mc.isSingleplayer()) {
//                    server = "localhost";
//                } else {
//                    server = Helper.mc.getServerData().serverIP.toLowerCase();
//                }
//                String text = "moonware | " + MoonWare.VERSION + " | " + server + " | 64 tick | " + Minecraft.player.connection.getPlayerInfo(Minecraft.player.getUniqueID()).getResponseTime() + "ms";
//                float width = MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth(text) + 4;
//
//                if (WaterMark.colorRectPosition.currentMode.equals("Top")) {
//                    switch (WaterMark.logoColor.currentMode) {
//                        case "Rainbow":
//                            Color rainbow;
//                            for (int i = x; i < x + width; i++) {
//                                rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
//                                RectHelper.drawSmoothRect(i, y - 3, x + width, y - 1.5f, rainbow.getRGB());
//                            }
//                            break;
//                        case "Gradient":
//                            RectHelper.drawSmoothGradientRect(x, y - 3, x + width, y - 0.5F, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
//                            break;
//                        case "Static":
//                            RectHelper.drawSmoothRect(x, y - 3, x + width, y - 0.5F, WaterMark.customRect.getColorValue());
//                            break;
//                        case "Default":
//                            RectHelper.drawSmoothRect(x, y - 3, x + width, y - 0.5F, color.getRGB());
//                            break;
//                    }
//                } else {
//                    switch (WaterMark.logoColor.currentMode) {
//                        case "Rainbow":
//                            Color rainbow;
//                            for (int i = x; i < x + width; i++) {
//                                rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
//                                RectHelper.drawSmoothRect(i, y, x + width, y + 12, rainbow.getRGB());
//                            }
//                            break;
//                        case "Gradient":
//                            RectHelper.drawSmoothGradientRect(x, y, x + width, y + 12, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
//                            break;
//                        case "Static":
//                            RectHelper.drawSmoothRect(x, y, x + width, y + 12, WaterMark.customRect.getColorValue());
//                            break;
//                        case "Default":
//                            RectHelper.drawSmoothRect(x, y, x + width, y + 12, color.getRGB());
//                            break;
//                    }
//                }
//
//                RectHelper.drawSmoothRect(x, y - 1, x + width, y + 10, new Color(47, 47, 47).getRGB());
//                MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawShadow(text, x + 2, y + 1, -1);
//            } else if (mode.equalsIgnoreCase("OneTap v3")) {
//                if (Helper.mc.isSingleplayer()) {
//                    server = "localhost";
//                } else {
//                    server = Helper.mc.getServerData().serverIP.toLowerCase();
//                }
//                String text = "MoonWare | " + server + " | Fps " + Minecraft.getDebugFPS() + " | " + Minecraft.player.connection.getPlayerInfo(Minecraft.player.getUniqueID()).getResponseTime() + "ms";
//                float width = MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth(text) + 4;
//
//
//                if (WaterMark.colorRectPosition.currentMode.equals("Top")) {
//                    switch (WaterMark.logoColor.currentMode) {
//                        case "Rainbow":
//                            Color rainbow;
//                            for (int i = x; i < x + width; i++) {
//                                rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
//                                RectHelper.drawSmoothRect(i, y - 3, x + width, y - 1.5f, rainbow.getRGB());
//                            }
//                            break;
//                        case "Gradient":
//                            RectHelper.drawSmoothGradientRect(x, y - 3, x + width, y - 0.5F, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
//                            break;
//                        case "Static":
//                            RectHelper.drawSmoothRect(x, y - 3, x + width, y - 0.5F, WaterMark.customRect.getColorValue());
//                            break;
//                        case "Default":
//                            RectHelper.drawSmoothRect(x, y - 3, x + width, y - 0.5F, color.getRGB());
//                            break;
//                    }
//                } else {
//                    switch (WaterMark.logoColor.currentMode) {
//                        case "Rainbow":
//                            Color rainbow;
//                            for (int i = x; i < x + width; i++) {
//                                rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
//                                RectHelper.drawSmoothRect(i, y + 10, x + width, y + 12, rainbow.getRGB());
//                            }
//                            break;
//                        case "Gradient":
//                            RectHelper.drawSmoothGradientRect(x, y + 10, x + width, y + 12, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
//                            break;
//                        case "Static":
//                            RectHelper.drawSmoothRect(x, y + 10, x + width, y + 12, WaterMark.customRect.getColorValue());
//                            break;
//                        case "Default":
//                            RectHelper.drawSmoothRect(x, y + 10, x + width, y + 12, color.getRGB());
//                            break;
//                    }
//
//
//                    RectHelper.drawSmoothRect(x, y - 1, x + width, y + 10, new Color(23, 23, 23, 110).getRGB());
//
//                    MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawShadow(text, x + 2, y + 1, -1);
//                }
//            } else if (mode.equalsIgnoreCase("NeverLose")) {
//                int ping = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(Minecraft.player.getUniqueID()).getResponseTime();
//                String dateFormat = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
//                String clientName = "MoonWare";
//
//                String text = Formatting.BOLD + "MoonWare" + Formatting.RESET + Formatting.GRAY + " | " + Formatting.RESET + Feature.getPlayerName() +
//                Formatting.GRAY + " | " + Formatting.RESET + (MoonWare.featureManager.getFeatureByClass(Disabler.class).getState() ? ping + "ms"  : ping + "ms" + Formatting.GRAY + Formatting.RESET + " | " + dateFormat);
//                float width = MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).getWidth(text) + 1;
//                //RectHelper.drawSmoothRect(x, y - 2, x + width + 3.0f, y + 11, new Color(0, 0, 28).getRGB());
//                RoundedUtil.drawRound(x,y - 2, width + 3.0f, 13,6, new Color(0,0,28));
//                MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).drawShadow(text, x + 2, y + 2, -1);
//                Minecraft.gameRenderer.setupOverlayRendering();
//            } else if (mode.equals("NoRender")) {
//                int ping;
//
//                if (Helper.mc.isSingleplayer()) {
//                    ping = 0;
//                } else {
//                    ping = (int) Helper.mc.getServerData().pingToServer;
//                }
//
//                MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawShadow("moonware §7" + MoonWare.VERSION + " §7[§f" + Helper.mc.getServerData().serverIP.toLowerCase() + " FPS§7]" + " §7[§f" + ping + " PING§7]", x, y, color.getRGB());
//            }
//        }
//        super.draw();
//    }
//    public static Map<Integer, Integer> cache = new HashMap();
//
//    private static void renderShadow0(double x, double y, double width, double height, int color, int blurRadius) {
//        GL11.glDisable(GL11.GL_ALPHA_TEST);
//        width += 10;
//        height += 10;
//        x -= blurRadius;
//        y -= blurRadius;
//        int identifier = (int) (width * height * blurRadius);
//        int texId = cache.getOrDefault(identifier, -1);
//        if (texId == -1) {
//            BufferedImage original = new BufferedImage((int) width, (int) height, 2);
//            Graphics g = original.getGraphics();
//            g.fillRect(blurRadius, blurRadius, (int) width - blurRadius * 2, (int) height - blurRadius * 2);
//            g.dispose();
//            BufferedImage blurred = new GaussianFilter((float) blurRadius).filter(original, null);
//            cache.put(identifier,
//                    texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false));
//        }
//        GlStateManager.bindTexture(texId);
//        GL11.glColor4f((color >> 16 & 0xFF) / 255F, (color >> 8 & 0xFF) / 255F, (color & 0xFF) / 255F,
//                (color >> 24 & 0xFF) / 255F);
//        GL11.glBegin(GL11.GL_QUADS);
//        GL11.glTexCoord2f(0.0f, 0.0f);
//        GL11.glVertex2d(x, y);
//        GL11.glTexCoord2f(0.0f, 1.0f);
//        GL11.glVertex2d(x, y + height);
//        GL11.glTexCoord2f(1.0f, 1.0f);
//        GL11.glVertex2d(x + width, y + height);
//        GL11.glTexCoord2f(1.0f, 0.0f);
//        GL11.glVertex2d(x + width, y);
//        GL11.glEnd();
//        GL11.glEnable(GL11.GL_ALPHA_TEST);
//    }
//
//    public static void renderShadow(double x, double y, double width, double height, int color, int blurRadius) {
//        renderShadow0(x, y, width, height, color, blurRadius);
//        GL11.glColor4f(1, 1, 1, 1);
//    }
}