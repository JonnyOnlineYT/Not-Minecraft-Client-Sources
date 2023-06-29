package de.strafe.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

import static de.strafe.utils.RenderUtil.mc;
import static org.lwjgl.opengl.GL11.*;

public class Stencil {

    private static final Stencil INSTANCE = new Stencil();
    private final HashMap<Integer, StencilFunc> stencilFuncs = new HashMap<>();
    private int layers = 1;
    private boolean renderMask;

    public static Stencil getInstance() {
        MCStencil.checkSetupFBO();
        return INSTANCE;
    }

    public static void dispose() {
        glDisable(GL_STENCIL_TEST);
    }

    public static void erase(int ref) {
        glColorMask(true, true, true, true);
        glStencilFunc(GL_EQUAL, ref, 1);
        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
    }

    public static void write() {
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(false);
        checkSetupFBO(Minecraft.getMinecraft().getFramebuffer());
        glClear(GL_STENCIL_BUFFER_BIT);
        glEnable(GL_STENCIL_TEST);
        glStencilFunc(GL_ALWAYS, 1, 1);
        glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
        glColorMask(false, false, false, false);
    }

    public static void checkSetupFBO(Framebuffer framebuffer) {
        if (framebuffer == null) return;
        // Checks if screen has been resized or new FBO has been created
        if (framebuffer.depthBuffer > -1) {
            // Sets up the FBO with depth and stencil extensions (24/8 bit)
            setupFBO(framebuffer);
            // Reset the ID to prevent multiple FBO's
            framebuffer.depthBuffer = -1;
        }
    }

    public void startLayer() {
        if (this.layers == 1) {
            GL11.glClearStencil(0);
            GL11.glClear(1024);
        }
        GL11.glEnable(2960);
        this.layers++;
        if (this.layers > getMaximumLayers())
            this.layers = 1;
    }


    public static void erase(boolean invert) {
        glStencilFunc(invert ? GL_EQUAL : GL_NOTEQUAL, 1, 65535);
        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        glAlphaFunc(GL_GREATER, 0.0f);
    }

    public static void write(boolean renderClipLayer) {
        Stencil.checkSetupFBO();
        glClearStencil(0);
        glClear(GL_STENCIL_BUFFER_BIT);
        glEnable(GL_STENCIL_TEST);
        glStencilFunc(GL_ALWAYS, 1, 65535);
        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
        if (!renderClipLayer) {
            GlStateManager.colorMask(false, false, false, false);
        }
    }

    public static void checkSetupFBO() {
        Framebuffer fbo = mc.getFramebuffer();
        if (fbo != null && fbo.depthBuffer > -1) {
            Stencil.setupFBO(fbo);
            fbo.depthBuffer = -1;
        }
    }




    public void stopLayer() {
        if (this.layers == 1)
            return;
        this.layers--;
        if (this.layers == 1) {
            GL11.glDisable(2960);
        } else {
            StencilFunc lastStencilFunc = this.stencilFuncs.remove(Integer.valueOf(this.layers));
            if (lastStencilFunc != null)
                lastStencilFunc.use();
        }
    }

    public void clear() {
        GL11.glClearStencil(0);
        GL11.glClear(1024);
        this.stencilFuncs.clear();
        this.layers = 1;
    }

    public void setBuffer() {
        setStencilFunc(new StencilFunc(!this.renderMask ? 512 : 519, this.layers,
                getMaximumLayers(), 7681, 7680, 7680));
    }

    public void setBuffer(boolean set) {
        setStencilFunc(new StencilFunc(!this.renderMask ? 512 : 519, set ? this.layers : (this.layers - 1),

                getMaximumLayers(), 7681, 7681, 7681));
    }

    public void cropInside() {
        setStencilFunc(new StencilFunc(514, this.layers, getMaximumLayers(), 7680, 7680, 7680));
    }

    public void setStencilFunc(StencilFunc stencilFunc) {
        glStencilFunc(stencilFunc.func_func, stencilFunc.func_ref, stencilFunc.func_mask);
        GL11.glStencilOp(stencilFunc.op_fail, stencilFunc.op_zfail, stencilFunc.op_zpass);
        this.stencilFuncs.put(Integer.valueOf(this.layers), stencilFunc);
    }

    public int getLayer() {
        return this.layers;
    }

    public int getStencilBufferSize() {
        return GL11.glGetInteger(3415);
    }

    public int getMaximumLayers() {
        return (int) (Math.pow(2.0D, getStencilBufferSize()) - 1.0D);
    }

    public void createRect(double x, double y, double x2, double y2) {
        GL11.glBegin(7);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
    }

    public static class MCStencil {
        public static void checkSetupFBO() {
            Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
            if (fbo != null &&
                    fbo.depthBuffer > -1) {
                setupFBO(fbo);
                fbo.depthBuffer = -1;
            }
        }

        public static void setupFBO(Framebuffer fbo) {
            EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
            int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
            EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
            EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041,
                    Minecraft.displayWidth,
                    Minecraft.displayHeight);
            EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
            EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
        }
    }

    public class StencilFunc {

        public final int func_func;
        public final int func_ref;
        public final int func_mask;
        public final int op_fail;
        public final int op_zfail;
        public final int op_zpass;

        public StencilFunc(int func_func, int func_ref, int func_mask, int op_fail, int op_zfail, int op_zpass) {
            this.func_func = func_func;
            this.func_ref = func_ref;
            this.func_mask = func_mask;
            this.op_fail = op_fail;
            this.op_zfail = op_zfail;
            this.op_zpass = op_zpass;
        }

        public void use() {
            glStencilFunc(this.func_func, this.func_ref, this.func_mask);
            GL11.glStencilOp(this.op_fail, this.op_zfail, this.op_zpass);
        }
    }

    /**
     * Sets up the FBO with depth and stencil
     *
     * @param fbo Framebuffer
     */
    public static void setupFBO(Framebuffer fbo) {
        // Deletes old render buffer extensions such as depth
        // Args: Render Buffer ID
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
        // Generates a new render buffer ID for the depth and stencil extension
        int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        int stencil_texture_buffer_ID = EXTFramebufferObject.glGenFramebuffersEXT();
        // Binds new render buffer by ID
        // Args: Target (GL_RENDERBUFFER_EXT), ID
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
        // Adds the depth and stencil extension
        // Args: Target (GL_RENDERBUFFER_EXT), Extension (GL_DEPTH_STENCIL_EXT),
        // Width, Height
        EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT, Minecraft.displayWidth, Minecraft.displayHeight);
        // Adds the stencil attachment
        // Args: Target (GL_FRAMEBUFFER_EXT), Attachment
        // (GL_STENCIL_ATTACHMENT_EXT), Target (GL_RENDERBUFFER_EXT), ID
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
        // Adds the depth attachment
        // Args: Target (GL_FRAMEBUFFER_EXT), Attachment
        // (GL_DEPTH_ATTACHMENT_EXT), Target (GL_RENDERBUFFER_EXT), ID
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_texture_buffer_ID, 0);
    }
}