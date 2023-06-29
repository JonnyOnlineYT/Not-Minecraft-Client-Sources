//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.lang.reflect.Field;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoShift extends Module {
      TimerUtil timer;
      public boolean shouldShifting;
      NumberSetting delayShifting;

      public AutoShift() {
            if (((492595660 << 2 << 1 >> 4 | 1273563627) ^ -1650228725) != 0) {
                  ;
            }

            super("AutoShift", "presses shift when you hit entity", (1081624874 >>> 4 >> 3 ^ 3131537 | 2446098) ^ 11500307, Module.Category.COMBAT);
            if ((76038144 ^ 55602062 ^ 29575784 ^ 102457830) == 0) {
                  ;
            }

            this.timer = new TimerUtil();
            NumberSetting var10001 = new NumberSetting("TimeShift", this, 150.0D, 30.0D, 500.0D, 10.0D);
            if (!"you're dogshit".equals("please go outside")) {
                  ;
            }

            this.delayShifting = var10001;
            this.shouldShifting = (boolean)(800 & 792 ^ 768);
            Setting[] var1 = new Setting[0 >> 3 >> 3 ^ 1];
            int var10003 = (1313993061 >>> 2 | 316535252) ^ 333315549;
            if ((1952430158 >>> 2 << 1 & 338742402 ^ -2084846820) != 0) {
                  ;
            }

            var1[var10003] = this.delayShifting;
            this.addSettings(var1);
      }

      public static void setPressed(KeyBinding var0, boolean var1) {
            try {
                  Field var10000 = var0.getClass().getDeclaredField("pressed");
                  if ((((919977039 >> 2 | 143247315) >>> 1 & 14343698 | 12438324) ^ 940865949) == 0) {
                  }

                  Field var2 = var10000;
                  var2.setAccessible((boolean)(((0 & 356730631) >> 1 >> 4 | 801410929) ^ 801410928));
                  var2.setBoolean(var0, var1);
            } catch (ReflectiveOperationException var3) {
                  throw new RuntimeException(var3);
            }

            if (!"idiot".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

      }

      @SubscribeEvent
      public void onPlayerAttack(AttackEntityEvent var1) {
            if (mc.player != null) {
                  Minecraft var10000 = Minecraft.getMinecraft();
                  if (((878067579 | 653545287) >> 4 & 35096470 & 28398662 ^ -1167064668) != 0) {
                        ;
                  }

                  KeyBinding.setKeyBindState(var10000.gameSettings.keyBindSneak.getKeyCode(), (boolean)((0 ^ 161856165) >>> 4 & 7556958 ^ 1198859));
                  this.shouldShifting = (boolean)((0 >>> 4 ^ 1505350959) >> 3 >> 2 ^ 237942 ^ 47083486);
                  this.timer.reset();
            }
      }

      public static void resetPressed(KeyBinding var0) {
            if ((741126144 >>> 3 << 3 ^ 741126144) == 0) {
                  ;
            }

            AutoShift.setPressed(var0, GameSettings.isKeyDown(var0));
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  if (this.timer.hasReached(this.delayShifting.getValue()) && mc.player.isSneaking()) {
                        if (((605028546 | 313931530) ^ -1615070258) != 0) {
                              ;
                        }

                        if (this.shouldShifting) {
                              int var10000 = Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode();
                              if ((((665288206 << 3 ^ 417711304) << 1 & 811715547 | 1984648) ^ -636923471) != 0) {
                                    ;
                              }

                              KeyBinding.setKeyBindState(var10000, (boolean)((1044338236 >>> 1 | 230616912) ^ 532672350));
                              this.shouldShifting = (boolean)(254439336 >> 1 >> 2 >>> 4 >> 4 ^ 124237);
                              this.timer.reset();
                              if (!"ape covered in human flesh".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                    ;
                              }
                        }
                  }

            }
      }
}
