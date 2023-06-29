package net.minecraft.client.shader;

import java.nio.IntBuffer;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class Framebuffer {
    public int framebufferTextureWidth;
    public int framebufferTextureHeight;
    public int framebufferWidth;
    public int framebufferHeight;
    public boolean useDepth;
    public int framebufferObject;
    public int framebufferTexture;
    public int depthBuffer;
    public float[] framebufferColor;
    public int framebufferFilter;

    public Framebuffer(int width, int height, boolean useDepthIn) {
        useDepth = useDepthIn;
        framebufferObject = -1;
        framebufferTexture = -1;
        depthBuffer = -1;
        framebufferColor = new float[4];
        framebufferColor[0] = 1.0F;
        framebufferColor[1] = 1.0F;
        framebufferColor[2] = 1.0F;
        framebufferColor[3] = 0.0F;
        createBindFramebuffer(width, height);
    }

    public void createBindFramebuffer(int width, int height) {
        if (!OpenGlHelper.isFramebufferEnabled()) {
            framebufferWidth = width;
            framebufferHeight = height;
        } else {
            GlStateManager.enableDepth();

            if (framebufferObject >= 0) {
                deleteFramebuffer();
            }

            createFramebuffer(width, height);
            checkFramebufferComplete();
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
        }
    }

    public void deleteFramebuffer() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            unbindFramebufferTexture();
            unbindFramebuffer();

            if (depthBuffer > -1) {
                OpenGlHelper.glDeleteRenderbuffers(depthBuffer);
                depthBuffer = -1;
            }

            if (framebufferTexture > -1) {
                TextureUtil.deleteTexture(framebufferTexture);
                framebufferTexture = -1;
            }

            if (framebufferObject > -1) {
                OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
                OpenGlHelper.glDeleteFramebuffers(framebufferObject);
                framebufferObject = -1;
            }
        }
    }

    public void createFramebuffer(int width, int height) {
        framebufferWidth = width;
        framebufferHeight = height;
        framebufferTextureWidth = width;
        framebufferTextureHeight = height;

        if (!OpenGlHelper.isFramebufferEnabled()) {
            framebufferClear();
        } else {
            framebufferObject = OpenGlHelper.glGenFramebuffers();
            framebufferTexture = TextureUtil.glGenTextures();

            if (useDepth) {
                depthBuffer = OpenGlHelper.glGenRenderbuffers();
            }

            setFramebufferFilter(9728);
            GlStateManager.bindTexture(framebufferTexture);
            GlStateManager.glTexImage2D(3553, 0, 32856, framebufferTextureWidth, framebufferTextureHeight, 0, 6408, 5121, null);
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, framebufferObject);
            OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_COLOR_ATTACHMENT0, 3553, framebufferTexture, 0);

            if (useDepth) {
                OpenGlHelper.glBindRenderbuffer(OpenGlHelper.GL_RENDERBUFFER, depthBuffer);
                OpenGlHelper.glRenderbufferStorage(OpenGlHelper.GL_RENDERBUFFER, 33190, framebufferTextureWidth, framebufferTextureHeight);
                OpenGlHelper.glFramebufferRenderbuffer(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_DEPTH_ATTACHMENT, OpenGlHelper.GL_RENDERBUFFER, depthBuffer);
            }

            framebufferClear();
            unbindFramebufferTexture();
        }
    }

    public void setFramebufferFilter(int framebufferFilterIn) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            framebufferFilter = framebufferFilterIn;
            GlStateManager.bindTexture(framebufferTexture);
            GlStateManager.glTexParameteri(3553, 10241, framebufferFilterIn);
            GlStateManager.glTexParameteri(3553, 10240, framebufferFilterIn);
            GlStateManager.glTexParameteri(3553, 10242, 10496);
            GlStateManager.glTexParameteri(3553, 10243, 10496);
            GlStateManager.bindTexture(0);
        }
    }

    public void checkFramebufferComplete() {
        int i = OpenGlHelper.glCheckFramebufferStatus(OpenGlHelper.GL_FRAMEBUFFER);

        if (i != OpenGlHelper.GL_FRAMEBUFFER_COMPLETE) {
            if (i == OpenGlHelper.GL_FB_INCOMPLETE_ATTACHMENT) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
            } else if (i == OpenGlHelper.GL_FB_INCOMPLETE_MISS_ATTACH) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
            } else if (i == OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
            } else if (i == OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
            } else {
                throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + i);
            }
        }
    }

    public void bindFramebufferTexture() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(framebufferTexture);
        }
    }

    public void unbindFramebufferTexture() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(0);
        }
    }

    public void bindFramebuffer(boolean p_147610_1_) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, framebufferObject);

            if (p_147610_1_) {
                GlStateManager.viewport(0, 0, framebufferWidth, framebufferHeight);
            }
        }
    }

    public void unbindFramebuffer() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
        }
    }

    public void setFramebufferColor(float red, float green, float blue, float alpha) {
        framebufferColor[0] = red;
        framebufferColor[1] = green;
        framebufferColor[2] = blue;
        framebufferColor[3] = alpha;
    }

    public void framebufferRender(int width, int height) {
        framebufferRenderExt(width, height, true);
    }

    public void framebufferRenderExt(int width, int height, boolean p_178038_3_) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.colorMask(true, true, true, false);
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, width, height, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            GlStateManager.viewport(0, 0, width, height);
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableAlpha();

            if (p_178038_3_) {
                GlStateManager.disableBlend();
                GlStateManager.enableColorMaterial();
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            bindFramebufferTexture();
            float f = (float) width;
            float f1 = (float) height;
            float f2 = (float) framebufferWidth / (float) framebufferTextureWidth;
            float f3 = (float) framebufferHeight / (float) framebufferTextureHeight;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(0.0D, f1, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(f, f1, 0.0D).tex(f2, 0.0D).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(f, 0.0D, 0.0D).tex(f2, f3).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, f3).color(255, 255, 255, 255).endVertex();
            tessellator.draw();
            unbindFramebufferTexture();
            GlStateManager.depthMask(true);
            GlStateManager.colorMask(true, true, true, true);
        }
    }

    public void framebufferClear() {
        bindFramebuffer(true);
        GlStateManager.clearColor(framebufferColor[0], framebufferColor[1], framebufferColor[2], framebufferColor[3]);
        int i = 16384;

        if (useDepth) {
            GlStateManager.clearDepth(1.0D);
            i |= 256;
        }

        GlStateManager.clear(i);
        unbindFramebuffer();
    }
}
