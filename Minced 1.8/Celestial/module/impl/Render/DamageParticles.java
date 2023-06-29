package Celestial.module.impl.Render;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.packet.EventAttackSilent;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.event.events.impl.player.RespawnEvent;
import Celestial.event.events.impl.render.EventRender3D;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.math.TimerHelper;
import Celestial.utils.render.RenderUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.icu.math.BigDecimal;
import Celestial.ui.settings.impl.ColorSetting;
import Celestial.ui.settings.impl.ListSetting;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DamageParticles extends Module {

    public TimerHelper timerHelper = new TimerHelper();
    public NumberSetting deleteAfter;
    public ColorSetting colorOnHit;
    public ColorSetting colorOnHeal;
    public static ListSetting particlesMode = new ListSetting("Particles Mode", "Text", () -> true, "Text", "UwU", "Particles");
    public ListSetting particleMode = new ListSetting("Particle Mode", "Spell", () -> particlesMode.currentMode.equals("Particles"), "Spell", "Enchant", "Criticals", "Heart", "Flame", "HappyVillager", "AngryVillager", "Portal", "Redstone", "Cloud");
    public NumberSetting particleMultiplier = new NumberSetting("Particle Multiplier", 5, 1, 15, 1, () -> particlesMode.currentMode.equals("Particles"));
    private final Random random = new Random();

    private final Map<Integer, Float> hpData = Maps.newHashMap();
    private final List<Particle> particles = Lists.newArrayList();

    public DamageParticles() {
        super("DamageParticles", "Партиклы после нанесения урона", ModuleCategory.Render);
        deleteAfter = new NumberSetting("Delete After", 7, 1, 20, 1, () -> particlesMode.currentMode.equals("Text"));
        colorOnHit = new ColorSetting("Color on Hit", Color.RED.getRGB(), () -> particlesMode.currentMode.equals("Text") || particlesMode.currentMode.equals("UwU"));
        colorOnHeal = new ColorSetting("Color on Heal", Color.GREEN.getRGB(), () -> particlesMode.currentMode.equals("Text") || particlesMode.currentMode.equals("UwU"));

        addSettings(particlesMode, particleMode, particleMultiplier, deleteAfter, colorOnHit, colorOnHeal);
    }

    @EventTarget
    public void onRespawn(RespawnEvent event) {
        if (particlesMode.currentMode.equals("Text") || particlesMode.currentMode.equals("UwU")) {
            particles.clear();
        }
    }

    @EventTarget
    public void onAttackSilent(EventAttackSilent event) {
        String mode = particleMode.getOptions();
        if (particlesMode.currentMode.equals("Particles")) {
            if (mode.equalsIgnoreCase("Redstone")) {
                for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                    for (int i2 = 0; i2 < particleMultiplier.getCurrentValue(); i2++) {
                        Helper.mc.effectRenderer.spawnEffectParticle(37, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F), Block.getIdFromBlock(Blocks.REDSTONE_BLOCK));
                    }
                }
            } else if (mode.equalsIgnoreCase("Heart")) {
                for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                    for (int i2 = 0; i2 < particleMultiplier.getCurrentValue(); i2++) {
                        Helper.mc.world.spawnParticle(EnumParticleTypes.HEART, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                    }
                }
            } else if (mode.equalsIgnoreCase("Flame")) {
                for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                    for (int i2 = 0; i2 < particleMultiplier.getCurrentValue(); i2++) {
                        Helper.mc.world.spawnParticle(EnumParticleTypes.FLAME, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                    }
                }
            } else if (mode.equalsIgnoreCase("Cloud")) {
                for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                    for (int i2 = 0; i2 < particleMultiplier.getCurrentValue(); i2++) {
                        Helper.mc.world.spawnParticle(EnumParticleTypes.CLOUD, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                    }
                }
            } else if (mode.equalsIgnoreCase("HappyVillager")) {
                for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                    for (int i2 = 0; i2 < particleMultiplier.getCurrentValue(); i2++) {
                        Helper.mc.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                    }
                }
            } else if (mode.equalsIgnoreCase("AngryVillager")) {
                for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                    for (int i2 = 0; i2 < particleMultiplier.getCurrentValue(); i2++) {
                        Helper.mc.world.spawnParticle(EnumParticleTypes.VILLAGER_ANGRY, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                    }
                }
            } else if (mode.equalsIgnoreCase("Spell")) {
                for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                    for (int i2 = 0; i2 < particleMultiplier.getCurrentValue(); i2++) {
                        Helper.mc.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                    }
                }
            } else if (mode.equalsIgnoreCase("Portal")) {
                for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                    for (int i2 = 0; i2 < particleMultiplier.getCurrentValue(); i2++) {
                        Helper.mc.world.spawnParticle(EnumParticleTypes.PORTAL, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                    }
                }
            } else if (mode.equalsIgnoreCase("Enchant")) {
                for (int i2 = 0; i2 < particleMultiplier.getCurrentValue(); i2++) {
                    Helper.mc.player.onEnchantmentCritical(event.getTargetEntity());
                }
            } else if (mode.equalsIgnoreCase("Criticals")) {
                for (int i2 = 0; i2 < particleMultiplier.getCurrentValue(); i2++) {
                    Helper.mc.player.onCriticalHit(event.getTargetEntity());
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (particlesMode.currentMode.equals("Text") || particlesMode.currentMode.equals("UwU")) {
            for (Entity entity : Helper.mc.world.loadedEntityList) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase ent = (EntityLivingBase) entity;
                    final double lastHp = hpData.getOrDefault(ent.getEntityId(), ent.getMaxHealth());
                    hpData.remove(entity.getEntityId());
                    hpData.put(entity.getEntityId(), ent.getHealth());
                    if (lastHp == ent.getHealth()) continue;
                    Color color;
                    if (lastHp > ent.getHealth()) {
                        color = new Color(colorOnHit.getColorValue());
                    } else {
                        color = new Color(colorOnHeal.getColorValue());
                    }
                    Vec3d loc = new Vec3d(entity.posX + Math.random() * 0.5 * (Math.random() > 0.5 ? -1 : 1), entity.getEntityBoundingBox().minY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 0.5, entity.posZ + Math.random() * 0.5 * (Math.random() > 0.5 ? -1 : 1));
                    double str = new BigDecimal(Math.abs(lastHp - ent.getHealth())).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    particles.add(new Particle("" + str, loc.x, loc.y, loc.z, color));
                }
            }
        }
    }

    @EventTarget
    public void onRender3d(EventRender3D e) {
        if (particlesMode.currentMode.equals("Text")) {
            if (timerHelper.hasReached(deleteAfter.getCurrentValue() * 300)) {
                particles.clear();
                timerHelper.reset();
            }
            if (!particles.isEmpty()) {
                for (Particle p : particles) {
                    if (p != null) {
                        GlStateManager.pushMatrix();
                        GlStateManager.enablePolygonOffset();
                        GlStateManager.doPolygonOffset(1, -1500000);
                        GlStateManager.translate(p.posX - Helper.mc.getRenderManager().renderPosX, p.posY - Helper.mc.getRenderManager().renderPosY, p.posZ - Helper.mc.getRenderManager().renderPosZ);
                        GlStateManager.rotate(-Helper.mc.getRenderManager().playerViewY, 0, 1, 0);
                        GlStateManager.rotate(Helper.mc.getRenderManager().playerViewX, Helper.mc.gameSettings.thirdPersonView == 2 ? -1 : 1, 0, 0);
                        GlStateManager.scale(-0.03, -0.03, 0.03);
                        GL11.glDepthMask(false);
                           Helper.mc.fontRendererObj.drawBlurredString(p.str, (float) (-Helper.mc.fontRendererObj.getStringWidth(p.str) * 0.5), -Helper.mc.fontRendererObj.FONT_HEIGHT + 1, 8, RenderUtils.injectAlpha(p.color, 100), p.color.getRGB());
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
    }

    @EventTarget
    public void onRender(EventRender3D e) {
        if (particlesMode.currentMode.equals("UwU")) {
            if (timerHelper.hasReached(deleteAfter.getCurrentValue() * 300)) {
                particles.clear();
                timerHelper.reset();
            }
            if (!particles.isEmpty()) {
                for (Particle p : particles) {
                    if (p != null) {
                        GlStateManager.pushMatrix();
                        GlStateManager.enablePolygonOffset();
                        GlStateManager.doPolygonOffset(1, -1500000);
                        GlStateManager.translate(p.posX - Helper.mc.getRenderManager().renderPosX, p.posY - Helper.mc.getRenderManager().renderPosY, p.posZ - Helper.mc.getRenderManager().renderPosZ);
                        GlStateManager.rotate(-Helper.mc.getRenderManager().playerViewY, 0, 1, 0);
                        GlStateManager.rotate(Helper.mc.getRenderManager().playerViewX, Helper.mc.gameSettings.thirdPersonView == 2 ? -1 : 1, 0, 0);
                        GlStateManager.scale(-0.03, -0.03, 0.03);
                        GL11.glDepthMask(false);
                        Helper.mc.fontRendererObj.drawBlurredString("UwU", (float) (-Helper.mc.fontRendererObj.getStringWidth(p.str) * 0.5), -Helper.mc.fontRendererObj.FONT_HEIGHT + 1, 8, RenderUtils.injectAlpha(p.color, 100), p.color.getRGB());
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