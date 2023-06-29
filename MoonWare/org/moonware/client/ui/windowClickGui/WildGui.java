package org.moonware.client.ui.windowClickGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Formatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWFont;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.StencilUtil;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.ShaderUtil;
import org.moonware.client.helpers.Utils.blur.GaussianBlur;
import org.moonware.client.helpers.Utils.render.RenderUtil;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.windowClickGui.implWild.ModuleComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

import static org.lwjgl.opengl.GL11.*;

public class WildGui  extends Screen {
    public Type selected = Type.Combat; // Creating a variable with a selected type`s
    public int GUI_WIDTH = 270;
    public final int GUI_HEIGHT = 160;
    public static final int DRAG_WIDTH = 80;
    public static final int DRAG_HEIGHT = 35;
    public static final int SEARCH_HEIGHT = 20;
    public boolean isPlayed;
    public int button;

    /*Creating drag variables*/
    public boolean exiit;
    public int x = -1;
    public int y = -1;
    public int mouseHoldX = -1;
    public int mouseHoldY = -1;
    private double translateValue;
    private double translateValue2;
    private TimerHelper exit = new TimerHelper();
    private float widthGui;
    private float heightGui;
    private float circleanim;
    private float bluranim;
    public TimerHelper openanim = new TimerHelper();
    public TimerHelper openanim1 = new TimerHelper();
    public float scrollAnim;
    public float scrollAnim1;
    public int rAnim;
    public int gAnim;
    public int bAnim;
    public int aAnim;
    public double scaleAnim;
    public WildGui() {
        exiit = false;
    }
    @Override
    public void init() {
        exiit = false;
        if (x == -1 || y == -1) { // initialize variables
            x = width / 2 - getGuiWidth() / 2;
            y = height / 2 - getGuiHeight() / 2;
        }
        /*This prevents you from going off screen when dragging.*/
        x = MathHelper.clamp(x, 0, width - getGuiWidth());
        y = MathHelper.clamp(y, 0, height - getGuiHeight());
        translateValue = 1.0;
        /*==============================================*/

    }

