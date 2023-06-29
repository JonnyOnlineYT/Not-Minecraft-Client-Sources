//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Fraerok6 extends Module {
      public NumberSetting verticalSetting;
      public NumberSetting horizontalSetting;

      @SubscribeEvent
      public void onLivingUpdate(LivingUpdateEvent var1) {
            if (mc.player != null && mc.world != null) {
                  if (mc.player.hurtTime > 0 && mc.player.hurtTime == mc.player.maxHurtTime && !ModuleManager.getModuleByName("TargetStrafe").isToggled()) {
                        EntityPlayerSP var10000 = mc.player;
                        var10000.motionX *= (double)((float)this.horizontalSetting.getValue() / 100.0F);
                        var10000 = mc.player;
                        double var10001 = var10000.motionZ;
                        double var10002 = this.horizontalSetting.getValue();
                        if ((((1472002367 ^ 49636373 | 1240892308) >> 1 | 332385631) ^ 1073729503) == 0) {
                              ;
                        }

                        float var2 = (float)var10002;
                        if (((114198048 ^ 19045157) >> 1 ^ 66457218) == 0) {
                              ;
                        }

                        var10001 *= (double)(var2 / 100.0F);
                        if (!"please get a girlfriend and stop cracking plugins".equals("please go outside")) {
                              ;
                        }

                        var10000.motionZ = var10001;
                        var10000 = mc.player;
                        var10000.motionY *= (double)((float)this.verticalSetting.getValue() / 100.0F);
                  }

            }
      }

      public Fraerok6() {
            super("Velocity", "make your knockback less", 17718619 >>> 4 & 531393 ^ 449, Module.Category.COMBAT);
            if ((((1772968754 ^ 1005255174) >>> 4 | 54273313) ^ 121405235) == 0) {
                  ;
            }

            this.horizontalSetting = new NumberSetting("Horizontal", this, 0.0D, 0.0D, 100.0D, 1.0D);
            NumberSetting var10001 = new NumberSetting;
            if (!"ape covered in human flesh".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var10001.<init>("Vertical", this, 0.0D, 0.0D, 100.0D, 1.0D);
            this.verticalSetting = var10001;
            Setting[] var1 = new Setting[(0 << 1 ^ 1973099981) >>> 4 >> 4 ^ 7707423];
            var1[(1761780340 | 1585471883) >>> 4 >> 1 ^ 66852639] = this.horizontalSetting;
            var1[0 << 4 >>> 2 >> 3 ^ 1] = this.verticalSetting;
            this.addSettings(var1);
      }
}
