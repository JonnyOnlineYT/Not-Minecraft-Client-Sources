/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package ru.fluger.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.ScaleUtils;

public class TextureEngine {
    private int x;
    private int y;
    private int width;
    private int height;
    private ResourceLocation texture;
    private final ScaleUtils scale;

    public TextureEngine(String path, ScaleUtils scale, int width, int height) {
        this.scale = scale;
        this.width = width;
        this.height = height;
        try {
            this.texture = new ResourceLocation(path);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public TextureEngine(ResourceLocation loc, ScaleUtils scale, int width, int height) {
        this.scale = scale;
        this.width = width;
        this.height = height;
        try {
            this.texture = loc;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void bind(int x, int y) {
        this.x = x;
        this.y = y;
        TextureEngine.bind((float)this.getX() + 5.0f, this.getY(), this.width / this.scale.getScale(), this.height / this.scale.getScale(), 1.0f, this.texture);
    }

    public void bind(int x, int y, float red, float green, float blue) {
        this.x = x;
        this.y = y;
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(red, green, blue, 1.0f);
        GlStateManager.enableTexture2D();
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
        TextureEngine.a((float)this.getX() + 5.0f, this.getY(), 0.0f, 0.0f, this.width / this.scale.getScale(), this.height / this.scale.getScale(), this.width / this.scale.getScale(), this.height / this.scale.getScale());
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
    }

    public static void bind(float f, float f2, float f3, float f4, float f5, ResourceLocation rs) {
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, f5);
        Minecraft.getMinecraft().getTextureManager().bindTexture(rs);
        TextureEngine.a(f, f2, 0.0f, 0.0f, f3, f4, f3, f4);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
    }

    public static void a(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = 1.0f / f7;
        float f10 = 1.0f / f8;
        Tessellator bly2 = Tessellator.getInstance();
        BufferBuilder ali2 = bly2.getBuffer();
        ali2.begin(7, DefaultVertexFormats.POSITION_TEX);
        ali2.pos(f, f2 + f6, 0.0).tex(f3 * f9, (f4 + f6) * f10).endVertex();
        ali2.pos(f + f5, f2 + f6, 0.0).tex((f3 + f5) * f9, (f4 + f6) * f10).endVertex();
        ali2.pos(f + f5, f2, 0.0).tex((f3 + f5) * f9, f4 * f10).endVertex();
        ali2.pos(f, f2, 0.0).tex(f3 * f9, f4 * f10).endVertex();
        bly2.draw();
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

