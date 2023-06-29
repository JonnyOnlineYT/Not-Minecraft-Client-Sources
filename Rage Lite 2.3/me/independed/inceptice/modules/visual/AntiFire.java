//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiFire extends Module {
      public AntiFire() {
            if (((570807740 << 2 << 4 | 1785743372) ^ 1122960156) != 0) {
                  ;
            }

            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("nefariousMoment")) {
                  ;
            }

            super("AntiFire", "removes fucking fire", ((2129002955 ^ 933557445 | 504480845) ^ 300469824) << 4 ^ -340545296, Module.Category.RENDER);
      }

      @SubscribeEvent
      public void onClientTick(ClientTickEvent var1) {
            if (mc.player != null && !mc.player.isDead) {
                  Stream var10000 = mc.world.loadedEntityList.stream().filter((var0) -> {
                        int var10000;
                        if (!var0.isDead) {
                              var10000 = ((0 & 1877928461 | 1092293974) & 233420364) >> 4 ^ 1085765;
                        } else {
                              var10000 = 718794867 >>> 2 >>> 2 >>> 4 ^ 2257144 ^ 567048;
                              if ((239310276 >>> 4 ^ 5874405 ^ -1951557380) != 0) {
                                    ;
                              }
                        }

                        return (boolean)var10000;
                  }).filter((var0) -> {
                        return var0 instanceof EntityPlayer;
                  });
                  if (((239447487 ^ 235474576 ^ 4395425) >> 3 ^ 128593) == 0) {
                        ;
                  }

                  List var2 = (List)var10000.collect(Collectors.toList());
                  Iterator var3 = var2.iterator();

                  while(var3.hasNext()) {
                        Object var8 = var3.next();
                        if (((298301420 ^ 196351572 | 217479490) ^ -736216674) != 0) {
                              ;
                        }

                        Entity var9 = (Entity)var8;
                        if ((315464 >> 2 ^ 78866) != 0) {
                        }

                        Entity var4 = var9;
                        EntityPlayer var5 = (EntityPlayer)var4;

                        try {
                              Field var6 = ((Field[])Entity.class.getDeclaredFields())[(10 >>> 3 | 0) << 2 ^ 50];
                              if ((314114083 << 2 << 2 >> 4 ^ -170378437) != 0) {
                                    ;
                              }

                              var6.setAccessible((boolean)((0 & 1407038963 | 418762418) ^ 418762419));
                              var6.setBoolean(var5, (boolean)(((0 ^ 1567823588) & 959967210 | 369931435) ^ 524269290));
                        } catch (Exception var7) {
                              if ((387407568 >>> 1 >>> 1 << 2 ^ 387407568) != 0) {
                              }
                        }

                        if ((1959096045 << 4 >>> 1 >> 1 ^ -2132909941) != 0) {
                              ;
                        }
                  }

            } else {
                  if ((924800861 >>> 1 << 4 & 826149932 ^ 809109536) == 0) {
                        ;
                  }

            }
      }
}
