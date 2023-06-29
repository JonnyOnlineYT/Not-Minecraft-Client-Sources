package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender2D;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.Utils.MathUtils;
import org.moonware.client.helpers.Utils.ShaderUtil;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.renderer.OpenGlHelper.glUniform1;

public class GlowESP extends Feature {

    public static BooleanSetting seperate = new BooleanSetting("Seperate Texture",  true, () -> true);
    public static ColorSetting glowColor = new ColorSetting("Color Color", new Color(255, 255, 255).getRGB(), () -> true);
    public static NumberSetting exposure = new NumberSetting("Exposure", 3, 1.0f, 10, 1.0f, () -> true);
    public static NumberSetting radius = new NumberSetting("Radius", 5, 1.0f, 150, 1.0f, () -> true);
    public static NumberSetting blurSize = new NumberSetting("Radius", 5, 1.0f, 500, 5.0f, () -> true);



    public GlowESP() {
        super("GlowESP", "", Type.Visuals);
        this.addSettings(glowColor,blurSize, radius, exposure, seperate);
    }

    public static boolean renderNameTags = true;
    private final ShaderUtil outlineShader = new ShaderUtil("moonware/shaders/outline.frag");
    private final ShaderUtil glowShader = new ShaderUtil("moonware/shaders/glow.frag");

    public Framebuffer framebuffer;
    public Framebuffer outlineFrameBuffer;
    public Framebuffer glowFrameBuffer;
    private final Frustum frustum = new Frustum();

    private final List<Entity> entities = new ArrayList<>();


    @Override
    public void onEnable() {
        super.onEnable();
    }

    public void createFrameBuffers() {
        try {
            framebuffer = RenderUtils2.createFrameBuffer(framebuffer);
            outlineFrameBuffer = RenderUtils2.createFrameBuffer(outlineFrameBuffer);
            glowFrameBuffer = RenderUtils2.createFrameBuffer(glowFrameBuffer);
        }catch (Exception ex) {
        }
    }

    @EventTarget
    public void render(EventRender3D e) {
        createFrameBuffers();
        collectEntities();
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        renderEntities(e.getPartialTicks());
        framebuffer.unbindFramebuffer();
        Helper.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.disableLighting();
        GlStateManager.resetColor();
    }
    @EventTarget
    public void render(EventRender2D e) {

        ScaledResolution sr = new ScaledResolution(Helper.mc);
        if (framebuffer != null && outlineFrameBuffer != null && entities.size() > 0) {
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.0f);
            GlStateManager.enableBlend();
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            outlineFrameBuffer.framebufferClear();
            outlineFrameBuffer.bindFramebuffer(true);
            outlineShader.init();
            setupOutlineUniforms(0, 1);
            RenderUtils2.bindTexture(framebuffer.framebufferTexture);
            ShaderUtil.drawQuads();
            outlineShader.init();
            setupOutlineUniforms(1, 0);
            RenderUtils2.bindTexture(framebuffer.framebufferTexture);
            ShaderUtil.drawQuads();
            outlineShader.unload();
            outlineFrameBuffer.unbindFramebuffer();

            GlStateManager.color(1, 1, 1, 1);
            glowFrameBuffer.framebufferClear();
            glowFrameBuffer.bindFramebuffer(true);
            glowShader.init();
            setupGlowUniforms(1, 0);
            RenderUtils2.bindTexture(outlineFrameBuffer.framebufferTexture);
            ShaderUtil.drawQuads();
            glowShader.unload();
            glowFrameBuffer.unbindFramebuffer();

            Helper.mc.getFramebuffer().bindFramebuffer(true);
            glowShader.init();
            setupGlowUniforms(0, 1);
            if (seperate.getBoolValue()) {
                GL13.glActiveTexture(GL13.GL_TEXTURE16);
                RenderUtils2.bindTexture(framebuffer.framebufferTexture);
            }
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            RenderUtils2.bindTexture(glowFrameBuffer.framebufferTexture);
            ShaderUtil.drawQuads();
            GlStateManager.resetColor();
            glowShader.unload();

        }

    };


    public void setupGlowUniforms(float dir1, float dir2) {
        //double blurSize = 1.0/128.0;
        Color color = getColor();
        glowShader.setUniformi("texture", 0);
        if (seperate.getBoolValue()) {
            glowShader.setUniformi("textureToCheck", 16);
        }
        glowShader.setUniformf("radius", radius.getNumberValue());
        glowShader.setUniformf("blurSize",1f/512);
        glowShader.setUniformf("blurMoment",blurSize.getNumberValue());


        glowShader.setUniformf("texelSize", 1.0f / Minecraft.getScaledRoundedWidth(), 1.0f / Minecraft.getScaledRoundedHeight());
        glowShader.setUniformf("direction", dir1, dir2);
        glowShader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        glowShader.setUniformf("exposure", (float) (exposure.getNumberValue()));
        glowShader.setUniformi("avoidTexture", seperate.getBoolValue() ? 1 : 0);

        final FloatBuffer buffer = BufferUtils.createFloatBuffer(256);
        for (int i = 1; i <= radius.getNumberValue(); i++) {
            buffer.put(MathUtils.calculateGaussianValue(i, blurSize.getNumberValue()));
        }
        buffer.rewind();

        glUniform1(glowShader.getUniform("weights"), buffer);
        glowShader.setUniformf("force", 1);
    }


    public void setupOutlineUniforms(float dir1, float dir2) {
        Color color = getColor();
        outlineShader.setUniformi("texture", 0);
        outlineShader.setUniformf("radius", radius.getNumberValue() / 1.5f);
        outlineShader.setUniformf("texelSize", 1.0f / Minecraft.getScaledRoundedWidth(), 1.0f / Minecraft.getScaledRoundedHeight());
        outlineShader.setUniformf("direction", dir1, dir2);
        outlineShader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
    }

    public void renderEntities(float ticks) {
        entities.forEach(entity -> {
            renderNameTags = false;
            Helper.mc.getRenderManager().renderEntityStatic(entity, ticks, false);

            renderNameTags = true;
        });
    }

    private Color getColor() {
        return new Color(glowColor.getColorValue());

    }

    public void collectEntities() {
        entities.clear();
        for (Entity entity : Helper.mc.world.getLoadedEntityList()) {
            if (!DrawHelper.isInViewFrustrum(entity)) continue;
            if (entity == Helper.mc.player && Helper.mc.gameSettings.thirdPersonView == 0) continue;
            if (entity instanceof EntityPlayer) {
                entities.add(entity);
            }

        }
    }


}