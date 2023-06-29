//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class fraerok8 extends Module {
      public NumberSetting increase;

      public fraerok8() {
            if (((1107591010 ^ 143904072 | 30729958 | 1007274957) ^ 2145386479) == 0) {
                  ;
            }

            if ((((1120164589 ^ 256282129) & 1194445549) >> 2 ^ 289419323) == 0) {
                  ;
            }

            super("NoSlowdown", "you can run using items", 1503900163 << 1 >> 4 ^ -80447936, Module.Category.MOVEMENT);
            NumberSetting var10001 = new NumberSetting("Speed %", this, 100.0D, 0.0D, 100.0D, 1.0D);
            if (!"please go outside".equals("stop skidding")) {
                  ;
            }

            this.increase = var10001;
            Setting[] var1 = new Setting[((0 >> 2 | 2084570809) & 578632659) << 2 ^ -2130703803];
            var1[(797943158 >>> 4 << 3 >> 4 ^ 10477879) >> 3 ^ 3961731] = this.increase;
            this.addSettings(var1);
      }

      @SubscribeEvent
      public void onPushPlayer(InputUpdateEvent var1) {
            if ((1408325702 >> 4 & 49459571 ^ -1282049215) != 0) {
                  ;
            }

            if (mc.player != null && mc.world != null) {
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  if (mc.player.isHandActive() && !mc.player.isRiding()) {
                        if (((685770134 >> 1 | 22350258) ^ 359992315) == 0) {
                              ;
                        }

                        MovementInput var10000 = var1.getMovementInput();
                        double var10001 = (double)var10000.moveStrafe;
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("nefariousMoment")) {
                              ;
                        }

                        NumberSetting var10002 = this.increase;
                        if (!"please go outside".equals("you probably spell youre as your")) {
                              ;
                        }

                        double var2 = var10002.getValue();
                        if ((8470533 >>> 4 ^ 529408) == 0) {
                              ;
                        }

                        var10000.moveStrafe = (float)(var10001 * (var2 / 5.0D));
                        var10000 = var1.getMovementInput();
                        var10000.moveForward = (float)((double)var10000.moveForward * (this.increase.getValue() / 5.0D));
                  }
            }

            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("ape covered in human flesh")) {
                  ;
            }

      }
}
