package net.minecraft.client.shader;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;
import org.lwjgl.util.vector.Matrix4f;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class Shader
{
    private final ShaderManager manager;
    public final Framebuffer framebufferIn;
    public final Framebuffer framebufferOut;
    private final List<Object> listAuxFramebuffers = Lists.newArrayList();
    private final List<String> listAuxNames = Lists.newArrayList();
    private final List<Integer> listAuxWidths = Lists.newArrayList();
    private final List<Integer> listAuxHeights = Lists.newArrayList();
    private Matrix4f projectionMatrix;

    public Shader(IResourceManager resourceManager, String programName, Framebuffer framebufferInIn, Framebuffer framebufferOutIn) throws IOException
    {
        manager = new ShaderManager(resourceManager, programName);
        framebufferIn = framebufferInIn;
        framebufferOut = framebufferOutIn;
    }

    public void deleteShader()
    {
        manager.deleteShader();
    }

    public void addAuxFramebuffer(String auxName, Object auxFramebufferIn, int width, int height)
    {
        listAuxNames.add(listAuxNames.size(), auxName);
        listAuxFramebuffers.add(listAuxFramebuffers.size(), auxFramebufferIn);
        listAuxWidths.add(listAuxWidths.size(), Integer.valueOf(width));
        listAuxHeights.add(listAuxHeights.size(), Integer.valueOf(height));
    }

    private void preLoadShader()
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.disableFog();
        GlStateManager.disableLighting();
        GlStateManager.disableColorMaterial();
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(0);
    }

    public void setProjectionMatrix(Matrix4f projectionMatrixIn)
    {
        projectionMatrix = projectionMatrixIn;
    }

    public void loadShader(float p_148042_1_)
    {
        preLoadShader();
        framebufferIn.unbindFramebuffer();
        float f = (float) framebufferOut.framebufferTextureWidth;
        float f1 = (float) framebufferOut.framebufferTextureHeight;
        GlStateManager.viewport(0, 0, (int)f, (int)f1);
        manager.addSamplerTexture("DiffuseSampler", framebufferIn);

        for (int i = 0; i < listAuxFramebuffers.size(); ++i)
        {
            manager.addSamplerTexture(listAuxNames.get(i), listAuxFramebuffers.get(i));
            manager.getShaderUniformOrDefault("AuxSize" + i).set((float) listAuxWidths.get(i).intValue(), (float) listAuxHeights.get(i).intValue());
        }

        manager.getShaderUniformOrDefault("ProjMat").set(projectionMatrix);
        manager.getShaderUniformOrDefault("InSize").set((float) framebufferIn.framebufferTextureWidth, (float) framebufferIn.framebufferTextureHeight);
        manager.getShaderUniformOrDefault("OutSize").set(f, f1);
        manager.getShaderUniformOrDefault("Time").set(p_148042_1_);
        Minecraft minecraft = Minecraft.getMinecraft();
        manager.getShaderUniformOrDefault("ScreenSize").set((float) Minecraft.width, (float) Minecraft.height);
        manager.useShader();

        if (true) {

            //void var8_16;
            if (true) {
                //void var8_16;
                int oneColor = new Color(0,0,120).getRGB();
                int color = 0;
                String string = "Astolfo";
                int n = -1;
                switch (string) {
                    case "Client": {
                        if (!string.equals("Client")) break;
                        boolean bl = false;
                        break;
                    }
                    case "Custom": {
                        if (!string.equals("Custom")) break;
                        boolean bl = true;
                        break;
                    }
                    case "Astolfo": {
                        if (!string.equals("Astolfo")) break;
                        int n2 = 2;
                        break;
                    }
                    case "Rainbow": {
                        if (!string.equals("Rainbow")) break;
                        int n3 = 3;
                    }
                }
                switch (string) {
                    case "Client": {
                        color = ClientHelper.getClientColor().getRGB();
                        break;
                    }
                    case "Custom": {
                        color = oneColor;
                        break;
                    }
                    case "Astolfo": {
                        color = PaletteHelper.astolfo(5000.0f, 1).getRGB();
                        break;
                    }
                    case "Rainbow": {
                        color = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                    }
                }

            }
        }
        framebufferOut.framebufferClear();
        framebufferOut.bindFramebuffer(false);
        GlStateManager.depthMask(false);
        GlStateManager.colorMask(true, true, true, true);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(0.0D, f1, 500.0D).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(f, f1, 500.0D).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(f, 0.0D, 500.0D).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 500.0D).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.colorMask(true, true, true, true);
        manager.endShader();
        framebufferOut.unbindFramebuffer();
        framebufferIn.unbindFramebufferTexture();

        for (Object object : listAuxFramebuffers)
        {
            if (object instanceof Framebuffer)
            {
                ((Framebuffer)object).unbindFramebufferTexture();
            }
        }
    }

    public ShaderManager getShaderManager()
    {
        return manager;
    }
}
