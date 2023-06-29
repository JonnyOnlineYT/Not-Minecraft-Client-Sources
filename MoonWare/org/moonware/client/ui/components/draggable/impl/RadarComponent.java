package org.moonware.client.ui.components.draggable.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Formatting;
import org.lwjgl.opengl.GL11;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.StencilUtil;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.helpers.Utils.MathUtils;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.render.GLUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.ui.components.draggable.HudComponent;
import org.moonware.client.utils.MWFont;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RadarComponent extends HudComponent {
    private final List<EntityLivingBase> entities = new ArrayList<>();

    public RadarComponent() {
        super(4, 34, 90, 90);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        setVisible(HUD.radar.get());
        super.draw(mouseX, mouseY, partialTick);
        if (Minecraft.gameSettings.showDebugInfo) return;
        getEntities();
        boolean isNurik = HUD.nurikMode.get();
        if(isNurik) {
            float size = 90.0F;
            int x = this.x + 4;
            int y = this.y + 4;
            int middleX = (int) (x + 4 + size / 2);
            int middleY = (int) (y + 4 + size / 2);
            RenderHelper2.drawRainbowRound(x, y, size, size + 5 - 10, 4, true, true, true, true, 2, 4);


            GlStateManager.pushMatrix();
//            GL11.glEnable(GL11.GL_SCISSOR_TEST);
//            RenderHelper2.scissorRect(x + 1, y + 20 - 3, x + size - 1, y + 22 - 5 + size - 33 + 10);
           StencilUtil.initStencilToWrite();
            RenderHelper2.drawRainbowRound(x, y, size, size + 5 - 10, 4, true, true, true, true, 2, 4);
            StencilUtil.readStencilBuffer(1);
            GLUtil.rotate(middleX, middleY, Minecraft.player.rotationYaw, () -> {
                for (EntityLivingBase entity : entities) {
                    double xDiff = MathUtils.interpolate(entity.prevPosX, entity.posX, Minecraft.timer.renderPartialTicks) - MathUtils.interpolate(Minecraft.player.prevPosX, Minecraft.player.posX, Minecraft.timer.renderPartialTicks);
                    double zDiff = MathUtils.interpolate(entity.prevPosZ, entity.posZ, Minecraft.timer.renderPartialTicks) - MathUtils.interpolate(Minecraft.player.prevPosZ, Minecraft.player.posZ, Minecraft.timer.renderPartialTicks);
                    if ((xDiff + zDiff) < (size / 2f)) {
                        float translatedX = (float) (middleX - xDiff);
                        float translatedY = (float) (middleY - zDiff);
                        RoundedUtil.drawRound(translatedX, translatedY, 3, 3, 1f, new Color(255, 255, 255, 255));
                        //Gui.drawRect2(translatedX, translatedY, 3, 3, -1);
                    }
                }
            });
            StencilUtil.uninitStencilBuffer();
//            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            MWFont.GREYCLIFFCF_MEDIUM.get(27).drawShadow(Formatting.BOLD + "Radar", x + 3, y + 7 - 5, -1);
            RectHelper.drawRect(x + 3, y + 20 - 5, x + size - 3, y + 21 - 5, 0x28FFFFFF);
            GlStateManager.popMatrix();
            this.width = 98;
            this.height = 94;
        }else{
            float size = HUD.radarSize.getCurrentValue();
            int x = this.x;
            int y = this.y;
            int middleX = (int) (x + size / 2);
            int middleY = (int) (y + size / 2);
            if (HUD.colored.get()) {
                HUD.nurikAlpha.setMaxValue(100);
                RenderHelper2.drawRainbowRound(x, y, size, size + 5 - 10, 4, true, true, true, true, 2, 4);
            }else{
                HUD.nurikAlpha.setMaxValue(255);
                RoundedUtil.drawRound(x, y, size, size + 5 - 10, 1.4f,new Color(31,31,31,HUD.nurikAlpha.getCurrentIntValue()));
                RenderHelper2.drawBlurredShadow(x, y, size, size + 5 - 10, 14,new Color(31,31,31,189));
                RoundedUtil.drawRound(x, y, size, 1, 0.1f,new Color(ClientHelper.getClientColor().getRGB()));
                RenderHelper2.drawRainbowRound(x, y, size, 0.11f, 0.01f, true, false, true, true, 2, 4);
//                GlowUtil.drawBlurredShadow(x + 1, y, size - 2, 0.11f, 14,new Color(-1),0);

            }
            //MWFont.GREYCLIFFCF_MEDIUM.get(27).drawShadow(Formatting.BOLD + "Radar", x + 3, y + 7 - 5, -1);
            //RectHelper.drawRect(x + 3, y + 20 - 5, x + size - 3, y + 21 - 5, 0x28FFFFFF);
            GlStateManager.pushMatrix();
            //ARenderHelper2.drawRainbowRound(x, y, size, size + 5 - 10, 4, false, false, true, true, 2, 4);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderHelper2.scissorRect(x + 1,y + 1,x + size - 1, y + size - 4);
            GLUtil.rotate(middleX, middleY, Minecraft.player.rotationYaw, () -> {
                for (EntityLivingBase entity : entities) {
                    double xDiff = MathUtils.interpolate(entity.prevPosX, entity.posX, Minecraft.timer.renderPartialTicks) - MathUtils.interpolate(Minecraft.player.prevPosX, Minecraft.player.posX, Minecraft.timer.renderPartialTicks);
                    double zDiff = MathUtils.interpolate(entity.prevPosZ, entity.posZ, Minecraft.timer.renderPartialTicks) - MathUtils.interpolate(Minecraft.player.prevPosZ, Minecraft.player.posZ, Minecraft.timer.renderPartialTicks);
                    if ((xDiff + zDiff) < (size / 2f) && !(entity == Minecraft.player)) {
                        float translatedX = (float) (middleX - xDiff);
                        float translatedY = (float) (middleY - zDiff);
                        RoundedUtil.drawRound(translatedX, translatedY, 3, 3, 1f, new Color(255, 255, 255, 255));
                        //Gui.drawRect2(translatedX, translatedY, 3, 3, -1);
                    }
                }
            });
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GlStateManager.popMatrix();
            this.width = (int) size;
            this.height = (int) size;
        }
    }

    public void getEntities() {
        entities.clear();
        for (Entity entity : Minecraft.world.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                if (entity instanceof EntityPlayer && entity != null && !entity.isInvisible()) {
                    entities.add((EntityLivingBase) entity);
                }
            }
        }
    }
}
