package org.spray.heaven.features.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.UpdateEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.main.Wrapper;

@ModuleInfo(name = "FastBreak", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class FastBreak extends Module {
    /*
    private final TimerHelper timerHelper = new TimerHelper();

    BooleanSetting FastFlockHit = new BooleanSetting("FastFlockHit", "", true);
    BooleanSetting Instant = new BooleanSetting("Instant", "", true);
    BooleanSetting Haste = new BooleanSetting("Haste", "", true);
    DoubleSetting SkeepValue = new DoubleSetting("SkeepValue", "",0.5F, 0.0F, 1.0F,0.1F);

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        mc.timer.timerSpeed = 1.0F;
        Minecraft var10000;
        if (this.Haste.getValue() && !this.Instant.getValue()) {
            if (mc.playerController.getIsHittingBlock()) {
                var10000 = mc;
                Minecraft.player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 1000, 1));
            }

            if (!mc.playerController.getIsHittingBlock()) {
                Wrapper.getPlayer().removeActivePotionEffect(Potion.getPotionById(3));
            }
        }

        if (this.FastFlockHit.getValue()) {
            mc.playerController.blockHitDelay = 0;
        }

        if (mc.playerController.curBlockDamageMP >= 1.0F - this.SkeepValue.getValue()) {
            mc.playerController.curBlockDamageMP = 1.0F;
            if (this.Haste.getValue() && this.Instant.getValue()) {
                if (!mc.playerController.getIsHittingBlock()) {
                    Wrapper.getPlayer().removeActivePotionEffect(Potion.getPotionById(3));
                }

                if (mc.playerController.getIsHittingBlock()) {
                    var10000 = mc;
                    Minecraft.player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 1000, 1));
                }
            }

            if (this.Instant.getValue()) {
                if (mc.playerController.getIsHittingBlock()) {
                    mc.timer.timerSpeed = 4.0193095207214355F;
                }

                if (mc.timer.timerSpeed == 4.0193095207214355D) {
                    MovementUtils.setSpeed(0.0D);
                    var10000 = mc;
                    Minecraft.player.onGround = false;
                    var10000 = mc;
                    Minecraft.player.jumpMovementFactor = 0.0F;
                }

                if (!mc.playerController.getIsHittingBlock() && this.timerHelper.hasReached(100.0D)) {
                    mc.timer.timerSpeed = 1.0F;
                    this.timerHelper.reset();
                }
            }
        }

    }

    public void onDisable() {
        if (this.Haste.getValue()) {
            Wrapper.getPlayer().removeActivePotionEffect(Potion.getPotionById(3));
        }

        if (this.Instant.getValue()) {
            mc.timer.timerSpeed = 1.0F;
        }

    }

     */
}
