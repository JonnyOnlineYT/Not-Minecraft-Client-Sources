package ua.apraxia.modules.impl.other;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventPreMotion;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.math.TimerUtility;

public class AutoPotion extends Module {

    public BooleanSetting onlyGround = new BooleanSetting("Only Ground", true);

    public BooleanSetting strenght = new BooleanSetting("Strenght", true);

    public BooleanSetting speed = new BooleanSetting("Speed", true);

    public BooleanSetting fire_resistance = new BooleanSetting("Fire Resistance", true);
    public SliderSetting delay = new SliderSetting("Throw Delay", 300.0F, 10.0F, 800.0F, 10.0F);

    private BooleanSetting hotberOnly = new BooleanSetting("Hotbar Only", true);

    public TimerUtility timerHelper = new TimerUtility();

    ItemStack held;

    public AutoPotion() {
        super("AutoPotion", Categories.Other);
        addSetting(this.hotberOnly, this.onlyGround, this.strenght, this.speed, this.fire_resistance);
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (this.timerHelper.hasReached(this.delay.value)) {
            if (this.strenght.value &&
                    !mc.player.isPotionActive(MobEffects.STRENGTH))
                throwPotion(5);
            if (this.speed.value &&
                    !mc.player.isPotionActive(MobEffects.SPEED))
                throwPotion(1);
            if (this.fire_resistance.value &&
                    !mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE))
                throwPotion(12);
            this.timerHelper.reset();
        }
    }

    private int getPotionIndexInv(int id) {
        for (int i = 0; i < 45; i++) {
            int index = (i < 9) ? (i + 36) : i;
            for (PotionEffect potion : PotionUtils.getEffectsFromStack(mc.player.inventory.getStackInSlot(index))) {
                if (potion.getPotion() == Potion.getPotionById(id) && mc.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION)
                    return index;
            }
        }
        return -1;
    }

    private int getPotionIndexHb(int id) {
        for (int i = 0; i < 9; i++) {
            for (PotionEffect potion : PotionUtils.getEffectsFromStack(mc.player.inventory.getStackInSlot(i))) {
                if (potion.getPotion() == Potion.getPotionById(id) && mc.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION)
                    return i;
            }
        }
        return -1;
    }

    private void throwPotion(int potionId) {
        if (this.onlyGround.value && !mc.player.onGround)
            return;
        int index = -1;
        if (getPotionIndexHb(potionId) == -1) {
            index = getPotionIndexInv(potionId);
        } else {
            index = getPotionIndexHb(potionId);
        }
        if (index == -1)
            return;
        if (!this.hotberOnly.value && index > 9) {
            mc.playerController.windowClick(0, index, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
            mc.playerController.windowClick(0, 44, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
            throwPot(index);
            mc.playerController.windowClick(0, 44, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
        } else {
            throwPot(index);
        }
    }

    void throwPot(int index) {
        mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange((index < 9) ? index : 8));
        mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(mc.player.rotationYaw, 90.0F, mc.player.onGround));
        mc.player.rotationPitchHead = 90.0F;
        mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(mc.player.inventory.currentItem));
    }

    enum Potions {
        STRENGTH, SPEED, FIRERES;
    }
}