    @Override
    public void onClosed() {
        if (widthGui < 10) {
            //
        }
        openanim.reset();
        scrollAnim = 1.0F;
        isPlayed = false;
        scaleAnim = 0;
        super.onClosed();
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 1) {
            openanim.reset();
            circleanim = 0;
            bluranim = 0;
            scaleAnim = 0;
            rAnim = 0;
            gAnim = 0;
            bAnim = 0;
            aAnim = 0;
        }
       super.keyPressed(key, c);
    }

    public void drawRoundBgCircle(int mouseX, int mouseY, int x, int y, int width, int height, Color color, boolean blurredshadow) {
        if (blurredshadow) {
            StencilUtil.initStencilToWrite();
            RenderUtils2.drawBlurredShadow(x, y, width, height, 3, color);
            StencilUtil.readStencilBuffer(1);
            //RoundedUtil.drawRound(mouseX - 10, mouseY - 10,20,20,1f,new Color(255,255,255));
            //RenderUtil.drawGoodCircle(mouseX + 5,mouseY,16,new Color(255,255,255).getRGB());
            RenderUtils2.drawBlurredShadow(mouseX - 5,mouseY - 5, 10,10,19,new Color(255,255,255));
            StencilUtil.uninitStencilBuffer();
            RoundedUtil.drawRound(x, y, width, height, 3, color);
            RenderUtils2.drawBlur(12,() -> RenderUtils2.drawBlurredShadow(x, y, width, height, 3, color));
            RenderUtils2.drawShadow(12,8,() -> RoundedUtil.drawRound(x, y, width, height, 3, color));
        }
    }
    public int getGuiWidth() {
        widthGui = 240;
        return (int) widthGui;
    }
    public int getGuiHeight() {
        heightGui = 200;
        return (int) heightGui;
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        super.draw(mouseX, mouseY, partialTick);
        if (!openanim.hasReached(400)) {
            scaleAnim = Interpolator.linear(scaleAnim,1.05F,2f/3);
            rAnim = Interpolator.linear(rAnim,213,2f/20);
            gAnim = Interpolator.linear(gAnim,213,2f/20);
            bAnim = Interpolator.linear(bAnim,213,2f/20);
            aAnim = Interpolator.linear(aAnim,190,2f/20);

        }else {
            scaleAnim = Interpolator.linear(scaleAnim,1.0,2f/13);
            rAnim = Interpolator.linear(rAnim,113,2f/50);
            gAnim = Interpolator.linear(gAnim,113,2f/50);
            bAnim = Interpolator.linear(bAnim,113,2f/50);
            aAnim = Interpolator.linear(aAnim,50,2f/50);
        }
        GlStateManager.pushMatrix();
        int mouseDWHEEL = Mouse.getDWheel();
        if (mouseDWHEEL > 0) {
            scrollAnim1 += ClickGui.speed.getCurrentValue();
        }
        if (mouseDWHEEL < 0) {
            scrollAnim1 -= ClickGui.speed.getCurrentValue();
        }
        scrollAnim = (float) Interpolator.linear(scrollAnim, scrollAnim1,2f/100);
        circleanim = (float) Interpolator.linear(circleanim, Minecraft.width + Minecraft.height,2f/390);
        if (!isPlayed) {
            MWUtils.playSound("opening.wav",-30.0f + 4 * 3.0F);
            isPlayed = true;
        }

        StencilUtil.initStencilToWrite();
        RectHelper.drawCircle(width / 2, height /2 , 0, 357, circleanim,100,true,new Color(31,31,31,100));
        StencilUtil.readStencilBuffer(1);
        GlStateManager.pushMatrix();
        renderColor(3);

        GlStateManager.popMatrix();

        GaussianBlur.renderBlur(6);
        StencilUtil.uninitStencilBuffer();
        GlStateManager.pushMatrix();
        glTranslatef ( x ,  y ,  0 );
        glScaled ( scaleAnim ,  scaleAnim ,  1 );
        glTranslatef (- x , - y ,  0 );
        RoundedUtil.drawRound(x,y,getGuiWidth(),getGuiHeight(),6,new Color(rAnim, gAnim, bAnim, aAnim));
        GlStateManager.popMatrix();
        RenderUtils2.drawBlur(16,() -> RoundedUtil.drawRound(x - 1,y - 1,getGuiWidth() + 2,getGuiHeight() + 2,3,new Color(rAnim, gAnim, bAnim, aAnim)));


        EntityPlayer target = Minecraft.player;
        glTranslatef ( x ,  y ,  0 );
        glScaled ( scaleAnim ,  scaleAnim ,  1 );
        glTranslatef (- x , - y ,  0 );
        GlStateManager.pushMatrix();
        //GlStateManager.scale(this.guiScaleAnim,this.guiScaleAnim,1);
        //RenderUtils2.drawBlurredShadow(x + 18,y + 18, 8,8,14,new Color(0,0,0));
        StencilUtil.initStencilToWrite();
        RenderUtil.drawGoodCircle(x + 18,y + 18,8,-1);
        StencilUtil.readStencilBuffer(1);
        for (NetworkPlayerInfo targetHead : Minecraft.player.connection.getPlayerInfoMap()) {
            if (target instanceof EntityPlayer || target != null) {
                if (Minecraft.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == Minecraft.player) {
                    if (targetHead != null) {
                        Minecraft.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    } else {
                        Minecraft.getTextureManager().bindTexture(new Namespaced("moonware/icons/xuss.jpg"));
                    }

                    float hurtPercent = 0;
                    glPushMatrix();
                    glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                    Gui.drawScaledCustomSizeModalRect(x + 10, y + 10, 8.0f, 8.0f, 8, 8, 16, 16, 64.0F, 64.0F);
                    glPopMatrix();
                    GlStateManager.bindTexture(0);
                } else {
                    //RenderHelper2.drawImage(new ResourceLocation("moonware/icons/xuss.jpg"), x + 4.5F, y + 5.5F, 20, 20, entityPlayer.hurtTime > 0 ? Color.WHITE : Color.WHITE);
                }
            } else {
                RenderHelper2.drawImage(new Namespaced("moonware/icons/xuss.jpg"), x + 4.5F, y + 5.5F, 20, 20, Color.WHITE);
            }
            glDisable(3089);
        }
        StencilUtil.uninitStencilBuffer();
        GlStateManager.popMatrix();
        RenderUtils2.drawShadow(2,4,() -> RenderUtil.drawGoodCircle(x + 18,y + 18,7.8F,-1));
        MWFont.MONTSERRAT_BOLD.get(18).draw(Formatting.BOLD + "MWARE", x + 27, y + 11, new Color(231,231,231).getRGB());
        MWFont.MONTSERRAT_REGULAR.get(15).draw(Minecraft.getSession().getUsername(), x + 27, y + 21, new Color(211,211,211).getRGB());
        MWFont.MONTSERRAT_REGULAR.get(15).drawCenter("main", x + 37,y + 35, new Color(183,183,183).getRGB());
        int offset = 0;
        int yOffset = 13;
        for (Type category : Type.values()) {
            if (category == selected) {
                drawRoundBgCircle(mouseX,mouseY,x + 5, y + 45 + offset, 80, 10,new Color(111,111,111,70),true);
            }
            MWFont.MONTSERRAT_REGULAR.get(15).draw(category.getName().toLowerCase(),x + 10, y + 48 + offset, new Color(183,183,183).getRGB());
            offset += yOffset;
        }
        int moffset = 0;
        int mYoffset = 45;
        glEnable(GL_SCISSOR_TEST);
        RenderUtils2.scissorRect(x + 95, y + 2, x + getGuiWidth(), y + getGuiHeight());

        ArrayList<Feature> features = new ArrayList<>(MoonWare.featureManager.getFeaturesForCategory(selected));
        Collections.sort(features);
        char a = 1;
        for (int id = 0 ; id < features.size() ; id++) {
            Feature feature = features.get(id);
            Feature nextfeature = getNextFeature(id);
            //System.out.println(getNextFeature(id));
            float offsetSettings = 0;
            for (Setting setting : feature.getSettings()) {
                if (!setting.isVisible())continue;
                if (setting instanceof BooleanSetting) {
                    offsetSettings += 10;
                }else if (setting instanceof NumberSetting) {
                    offsetSettings += 10;
                }else if (setting instanceof ListSetting) {
                    offsetSettings += 10;
                }else if (setting instanceof ColorSetting) {
                    offsetSettings += 10;
                }
            }
            new ModuleComponent(feature,x + 100, (int) (y + 30 + moffset + scrollAnim),mouseX,mouseY,(int) (15 + feature.getHoverGuiAnim(12)),a != feature.getLabel().charAt(0),this.button).draw();

            if (nextfeature.getLabel().charAt(0) != feature.getLabel().charAt(0)) {
                mYoffset = (int) (34 + feature.getHoverGuiAnim(12));
            }else {
                mYoffset = (int) (19 + feature.getHoverGuiAnim(12));
            }

            a = feature.getLabel().charAt(0);
            moffset += mYoffset;
        }
        //System.out.println(this.scrollAnim);
        glDisable(GL_SCISSOR_TEST);
        if (mouseHoldX != -1 && mouseHoldY != -1) {
            x = mouseX - mouseHoldX;
            y = mouseY - mouseHoldY;
            init();
        }
        if (Keyboard.isKeyDown(205)) {
            x += 4;
            init();
        }
        if (Keyboard.isKeyDown(203)) {
            x -= 4;
            init();
        }
        if (Keyboard.isKeyDown(200)) {
            y-=4;
            init();
        }
        if (Keyboard.isKeyDown(208)) {
            y+=4;
            init();
        }
        GlStateManager.popMatrix();
    }

    public Feature getNextFeature(int id) {
        ArrayList<Feature> features = new ArrayList<>(MoonWare.featureManager.getFeaturesForCategory(selected));
        Collections.sort(features);
        int nextid = id;
        nextid = MathHelper.clamp(id + 1, 1, features.size() - 1);
        return  features.get(nextid);
    }
    @Override
    public void mousePressed(int mouseX, int mouseY, int button) {
        int offset = 0;
        int yOffset = 13;
        ArrayList<String> abc = new ArrayList<String>();
        for (Type category : Type.values()) {
            abc.add(category.getName());
            if (ИсМаусИн(mouseX,mouseY,x + 5, y + 45 + offset, 80,10) && button == 0) {
                selected = category;
                //System.out.println("true");
            }else{
                //System.out.println("false");
            }
        }
        for (Type category :  Type.values()) {
            if (ИсМаусИн(mouseX,mouseY,x + 5, y + 45 + offset, 80,10) && button == 0) {
                selected = category;
            }
            if (category == selected) {
                //drawRoundBgCircle(mouseX,mouseY,x + 5, y + 45 + offset, 80, 10,new Color(111,111,111,70),true);
            }
            MWFont.MONTSERRAT_REGULAR.get(15).draw(category.getName().toLowerCase(),x + 10, y + 48 + offset, new Color(183,183,183).getRGB());
            offset += yOffset;
        }
        super.mousePressed(mouseX, mouseY, button);
        if (button == 0 && mouseX >= x && mouseY >= y && mouseX <= x + DRAG_WIDTH && mouseY <= y + DRAG_HEIGHT) { //determine the ability to launch the drag and run it
            mouseHoldX = mouseX - x;
            mouseHoldY = mouseY - y;
        }
        int moffset = 0;
        int mYoffset = 45;
        glEnable(GL_SCISSOR_TEST);
        RenderUtils2.scissorRect(x + 95, y + 2, x + getGuiWidth(), y + getGuiHeight());

        ArrayList<Feature> features = new ArrayList<>(MoonWare.featureManager.getFeaturesForCategory(selected));
        Collections.sort(features);
        char a = 1;
        for (int id = 0 ; id < features.size() ; id++) {
            Feature feature = features.get(id);
            Feature nextfeature = getNextFeature(id);

            int xm = x + 100;
            int wm = 105;
            float ym = y + 30 + moffset + scrollAnim - 1;
            int hm = 15;
            if (mouseX > xm && mouseX < xm + wm && mouseY > ym && mouseY < ym + hm) {
                if (button == 0)
                    feature.toggle();
                else if (button == 1) {
                    feature.setHovered(!feature.isHovered());
                }
            }
            float offsetSettings = 0;
            for (Setting setting : feature.getSettings()) {
                if (setting instanceof BooleanSetting) {
                    offsetSettings += 10;
                }else if (setting instanceof NumberSetting) {
                    offsetSettings += 10;
                }else if (setting instanceof ListSetting) {
                    offsetSettings += 10;
                }else if (setting instanceof ColorSetting) {
                    offsetSettings += 10;
                }
            }
            if (nextfeature.getLabel().charAt(0) != feature.getLabel().charAt(0)) {
                mYoffset = !feature.isHovered ? 39 - 5 : (int) (39 - 5 + offsetSettings);
            }else {
                mYoffset = !feature.isHovered ? 24 - 5 : (int) (24 - 5 + offsetSettings);
            }


            a = feature.getLabel().charAt(0);
            moffset += mYoffset;
        }
        this.button = button;
    }

    public boolean ИсМаусИн(int mouseX,int mouseY, int x,int y, int width,int height) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (button == 0) {
            mouseHoldX = mouseHoldY = -1; //give the ability to stop the drag
        }
    }
    public static ShaderUtil shader = new ShaderUtil("moonware/shaders/white.frag");
    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);
    public static void renderColor(float force) {
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);


        framebuffer = RenderUtil.createFrameBuffer(framebuffer);

        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        shader.init();
        setupUniforms2(force);
        RenderUtil.bindTexture((Minecraft.getFramebuffer()).framebufferTexture);

        ShaderUtil.drawQuads();
        framebuffer.unbindFramebuffer();
        shader.unload();

        Minecraft.getFramebuffer().bindFramebuffer(true);
        shader.init();
        setupUniforms2(force);
        RenderUtil.bindTexture(framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        shader.unload();

        GlStateManager.resetColor();
        GlStateManager.bindTexture(0);
    }
    public static void setupUniforms2(float force) {
        shader.setUniformi("textureIn", 0);
        shader.setUniformf("force", force);
    }
}
