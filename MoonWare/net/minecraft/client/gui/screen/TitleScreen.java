package net.minecraft.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Formatting;
import org.lwjgl.opengl.GL11;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.StencilUtil;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.blur.GaussianBlur;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.ui.titlescreen.TitleLikeScreen;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWFont;
import org.moonware.client.utils.MWUtils;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TitleScreen extends TitleLikeScreen {



    /** Configs and visuals setting variables*/

    public enum Category {
        Главная,Новости,Конфиги,Сайт;
    }
    private int scrollWheel = 0;
    private int scrollOffset = 0;
    private Category selected = Category.Главная;

    /** Configs and visuals setting  variables */


    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        this.scrollOffset = Interpolator.linear(this.scrollOffset,this.scrollWheel,2f/25);
        drawDefaultBackground();


        drawTitle();

        /** Configs and visuals setting */
        Color color1 , color2, color3, color4;
        color1 = DrawHelper.fade(new Color(102, 0, 255), 10, 100);
        color2 = DrawHelper.fade(new Color(102, 0, 255), 30, 100);
        color3 = DrawHelper.fade(new Color(102, 0, 255), 60, 100);
        color4 = DrawHelper.fade(new Color(102, 0, 255), 90, 100);
        this.OTHER_translateAnim = Interpolator.linear(this.OTHER_translateAnim,0, 2f/20);
        int fullX =Minecraft.getScaledRoundedWidth() - 390 + this.OTHER_translateAnim;
        int fullY = Minecraft.getScaledRoundedHeight() / 2 - Minecraft.getScaledRoundedHeight() / 4 - 80;
        int fullWidth = 380;
        int fullHeight = Minecraft.getScaledRoundedHeight() / 2;
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(fullX,fullY, fullWidth,fullHeight,6,new Color(1,1,1,67));
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(18);
        StencilUtil.uninitStencilBuffer();
        //RoundedUtil.drawRound(fullX,fullY, fullWidth,fullHeight,6,new Color(1,1,1,67));
        RoundedUtil.drawGradientRound(fullX, fullY, fullWidth, fullHeight,3, ColorUtil.applyOpacity(color1.darker().darker().darker(),.35f), ColorUtil.applyOpacity(color2.darker().darker().darker(),.35f), ColorUtil.applyOpacity(color3.darker().darker().darker(),.35f), ColorUtil.applyOpacity(color4.darker().darker().darker(),.35f));

        RenderUtils2.drawShadow(7,8,() -> RoundedUtil.drawRound(fullX,fullY, fullWidth,fullHeight,6,new Color(1,1,1,67)));
        StencilUtil.initStencilToWrite();
        RectHelper.drawRect(fullX - 2,fullY + 23, fullX + fullWidth +2, fullY + 23 - fullHeight,new Color(255,255,255,130).getRGB());
        StencilUtil.readStencilBuffer(1);

        RoundedUtil.drawGradientRound(fullX, fullY, fullWidth, fullHeight,3, ColorUtil.applyOpacity(color1.brighter().brighter(),.15f), ColorUtil.applyOpacity(color2.brighter().brighter(),.15f), ColorUtil.applyOpacity(color3.brighter().brighter(),.15f), ColorUtil.applyOpacity(color4.brighter().brighter(),.15f));
        StencilUtil.uninitStencilBuffer();
        int offset = 0;
        for (Category cat : Category.values()) {
            if (cat == selected) {
                RectHelper.drawGlow(fullX + 15 + offset,fullY + 5 - 9,fullX + 15 + offset - 2 + MWFont.MONTSERRAT_REGULAR.get(22).getWidth(cat.name()),fullY + 23,new Color(255,255,255, 48).getRGB());
            }
            MWFont.MONTSERRAT_REGULAR.get(22).draw( (cat == selected ? Formatting.BOLD : "") + cat.name(), fullX + 15 + offset, fullY + 5, -1);
            offset += MWFont.MONTSERRAT_REGULAR.get(22).getWidth(cat.name()) + 20;
        }
        RectHelper.drawRect(fullX - 0.5F,fullY + 22, fullX + fullWidth + 0.5F, fullY + 23,new Color(255,255,255,130).getRGB());
        if (selected == Category.Главная) {
            MWFont.MONTSERRAT_BOLD.get(30).drawCenter("Здесь пока-что ничего нет..", fullX + fullWidth / 2,fullY + fullHeight / 2,-1);
        }
        if (selected == Category.Новости) {
            RoundedUtil.drawRound(fullX + fullWidth - 5, fullY + 25,2,fullHeight - 27,1.3f,new Color(1,1,1,130));
            RoundedUtil.drawRound(fullX + fullWidth - 5,fullY + 25 + scrollOffset - 10, 2,20,1.3f,new Color(255,255,255,190));
            List<String> news = new ArrayList<>();
            news = Arrays.asList("Новое мейн меню!", "Профили пользователей",
                    "Добавлено данное меню","Артём даун", "Таргет есп теперь может принимать цвета худа");
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderHelper2.scissorRect(fullX - 2,fullY + 23.5f, fullX + fullWidth +2, fullY + 23 + fullHeight);
            StencilUtil.initStencilToWrite();
            RoundedUtil.drawRound(fullX,fullY, fullWidth,fullHeight,6,new Color(1,1,1,97));

            StencilUtil.readStencilBuffer(1);
            int offsetLeft = 0;
            int offsetRight = 0;
            for (int i = 0 ; i < news.size() ; i++) {
                String ws = news.get(i);
                if (i % 2 == 1) {
                    RoundedUtil.drawRound(fullX + 150, fullY + 40 + offsetRight - scrollOffset, 3  + MWFont.MONTSERRAT_BOLD.get(16).getWidth(ws),20,3, new Color(31,31,31, 76));
                    MWFont.MONTSERRAT_BOLD.get(16).draw(ws, fullX + 151,fullY + 45 + offsetRight - scrollOffset,-1);
                    offsetRight += 30;
                }else {
                    RoundedUtil.drawRound(fullX + 5, fullY + 40 + offsetLeft - scrollOffset,3 + MWFont.MONTSERRAT_BOLD.get(16).getWidth(ws),20,3, new Color(31,31,31, 107));
                    MWFont.MONTSERRAT_BOLD.get(16).draw(ws, fullX + 6,fullY + 45 + offsetLeft - scrollOffset,-1);
                    offsetLeft += 30;
                }
            }
            StencilUtil.uninitStencilBuffer();
            GL11.glDisable(GL11.GL_SCISSOR_TEST);

        }else if (selected == Category.Конфиги) {
            Color color1T, color2T, color3T, color4T;
            color1T = new Color(0, 0, 0);
            color2T = new Color(0, 0, 0);
            color3T = new Color(0, 0, 0);
            color4T = new Color(0, 0, 0);
            RoundedUtil.drawRound(fullX + fullWidth - 5, fullY + 25,2,fullHeight - 27,1.3f,new Color(1,1,1,130));
            RoundedUtil.drawRound(fullX + fullWidth - 5,fullY + 25 + scrollOffset - 10, 2,20,1.3f,new Color(255,255,255,190));
            RoundedUtil.drawGradientRound(fullX + 5,fullY + fullHeight - 30, 130,20,2,ColorUtil.applyOpacity(color1T,.35f),ColorUtil.applyOpacity(color2T,.35f), ColorUtil.applyOpacity(color3T,.35f), ColorUtil.applyOpacity(color4T,.35f));
        }
        super.draw(mouseX, mouseY, partialTick);
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int button) {
        int fullX =Minecraft.getScaledRoundedWidth() - 390 + this.OTHER_translateAnim;
        int fullY = Minecraft.getScaledRoundedHeight() / 2 - Minecraft.getScaledRoundedHeight() / 4 - 80;
        int fullWidth = 380;
        int fullHeight = Minecraft.getScaledRoundedHeight() / 2;
        int offset = 0;
        for (Category cat : Category.values()) {
            if (MWUtils.isHovered(fullX + 15 + offset,fullY, MWFont.MONTSERRAT_REGULAR.get(22).getWidth(cat.name()),17,mouseX,mouseY) &&button == 0) {
                if (cat == Category.Сайт) {
                    try {
                        URI uri = new URI("https://vk.com/moonwarerage");
                        Desktop dt = Desktop.getDesktop();
                        dt.browse(uri);
                    }catch (Exception ex) {
                    }
                }else {
                    selected = cat;
                    scrollWheel =4;
                }
            }
            offset += MWFont.MONTSERRAT_REGULAR.get(22).getWidth(cat.name()) + 20;
        }
        super.mousePressed(mouseX, mouseY, button);
    }

    @Override
    public void mouseScrolled(int mouseX, int mouseY, int scroll) {
        int fullHeight = Minecraft.getScaledRoundedHeight() / 2;
        if (scroll < 0) {
            scrollWheel += 10;
        }else{
            scrollWheel -=10;
        }
        scrollWheel = MathHelper.clamp(scrollWheel,4,fullHeight - 23 - 10);
        super.mouseScrolled(mouseX, mouseY, scroll);
    }
}
