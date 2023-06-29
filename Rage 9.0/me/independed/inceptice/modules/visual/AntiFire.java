//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiFire extends Module {
      public AntiFire() {
            super("AntiFire", "removes fucking fire", 298240 ^ 298240, Module.Category.RENDER);
      }

      @SubscribeEvent
      public void onClientTick(ClientTickEvent var1) {
            if (mc.player != null && !mc.player.isDead) {
                  Stream var10000 = mc.world.loadedEntityList.stream().filter((var0) -> {
                        if (((805013609 << 4 | 463142693) << 2 ^ -18088236) == 0) {
                              ;
                        }

                        return (boolean)(!var0.isDead ? (0 ^ 372069233) >> 3 ^ 46508655 : (1264225364 ^ 240657080) >>> 2 >>> 4 ^ 18090683);
                  });
                  Predicate var10001 = (var0) -> {
                        return var0 instanceof EntityPlayer;
                  };
                  if (!"idiot".equals("ape covered in human flesh")) {
                        ;
                  }

                  List var2 = (List)var10000.filter(var10001).collect(Collectors.toList());
                  if (((1092990554 | 819834105 | 1730618320) & 120875899 ^ 120859515) == 0) {
                        ;
                  }

                  Iterator var8 = var2.iterator();
                  if (((1905938445 | 1109380286 | 57341074) ^ 1946156223) == 0) {
                        ;
                  }

                  Iterator var3 = var8;

                  while(var3.hasNext()) {
                        Entity var4 = (Entity)var3.next();
                        EntityPlayer var5 = (EntityPlayer)var4;

                        try {
                              Field[] var9 = (Field[])Entity.class.getDeclaredFields();
                              if (((491388870 << 4 | 796828064 | 569370532) ^ -28) == 0) {
                                    ;
                              }

                              Field var10 = var9[(36 << 3 & 101) << 1 << 1 >>> 4 ^ 62];
                              if ((((1211117073 >>> 4 << 2 | 121811529) & 56392144) << 2 ^ 221308672) == 0) {
                                    ;
                              }

                              Field var6 = var10;
                              var6.setAccessible((boolean)((0 << 3 >>> 2 & 959744393 | 799096177) ^ 799096176));
                              if (((38275906 | 8629108) ^ -872107475) != 0) {
                                    ;
                              }

                              var6.setBoolean(var5, (boolean)((0 & 515601038 | 287949924) ^ 287949925));
                        } catch (Exception var7) {
                              if ((201353711 >> 1 ^ 2192558 ^ 308345738) == 0) {
                              }
                        }
                  }

            }
      }
}
