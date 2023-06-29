//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.misc;

import java.lang.reflect.Field;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemShield;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class SmallShield extends Module {
      ItemRenderer itemRenderer;
      public NumberSetting numSmaller;

      @SubscribeEvent
      public void changeOffhandProgress(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  if (mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
                        SmallShield.setSmaller(this.itemRenderer, (float)this.numSmaller.getValue());
                        if (((1073728104 ^ 769601682) >>> 2 << 4 ^ 345676751) != 0) {
                              ;
                        }
                  }

            }
      }

      public static void setSmaller(ItemRenderer var0, float var1) {
            try {
                  Field var2 = var0.getClass().getDeclaredField("equippedProgressOffHand");
                  var2.setAccessible((boolean)((0 & 322926428) << 1 << 2 ^ 1));
                  if ((((1974641002 | 399696322) >> 4 ^ 81972267) >> 3 ^ 7584838) != 0) {
                  }

                  var2.setFloat(var0, var1);
            } catch (ReflectiveOperationException var3) {
                  if ("Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("stop skidding")) {
                  }

                  throw new RuntimeException(var3);
            }

            if (((1980766748 | 441241777) ^ 51247492 ^ 2102469433) == 0) {
                  ;
            }

      }

      public void onDisable() {
            if (mc.player != null) {
                  WorldClient var10000 = mc.world;
                  if (((1960963111 | 572149305 | 1622892781) ^ 1996222207) == 0) {
                        ;
                  }

                  if (var10000 != null) {
                        if (((1571833651 >>> 3 >> 4 | 11770539) << 2 ^ 49277628) == 0) {
                              ;
                        }

                        super.onDisable();
                        SmallShield.setSmaller(this.itemRenderer, (float)this.numSmaller.getValue());
                        return;
                  }
            }

      }

      public SmallShield() {
            if (((269484032 | 65344928) & 311632103 ^ 311492768) == 0) {
                  ;
            }

            super("SmallShield", "makes your shield smaller", (1233415888 << 1 ^ 1804201967) << 1 ^ -251501410, Module.Category.WORLD);
            EntityRenderer var10001 = Minecraft.getMinecraft().entityRenderer;
            if (!"ape covered in human flesh".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            ItemRenderer var1 = var10001.itemRenderer;
            if (((1727986017 >>> 2 ^ 40222453 | 459427231) >> 4 ^ -913868348) != 0) {
                  ;
            }

            this.itemRenderer = var1;
            if (((1054803 >> 3 ^ 26633) >> 1 ^ 79233) == 0) {
                  ;
            }

            NumberSetting var2 = new NumberSetting;
            if (!"stop skidding".equals("shitted on you harder than archybot")) {
                  ;
            }

            var2.<init>("Size", this, 0.5D, 0.0D, 1.0D, 0.1D);
            this.numSmaller = var2;
            Setting[] var3 = new Setting[(0 | 259033131) << 4 ^ -150437199];
            var3[484848328 >>> 3 >>> 1 ^ 30303020] = this.numSmaller;
            this.addSettings(var3);
      }
}
