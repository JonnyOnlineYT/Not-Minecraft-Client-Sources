package org.moonware.client.ui.titlescreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.resources.I18n;
import optifine.Config;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.StencilUtil;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.utils.MWFont;

import java.awt.*;

public class TitleLikeScreen extends Screen {
    public int X_BUTTON = 4;
    public int OTHER_translateAnim = 250;
    @Override
    public void init() {
        widgets.add(new TitleButton(X_BUTTON, 75, 128, 20, this instanceof TitleScreen,
                I18n.format("Main Screen"), btn -> Minecraft.openScreen(new TitleScreen())));
        widgets.add(new TitleButton(X_BUTTON, 75 + 27, 128, 20, this instanceof GuiWorldSelection,
                I18n.format("menu.singleplayer"), btn -> Minecraft.openScreen(new GuiWorldSelection())));
        widgets.add(new TitleButton(X_BUTTON, 102 + 27, 128, 20, this instanceof MultiplayerScreen,
                I18n.format("menu.multiplayer"), btn -> Minecraft.openScreen(new MultiplayerScreen())));
        widgets.add(new TitleButton(X_BUTTON, 129 + 27, 128, 20, this instanceof AltScreen,
                I18n.format("Alt Login"), btn -> Minecraft.openScreen(new AltScreen())));
        widgets.add(new TitleButton(X_BUTTON, 129 + 27 + 27, 128, 20, this instanceof GuiOptions,
                I18n.format("menu.options"), btn -> Minecraft.openScreen(new GuiOptions())));
        widgets.add(new TitleButton(X_BUTTON, 129 + 27 + 27 + 27, 128, 20, false,
                I18n.format("menu.quit"), btn -> Minecraft.shutdown()));
        OTHER_translateAnim = 250;
        super.init();
    }

    public void drawRunnable() {
        RoundedUtil.drawRound(Minecraft.getScaledRoundedWidth() - 2 -MWFont.MONTSERRAT_BOLD.get(17).getWidth("Hello, " + MoonWare.LICENSE) ,-3,100,15,3,new Color(0, 0, 0, 97));

    }
    public final void drawTitle() {
        if (Config.isFastRender()) {
            Gui.drawRect(9, 18, 9 + 142, 18 + 220, 0x43000000);
            Gui.drawRect(14, Minecraft.getScaledRoundedHeight() - 38, 14 + 132, Minecraft.getScaledRoundedHeight() - 38 + 24, 0x3B000000);
        } else {
            RoundedUtil.drawRound(-2, -2, 140, Minecraft.height + 4, 4, true, new Color(0, 0, 0, 67));
            RenderUtils2.drawBlur(15, () -> RoundedUtil.drawRound(-2, -2, 140, Minecraft.height + 4, 4, true, new Color(0, 0, 0, 97)));
            RenderUtils2.drawShadow(4, 5, () -> RoundedUtil.drawRound(-2, -2, 140, Minecraft.height + 4, 4, true, new Color(0, 0, 0, 97)));
            RoundedUtil.drawRound(Minecraft.getScaledRoundedWidth() - 60,-3,100,15,3,new Color(0, 0, 0, 97));
            RenderUtils2.drawBlur(15, this::drawRunnable);
            RenderUtils2.drawShadow(4,5, this::drawRunnable);
            StencilUtil.initStencilToWrite();
            drawRunnable();
            StencilUtil.readStencilBuffer(2);
            DrawHelper.drawGlow(Minecraft.getScaledRoundedWidth() - 55,-50,Minecraft.getScaledRoundedWidth(),45,new Color(0, 0, 0, 160).getRGB());

            StencilUtil.uninitStencilBuffer();
            MWFont.MONTSERRAT_BOLD.get(17).draw("Hello, " + MoonWare.LICENSE,Minecraft.getScaledRoundedWidth() - 2  -MWFont.MONTSERRAT_BOLD.get(17).getWidth("Hello, " + MoonWare.LICENSE),4,-1);
            //RenderUtils2.drawBlur(1, () -> RoundedUtil.drawRound(14, Minecraft.getScaledRoundedHeight() - 38, 132, 24, 4, true, new Color(0, 0, 0, 89)));
            //RenderUtils2.drawShadow(0, 0, () -> RoundedUtil.drawRound(14, Minecraft.getScaledRoundedHeight() - 38, 132, 24, 4, true, new Color(0, 0, 0, 89)));
        }
        /*GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        DrawHelper.startSmooth();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        Minecraft.getTextureManager().bindTexture(new Namespaced("moonware/rqbad.png"));
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RoundedUtil.drawRoundTextured(17, Minecraft.getScaledRoundedHeight() - 35, 18, 18, 5, 255);
        DrawHelper.endSmooth();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();*/
       //MWFont.MONTSERRAT_BOLD.get(12).drawShadow("Hello", 40, Minecraft.getScaledRoundedHeight() - 30, new Color(145, 145, 145).getRGB());
        //MWFont.MONTSERRAT_BOLD.get(16).drawShadow(MoonWare.LICENSE, 40, Minecraft.getScaledRoundedHeight() - 24, -1);
        //MWFont.ELEGANT_ICONS.get(30).drawShadow("E", 130, Minecraft.getScaledRoundedHeight() - 30.5f, -1);
        Color color1 , color2, color3, color4;
        color1 = DrawHelper.fade(new Color(102, 0, 255), 10, 100);
        color2 = DrawHelper.fade(new Color(102, 0, 255), 30, 100);
        color3 = DrawHelper.fade(new Color(102, 0, 255), 60, 100);
        color4 = DrawHelper.fade(new Color(102, 0, 255), 90, 100);
        RoundedUtil.drawGradientRound(78 - 9 - 2 - 47, 28 - 4, MWFont.MONTSERRAT_BOLD.get(30).getWidth("MoonWare") + 8, 20, 5, color1, color2, color3, color4);
        //RoundedUtil.drawRound(0,0,140,250,2, new Color(0x1B000000, true));
        if (!(Minecraft.screen instanceof TitleScreen)) {
            MWFont.MONTSERRAT_BOLD.get(30).drawCenterShadow("MoonWare", 78 - 9 - 2, 28f, -1);
        }else {
            MWFont.MONTSERRAT_BOLD.get(30).drawCenterShadow("MoonWare", 78 - 9 - 2, 28f, -1);
        }
        //MWFont.MONTSERRAT_BOLD.get(40).drawCenterShadow("РЕЛИЗ", 78 - 9 - 2 + 150, 78f, -1);

    }
}
