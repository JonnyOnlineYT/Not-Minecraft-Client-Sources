//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.combat;

import black.nigger.wildclient.settings.Setting;
import black.nigger.wildclient.module.Category;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.settings.KeyBinding;
import black.nigger.wildclient.Wild;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import black.nigger.wildclient.module.Module;

public class AutoGApple extends Module
{
    private int oldSlot;
    private boolean eating;
    
    boolean isFood(final ItemStack itemStack) {
        return !isNullOrEmptyStack(itemStack) && itemStack.getItem() instanceof ItemAppleGold;
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        final int onHealth = (int)Wild.instance.settingsManager.getSettingByName(this, "OnHealth").getValDouble();
        if (AutoGApple.mc.player.getHealth() + AutoGApple.mc.player.getAbsorptionAmount() > (double)onHealth && this.eating) {
            this.eating = false;
            this.stop();
            return;
        }
        if (!this.canEat()) {
            return;
        }
        if (this.isFood(AutoGApple.mc.player.getHeldItemOffhand()) && AutoGApple.mc.player.getHealth() <= (double)onHealth && this.canEat()) {
            KeyBinding.setKeyBindState(AutoGApple.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            this.eating = true;
        }
        if (!this.canEat()) {
            this.stop();
        }
    }
    
    public static boolean isNullOrEmptyStack(final ItemStack itemStack) {
        return itemStack == null || itemStack.isEmpty();
    }
    
    public boolean canEat() {
        final Entity entity;
        return AutoGApple.mc.objectMouseOver == null || (!((entity = AutoGApple.mc.objectMouseOver.entityHit) instanceof EntityVillager) && !(entity instanceof EntityTameable));
    }
    
    void stop() {
        KeyBinding.setKeyBindState(AutoGApple.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
    }
    
    public AutoGApple() {
        super("AutoApple", "eat golden apples automatically", Category.PLAYER);
        this.oldSlot = -1;
        this.eating = false;
        Wild.instance.settingsManager.rSetting(new Setting("OnHealth", this, 12.0, 1.0, 20.0, true));
    }
}
