package org.moonware.client.ui.govnogui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.render.GlowUtil;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWFont;
import org.moonware.client.utils.MWUtils;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class MainScreen extends Screen {

    /**  Говнище*/
    public int GUI_WIDTH = 270;
    public final int GUI_HEIGHT = 160;
    public static int DRAG_WIDTH = 80;
    public static int DRAG_HEIGHT = 35;
    public static final int SEARCH_HEIGHT = 20;
    public int x = -1;
    public int y = -1;
    public int mouseHoldX = -1;
    public int mouseHoldY = -1;
    /**  Говнище*/

    private double ScaleAnimationValue;
    private double TranslateAnimationValue;
    private TimerHelper timer = new TimerHelper();
    private TimerHelper timer2 = new TimerHelper();

    private boolean exitBool;
    public void dragging(int mouseX, int mouseY) {
        if (mouseHoldX != -1 && mouseHoldY != -1) {
            x = mouseX - mouseHoldX;
            y = mouseY - mouseHoldY;
            init();
        }
        DRAG_WIDTH = getGuiWidth();
    }
    public int getGuiWidth() {
        int widthGui = 340;
        return (int) widthGui;
    }
    public int getGuiHeight() {
        int heightGui = 200;
        return (int) heightGui;
    }
    public MainScreen() {
        exitBool = false;
        ScaleAnimationValue = 0;
        TranslateAnimationValue = 0;
    }
    @Override
    public void init() {
        if (x == -1 || y == -1) { // initialize variables
            x = width / 2 - getGuiWidth() / 2;
            y = height / 2 - getGuiHeight() / 2;
        }
        /*This prevents you from going off screen when dragging.*/
        x = MathHelper.clamp(x, 0, width - getGuiWidth());
        y = MathHelper.clamp(y, 0, height - getGuiHeight());
    }

    @Override
    public boolean escapeCloses() {
        while (exitBool && ScaleAnimationValue < 0.5) {
            return  true;
        }
        return false;
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Keyboard.KEY_ESCAPE) {
            exitBool = true;
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        super.draw(mouseX, mouseY, partialTick);
        if (mouseHoldX != -1 && mouseHoldY != -1) {
            x = mouseX - mouseHoldX;
            y = mouseY - mouseHoldY;
            init();
        }
        if (exitBool && TranslateAnimationValue > Minecraft.getScaledRoundedWidth() + getGuiWidth() + (Minecraft.getScaledRoundedWidth() / 15) + this.x - 300) {
            exitBool = false;
            ScaleAnimationValue = 0;
            TranslateAnimationValue = 0;
            close();
        }
        dragging(mouseX,mouseY);
        if (!exitBool ) {
            timer.reset();
            if (!timer2.hasReached(500)) {
                TranslateAnimationValue  = Interpolator.linear(ScaleAnimationValue,0,2f/50);
                ScaleAnimationValue = Interpolator.linear(ScaleAnimationValue,1.1f,2f/20);
            }else{
                TranslateAnimationValue  = Interpolator.linear(ScaleAnimationValue,0,2f/50);
                ScaleAnimationValue = Interpolator.linear(ScaleAnimationValue,1,2f/20);
            }
        }else{
            TranslateAnimationValue  = Interpolator.linear(TranslateAnimationValue, Minecraft.getScaledRoundedWidth() + getGuiWidth() + (Minecraft.getScaledRoundedWidth() / 15) + this.x,2f/(35 + (Minecraft.getScaledRoundedWidth() / 15)));
            ScaleAnimationValue = Interpolator.linear(ScaleAnimationValue,-0.3f,2f/40);
        }
        MWUtils.scale((float) ((float) (this.x + getGuiWidth() / 2) - TranslateAnimationValue), this.y + getGuiHeight() / 2, (float) ScaleAnimationValue,() -> drawRunnable());
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int button) {
        if (button == 0 && mouseX >= x && mouseY >= y && mouseX <= x + DRAG_WIDTH && mouseY <= y + DRAG_HEIGHT) { //determine the ability to launch the drag and run it
            mouseHoldX = mouseX - x;
            mouseHoldY = mouseY - y;
        }
        super.mousePressed(mouseX, mouseY, button);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (button == 0) {
            mouseHoldX = mouseHoldY = -1; //give the ability to stop the drag
        }
    }

    @Override
    public void mouseScrolled(int mouseX, int mouseY, int scroll) {
        super.mouseScrolled(mouseX, mouseY, scroll);
    }
    public void drawRunnable() {
        RoundedUtil.drawRound((float) (this.x - TranslateAnimationValue), this.y,getGuiWidth(),getGuiHeight(),4,new Color(255,255,255));
        glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils2.scissorRect((float) (x - TranslateAnimationValue),this.y + 20,this.x + getGuiWidth(),this.y + 20 + getGuiHeight());
        if (ScaleAnimationValue > 0.99 && ScaleAnimationValue < 1.01)
            RoundedUtil.drawRound((float) (this.x - TranslateAnimationValue), this.y,getGuiWidth(),getGuiHeight(),4,new Color(225,225,225));

        glDisable(GL_SCISSOR_TEST);
        Gui.drawRect((float) (x - 1 - TranslateAnimationValue),this.y + 19, this.x + getGuiWidth(),this.y + 20,new Color(60,60,60,70).getRGB());
        GlowUtil.drawBlurredShadow((float) (x - 1 - TranslateAnimationValue),this.y + 20, getGuiWidth() +2,1,5,new Color(31,31,31,255),0);
        MWFont.SF_UI_TEXT_SEMIBOLD.get(30).drawCenter("Client UI",x - 1 - TranslateAnimationValue + getGuiWidth() / 2, this.y + 2, new Color(71,71,71).getRGB());
        RenderUtils2.drawShadow(7,2,() -> RoundedUtil.drawRound((float) (this.x - TranslateAnimationValue), this.y,getGuiWidth(),getGuiHeight(),4,new Color(31,31,31)));
    }
}
