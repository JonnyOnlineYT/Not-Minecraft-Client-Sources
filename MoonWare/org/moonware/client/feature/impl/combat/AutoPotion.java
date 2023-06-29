package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class AutoPotion extends Feature {
    ItemStack held;

    public AutoPotion() {
        super("AutoPotion", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0431\u0440\u043e\u0441\u0430\u0435\u0442 Splash \u0437\u0435\u043b\u044c\u044f \u043d\u0430\u0445\u043e\u0434\u044f\u0449\u0438\u0435\u0441\u044f \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435", Type.Combat);
        addSettings();
    }
    public static long lastBuffTime;

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Aura.target != null && mc.player.getCooledAttackStrength(1) > 0.5f)
            return;
        boolean throwed = (!mc.player.isPotionActive(MobEffects.SPEED) && isPotionOnHotBar(Potions.SPEED)
        )
                || (!mc.player.isPotionActive(MobEffects.STRENGTH) && isPotionOnHotBar(Potions.STRENGTH)
        )
                || (!mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE) && isPotionOnHotBar(Potions.FIRERES)
        );
        if (mc.player.ticksExisted > 10 && throwed && timerHelper.hasReached(1000)) {
            Minecraft.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, 90, mc.player.onGround));
            if (!mc.player.isPotionActive(MobEffects.SPEED) && isPotionOnHotBar(Potions.SPEED)) {
                throwPotion(Potions.SPEED);
            }
            if (!mc.player.isPotionActive(MobEffects.STRENGTH) && isPotionOnHotBar(Potions.STRENGTH)) {
                throwPotion(Potions.STRENGTH);
            }
            if (!mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE) && isPotionOnHotBar(Potions.FIRERES)) {
                throwPotion(Potions.FIRERES);
            }
            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
            timerHelper.reset();
            lastBuffTime = System.currentTimeMillis();
        }
    }

    private int getPotionIndexInv(int id) {
        for (int i = 0; i < 45; i++) {
            int index = (i < 9) ? (i + 36) : i;
            for (PotionEffect potion : PotionUtils.getEffectsFromStack(Minecraft.player.inventory.getStackInSlot(index))) {
                if (potion.getPotion() == Potion.getPotionById(id) && Minecraft.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION)
                    return index;
            }
        }
        return -1;
    }

    private int getPotionIndexHb(int id) {
        for (int i = 0; i < 9; i++) {
            for (PotionEffect potion : PotionUtils.getEffectsFromStack(Minecraft.player.inventory.getStackInSlot(i))) {
                if (potion.getPotion() == Potion.getPotionById(id) && Minecraft.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION)
                    return i;
            }
        }
        return -1;
    }


    void throwPot(int index) {
        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange((index < 9) ? index : 8));
        Minecraft.player.connection.sendPacket(new CPacketPlayer.Rotation(Minecraft.player.rotationYaw, 90.0F, Minecraft.player.onGround));
        Minecraft.player.rotationPitchHead = 90.0F;
        Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
    }
    public void throwPotion(Potions potion) {
        int slot = getPotionSlot(potion);
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        mc.playerController.updateController();
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        mc.playerController.updateController();
    }

    public static int getPotionSlot(AutoPotion.Potions potion) {
        for (int i = 0; i < 9; ++i) {
            if (isStackPotion(mc.player.inventory.getStackInSlot(i), potion)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isPotionOnHotBar(AutoPotion.Potions potions) {
        return getPotionSlot(potions) != -1;
    }

    public static boolean isStackPotion(ItemStack stack, AutoPotion.Potions potion) {
        if (stack == null)
            return false;

        Item item = stack.getItem();

        if (item == Items.SPLASH_POTION) {
            int id = 0;

            switch (potion) {
                case STRENGTH: {
                    id = 5;
                    break;
                }
                case SPEED: {
                    id = 1;
                    break;
                }
                case FIRERES: {
                    id = 12;
                    break;
                }
                case HEAL: {
                    id = 6;
                }
            }

            for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
                if (effect.getPotion() == Potion.getPotionById(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    public enum Potions {
        STRENGTH, SPEED, FIRERES, HEAL
    }
}