package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.render.RenderHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.lwjgl.opengl.GL11;
import org.moonware.client.feature.impl.combat.AntiBot;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DamageMarkers
        extends Feature {
    private final NumberSetting markerSize;
    private final NumberSetting ticksToRemove;
    private final BooleanSetting checkYourself;
    public ListSetting markerMode = new ListSetting("Marker Mode", "Text", () -> true, "Text", "Celestial");
    public ListSetting markerColorMode = new ListSetting("Marker Color Mode", "Damage", () -> markerMode.currentMode.equals("Text"), "Damage", "Client", "Custom");
    public ColorSetting markerColor = new ColorSetting("Marker Color", Color.RED.getRGB(), () -> markerColorMode.currentMode.equals("Custom") && markerMode.currentMode.equals("Text"));
    private final HashMap<Integer, Float> healthMap = new HashMap();
    private final ArrayList<Marker> particles = new ArrayList();

    public DamageMarkers() {
        super("DmgMarkers", "\u041e\u0442\u043e\u0431\u0440\u043e\u0436\u0430\u0435\u0442 \u0440\u0435\u0433\u0435\u043d\u0435\u0440\u0430\u0446\u0438\u044e/\u0434\u0430\u043c\u0430\u0433 \u0432\u0441\u0435\u0445 \u044d\u043d\u0442\u0438\u0442\u0438 \u0432\u043e\u043a\u0440\u0443\u0433", Type.Visuals);
        markerSize = new NumberSetting("Marker size", 0.5f, 0.1f, 3.0f, 0.1f, () -> true);
        ticksToRemove = new NumberSetting("Ticks to remove", 35.0f, 5.0f, 50.0f, 5.0f, () -> true);
        checkYourself = new BooleanSetting("Check Yourself", true, () -> true);
        addSettings(markerMode, markerColor, markerSize, ticksToRemove, checkYourself);
    }

    @Override
    public void onDisable() {
        particles.clear();
        healthMap.clear();
        super.onDisable();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onUpdate(EventUpdate event) {
        ArrayList<Marker> arrayList = particles;
        synchronized (arrayList) {
            for (Entity entity : Minecraft.world.loadedEntityList) {
                if (entity == null || Minecraft.player.getDistanceToEntity(entity) > 10.0f || entity.isDead || entity == Minecraft.player && checkYourself.getBoolValue() || !(entity instanceof EntityLivingBase)) continue;
                float lastHealth = healthMap.getOrDefault(entity.getEntityId(), Float.valueOf(((EntityLivingBase)entity).getMaxHealth())).floatValue();
                healthMap.put(entity.getEntityId(), Float.valueOf(((EntityLivingBase)entity).getHealth()));
                if (lastHealth == ((EntityLivingBase)entity).getHealth()) continue;
                particles.add(new Marker(entity, "" + BigDecimal.valueOf(Math.abs(lastHealth - ((EntityLivingBase)entity).getHealth())).setScale(1, 4), entity.posX - 0.5 + (double)new Random(System.currentTimeMillis()).nextInt(5) * 0.1, entity.getEntityBoundingBox().minY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2.0, entity.posZ - 0.5 + (double)new Random(System.currentTimeMillis() + 1L).nextInt(5) * 0.1));
            }
            ArrayList<Marker> needRemove = new ArrayList<Marker>();
            for (Marker marker : particles) {
                marker.ticks++;
                if (!((float)marker.ticks >= ticksToRemove.getNumberValue()) && !marker.getEntity().isDead) continue;
                needRemove.add(marker);
            }
            for (Marker marker : needRemove) {
                particles.remove(marker);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onRender3D(EventRender3D event) {
        ArrayList<Marker> arrayList = particles;
        synchronized (arrayList) {
            for (Marker marker : particles) {
                if (MoonWare.featureManager.getFeatureByClass(AntiBot.class).getState() && AntiBot.isBotPlayer.contains(marker.getEntity()) && (AntiBot.antiBotMode.currentMode.equals("Matrix New") || AntiBot.antiBotMode.currentMode.equals("Matrix"))) {
                    return;
                }
                RenderManager renderManager = Minecraft.getRenderManager();
                double size = markerMode.currentMode.equals("Text") ? (double)(markerSize.getNumberValue() / (float)marker.ticks * 2.0f) * 0.1 : (double) markerSize.getNumberValue() * 0.1;
                size = MathHelper.clamp(size, 0.03, markerSize.getNumberValue());
                double x = marker.posX - RenderManager.renderPosX;
                double y = marker.posY - RenderManager.renderPosY;
                double z = marker.posZ - RenderManager.renderPosZ;
                GlStateManager.pushMatrix();
                GlStateManager.enablePolygonOffset();
                GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
                GlStateManager.translate(x, y, z);
                GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                double textY = Minecraft.gameSettings.thirdPersonView == 2 ? -1.0 : 1.0;
                GlStateManager.rotate(renderManager.playerViewX, (float)textY, 0.0f, 0.0f);
                GlStateManager.scale(-size, -size, size);
                GL11.glDepthMask(false);
                int color = -1;
                int oneColor = markerColor.getColorValue();
                switch (markerColorMode.currentMode) {
                    case "Client": {
                        color = ClientHelper.getClientColor().getRGB();
                        break;
                    }
                    case "Custom": {
                        color = oneColor;
                        break;
                    }
                    case "Damage": {
                        boolean startWithGreen;
                        boolean startWithRed = marker.str.startsWith("5") || marker.str.startsWith("4") || marker.str.startsWith("6") || marker.str.startsWith("7") || marker.str.startsWith("8") || marker.str.startsWith("9") || marker.str.startsWith("10") || marker.str.startsWith("11") || marker.str.startsWith("12") || marker.str.startsWith("13") || marker.str.startsWith("14") || marker.str.startsWith("15") || marker.str.startsWith("16") || marker.str.startsWith("17") || marker.str.startsWith("18") || marker.str.startsWith("19") || marker.str.startsWith("20") || marker.str.startsWith("21") || marker.str.startsWith("22") || marker.str.startsWith("23") || marker.str.startsWith("24") || marker.str.startsWith("25") || marker.str.startsWith("26") || marker.str.startsWith("27") || marker.str.startsWith("28") || marker.str.startsWith("29") || marker.str.startsWith("30");
                        boolean startWithYellow = marker.str.startsWith("3");
                        boolean bl = startWithGreen = marker.str.startsWith("1") || marker.str.startsWith("2");
                        int n = startWithRed ? Color.RED.getRGB() : (startWithYellow ? Color.YELLOW.getRGB() : (color = startWithGreen ? Color.GREEN.getRGB() : Color.WHITE.getRGB()));
                    }
                }
                if (markerMode.currentMode.equals("Text")) {
                    Minecraft.font.drawStringWithShadow(marker.str, -((float) Minecraft.font.getStringWidth(marker.str) / 2.0f), -(Minecraft.font.height - 1), color);
                } else {
                    RenderHelper.drawImage(new Namespaced("moonware/icons/ban.png"), -((float) Minecraft.font.getStringWidth(marker.str) / 2.0f), -(Minecraft.font.height - 1), markerSize.getNumberValue() * 10.0f, markerSize.getNumberValue() * 10.0f, Color.WHITE);
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glDepthMask(true);
                GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
                GlStateManager.disablePolygonOffset();
                GlStateManager.popMatrix();
            }
        }
    }

    private class Marker {
        private Entity entity;
        private String str;
        private double posX;
        private double posY;
        private double posZ;
        private int ticks;

        public Marker(Entity entity, String str, double posX, double posY, double posZ) {
            this.entity = entity;
            this.str = str;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
        }

        public Entity getEntity() {
            return entity;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public double getPosX() {
            return posX;
        }

        public void setPosX(double posX) {
            this.posX = posX;
        }

        public double getPosY() {
            return posY;
        }

        public void setPosY(double posY) {
            this.posY = posY;
        }

        public double getPosZ() {
            return posZ;
        }

        public void setPosZ(double posZ) {
            this.posZ = posZ;
        }

        public int getTicks() {
            return ticks;
        }

        public void setTicks(int ticks) {
            this.ticks = ticks;
        }
    }
}
