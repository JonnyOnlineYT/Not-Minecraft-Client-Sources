package org.moonware.client.feature.impl.combat;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventAttackSilent;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.util.Random;

public class HitParticles extends Feature {

    private final NumberSetting particleMultiplier;
    private final ListSetting particleMode;
    private final Random random = new Random();

    public HitParticles() {
        super("DamageParticles", "При ударе спавнит партиклы вокруг сущности", Type.Combat);
        particleMode = new ListSetting("Particle Mode", "Spell", () -> true, "Spell", "Enchant", "Criticals", "Heart", "Flame", "HappyVillager", "AngryVillager", "Portal", "Redstone", "Cloud");
        particleMultiplier = new NumberSetting("Particle Multiplier", 5, 1, 15, 1, () -> true);
        addSettings(particleMode, particleMultiplier);
    }

    @EventTarget
    public void onAttackSilent(EventAttackSilent event) {
        String mode = particleMode.getOptions();
        if (mode.equalsIgnoreCase("Redstone")) {
            for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                for (int i2 = 0; i2 < particleMultiplier.getNumberValue(); i2++) {
                    Minecraft.effectRenderer.spawnEffectParticle(37, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F), Block.getIdFromBlock(Blocks.REDSTONE_BLOCK));
                }
            }
        } else if (mode.equalsIgnoreCase("Heart")) {
            for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                for (int i2 = 0; i2 < particleMultiplier.getNumberValue(); i2++) {
                    Minecraft.world.spawnParticle(EnumParticleTypes.HEART, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                }
            }
        } else if (mode.equalsIgnoreCase("Flame")) {
            for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                for (int i2 = 0; i2 < particleMultiplier.getNumberValue(); i2++) {
                    Minecraft.world.spawnParticle(EnumParticleTypes.FLAME, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                }
            }
        } else if (mode.equalsIgnoreCase("Cloud")) {
            for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                for (int i2 = 0; i2 < particleMultiplier.getNumberValue(); i2++) {
                    Minecraft.world.spawnParticle(EnumParticleTypes.CLOUD, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                }
            }
        } else if (mode.equalsIgnoreCase("HappyVillager")) {
            for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                for (int i2 = 0; i2 < particleMultiplier.getNumberValue(); i2++) {
                    Minecraft.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                }
            }
        } else if (mode.equalsIgnoreCase("AngryVillager")) {
            for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                for (int i2 = 0; i2 < particleMultiplier.getNumberValue(); i2++) {
                    Minecraft.world.spawnParticle(EnumParticleTypes.VILLAGER_ANGRY, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                }
            }
        } else if (mode.equalsIgnoreCase("Spell")) {
            for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                for (int i2 = 0; i2 < particleMultiplier.getNumberValue(); i2++) {
                    Minecraft.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                }
            }
        } else if (mode.equalsIgnoreCase("Portal")) {
            for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                for (int i2 = 0; i2 < particleMultiplier.getNumberValue(); i2++) {
                    Minecraft.world.spawnParticle(EnumParticleTypes.PORTAL, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                }
            }
        } else if (mode.equalsIgnoreCase("Enchant")) {
            for (int i2 = 0; i2 < particleMultiplier.getNumberValue(); i2++) {
                Minecraft.player.onEnchantmentCritical(event.getTargetEntity());
            }
        } else if (mode.equalsIgnoreCase("Criticals")) {
            for (int i2 = 0; i2 < particleMultiplier.getNumberValue(); i2++) {
                Minecraft.player.onCriticalHit(event.getTargetEntity());
            }
        }
    }
}
