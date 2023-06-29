package org.moonware.client.feature.impl.combat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.event.events.impl.player.RespawnEvent;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DamageParticles extends Feature {


    public NumberSetting deleteAfter;

    private final Map<Integer, Float> hpData = Maps.newHashMap();
    private final List<Particle> particles = Lists.newArrayList();
    public static BooleanSetting glow = new BooleanSetting("Glow", true, () -> true);
    public static NumberSetting glowRange = new NumberSetting("Glow Range", 14, 0, 150,0.01F, () -> glow.getBoolValue());

    public DamageParticles() {
        super("DamageMarkers", "Отображает дамаг-партиклы при ударе", Type.Visuals);
        deleteAfter = new NumberSetting("Delete After", 7, 1, 20, 1, () -> true);
        addSettings(deleteAfter, glow, glowRange);
    }

    @EventTarget
    public void onRespawn(RespawnEvent event) {
        particles.clear();
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        for (Entity entity : Minecraft.world.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) entity;
                double lastHp = hpData.getOrDefault(ent.getEntityId(), ent.getMaxHealth());
                hpData.remove(entity.getEntityId());
                hpData.put(entity.getEntityId(), ent.getHealth());
                if (lastHp == ent.getHealth()) continue;
                Color color;
                if (lastHp > ent.getHealth()) {
                    color = Color.red;
                } else {
                    color = Color.GREEN;
                }
                Vec3d loc = new Vec3d(entity.posX + Math.random() * 0.5 * (Math.random() > 0.5 ? -1 : 1), entity.getEntityBoundingBox().minY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 0.5, entity.posZ + Math.random() * 0.5 * (Math.random() > 0.5 ? -1 : 1));
                double str = new BigDecimal(Math.abs(lastHp - ent.getHealth())).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                particles.add(new Particle("" + str, loc.xCoord, loc.yCoord, loc.zCoord, color));
            }
        }
    }

    @EventTarget
    public void onRender3d(EventRender3D e) {
        if (Helper.timerHelper.hasReached(deleteAfter.getNumberValue() * 300)) {
            particles.clear();
            Helper.timerHelper.reset();
        }
        if (!particles.isEmpty()) {
            for (Particle p : particles) {
                if (p != null) {
                    GlStateManager.pushMatrix();
                    GlStateManager.enablePolygonOffset();
                    GL11.glPolygonOffset(1,-1500000);
                    GlStateManager.doPolygonOffset(1, -1500000);
                    GlStateManager.translate(p.posX - RenderManager.renderPosX, p.posY - RenderManager.renderPosY, p.posZ - RenderManager.renderPosZ);
                    GlStateManager.rotate(-Minecraft.getRenderManager().playerViewY, 0, 1, 0);
                    GlStateManager.rotate(Minecraft.getRenderManager().playerViewX, Minecraft.gameSettings.thirdPersonView == 2 ? -1 : 1, 0, 0);
                    GlStateManager.scale(-0.03, -0.03, 0.03);
                    GL11.glDepthMask(false);
                    if (glow.getBoolValue()) {
                        RenderHelper2.renderBlurredShadow(p.color,(float) (-Minecraft.font.getStringWidth(p.str) * 0.5), -Minecraft.font.height + 1, Minecraft.font.getStringWidth(p.str) + 2, 6.0, (int) glowRange.getCurrentValue());
                    }
                    Minecraft.font.drawStringWithShadow(p.str, (float) (-Minecraft.font.getStringWidth(p.str) * 0.5), -Minecraft.font.height + 1, p.color.getRGB());
                    GL11.glColor4f(1, 1, 1, 1);
                    GL11.glDepthMask(true);
                    GlStateManager.doPolygonOffset(1, 1500000);
                    GlStateManager.disablePolygonOffset();
                    GlStateManager.resetColor();
                    GlStateManager.popMatrix();
                }
            }
        }
    }
    @SuppressWarnings("All")
    class Particle {
        public String str;
        public double posX, posY, posZ;
        public Color color;
        public int ticks;

        public Particle(String str, double posX, double posY, double posZ, Color color) {
            this.str = str;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.color = color;
            this.ticks = 0;
        }
    }
}
