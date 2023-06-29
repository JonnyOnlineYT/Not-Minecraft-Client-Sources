package ru.fluger.client.ui.drag.imp;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.Counter;
import ru.fluger.client.Fluger;
import ru.fluger.client.GifEngine;
import ru.fluger.client.Particles;
import ru.fluger.client.ShaderShell;
import ru.fluger.client.feature.impl.hud.TargetHUD;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.helpers.render.AnimationHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.RoundedUtil;
import ru.fluger.client.ui.drag.Drag;

public class TargetHud
extends Drag {
    public static TimerHelper thudTimer = new TimerHelper();
    private double scale = 0.0;
    private static EntityLivingBase curTarget = null;
    private float healthBarWidth;
    public static GifEngine particle_gif = new GifEngine(new ResourceLocation("nightmare/girls/meme.gif"), 60, 60);
    public List<Particles> particles = new ArrayList<Particles>();
    ResourceLocation rs = new ResourceLocation("nightmare/steve.png");
    Counter counter = new Counter();

    public TargetHud() {
        this.setWidth(153.0f);
        this.setHeight(42.0f);
        this.name = "TargetHud";
        ShaderShell.init();
    }

    @Override
    public void init() {
        this.x = 60.0f;
        this.y = 80.0f;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        float x = this.x;
        float y = this.y;
        if (!Fluger.instance.featureManager.getFeatureByClass(TargetHUD.class).state) {
            return;
        }
    }

    public static void drawTexturedModalRect(ResourceLocation location, double x, double y, int textureX, int textureY, double width, double height) {
        boolean alpha_test = GL11.glIsEnabled((int)3008);
        GL11.glEnable((int)3008);
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0.0).tex((float)textureX * 0.00390625f, (float)((double)textureY + height) * 0.00390625f).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0).tex((float)((double)textureX + width) * 0.00390625f, (float)((double)textureY + height) * 0.00390625f).endVertex();
        bufferbuilder.pos(x + width, y, 0.0).tex((float)((double)textureX + width) * 0.00390625f, (float)textureY * 0.00390625f).endVertex();
        bufferbuilder.pos(x, y, 0.0).tex((float)textureX * 0.00390625f, (float)textureY * 0.00390625f).endVertex();
        tessellator.draw();
        if (alpha_test) {
            GL11.glEnable((int)3008);
        } else {
            GL11.glDisable((int)3008);
        }
    }

    public static void drawTexturedRect(ResourceLocation location, double xStart, double yStart, double width, double height, double scale) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glScaled((double)scale, (double)scale, (double)scale);
        TargetHud.drawTexturedModalRect(location, xStart / scale, yStart / scale, 0, 0, width, height);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static float getRenderHurtTime(EntityLivingBase hurt) {
        return (float)hurt.hurtTime - (hurt.hurtTime != 0 ? Minecraft.getMinecraft().timer.renderPartialTicks : 0.0f);
    }

    public static float getHurtPercent(EntityLivingBase hurt) {
        return TargetHud.getRenderHurtTime(hurt) / 10.0f;
    }
}
