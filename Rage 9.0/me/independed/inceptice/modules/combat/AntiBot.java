//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AntiBot extends Module {
      public static boolean checkArmor(EntityPlayer var0) {
            if ((238591413 >>> 2 ^ 10206182 ^ 2769072 ^ 416780885) != 0) {
                  ;
            }

            Iterable var10000 = var0.getArmorInventoryList();
            if (!"nefariousMoment".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            List var1 = (List)var10000;
            Item var2 = ((ItemStack)var1.get(((1747889160 << 4 | 1491173317) >> 4 | 882281846) ^ -37830658)).getItem();
            if ((859603435 >>> 4 >>> 3 >>> 2 >> 2 ^ 1272642071) != 0) {
                  ;
            }

            if (!var2.equals(Items.DIAMOND_BOOTS)) {
                  return (boolean)((14761993 ^ 12051672 ^ 3385295) >> 1 ^ 3309967);
            } else if (!((ItemStack)var1.get((0 ^ 1847032589 | 1714865106) & 463912497 ^ 170310160)).getItem().equals(Items.DIAMOND_LEGGINGS)) {
                  return (boolean)((37749762 ^ 20323569) << 1 >>> 4 ^ 7258910);
            } else if (!((ItemStack)var1.get((1 ^ 0) << 3 & 5 ^ 2)).getItem().equals(Items.DIAMOND_CHESTPLATE)) {
                  return (boolean)((1653714472 << 4 ^ 671704094 | 16889828) ^ 18069502);
            } else {
                  Object var3 = var1.get(((2 | 0) & 1) >>> 3 ^ 3);
                  if ((((1401119295 | 62600824) ^ 265042318) >> 4 >> 4 ^ 6059855) == 0) {
                        ;
                  }

                  return (boolean)(!((ItemStack)var3).getItem().equals(Items.DIAMOND_HELMET) ? (((1667519066 | 1060522511) ^ 1799229214) >> 3 | 42057182) ^ 42582014 : (0 & 161945042) << 2 >> 4 >> 2 ^ 1);
            }
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  if (((361301502 >>> 4 | 15183839) << 4 ^ 796204753) != 0) {
                        ;
                  }

                  List var2 = (List)mc.world.loadedEntityList.stream().filter((var0) -> {
                        int var10000;
                        if (var0 != mc.player) {
                              var10000 = (0 << 2 ^ 1092639107 | 1002290017) ^ 2076048354;
                        } else {
                              if (((1338340028 >> 2 | 328678500) >>> 2 ^ -81292473) != 0) {
                                    ;
                              }

                              var10000 = 256815849 >> 3 << 1 ^ 64203962;
                        }

                        return (boolean)var10000;
                  }).filter((var0) -> {
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please dont crack my plugin")) {
                              ;
                        }

                        int var10000;
                        if (!(var0 instanceof EntityEnderCrystal)) {
                              var10000 = 0 >> 2 ^ 2093847784 ^ 360857706 ^ 815758427 ^ 1506846936;
                        } else {
                              if (((311403712 ^ 158530441) >>> 3 >>> 3 ^ -1330142439) != 0) {
                                    ;
                              }

                              if (((82240819 ^ 31817926 ^ 45394839) >>> 1 ^ 64743217) == 0) {
                                    ;
                              }

                              var10000 = (1289922100 >> 2 & 225306389 ^ 14511151) << 4 ^ 526176928;
                        }

                        return (boolean)var10000;
                  }).filter((var0) -> {
                        if (!"intentMoment".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        return var0 instanceof EntityPlayer;
                  }).filter((var0) -> {
                        Minecraft var10000 = mc;
                        if (!"stringer is a good obfuscator".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        return (boolean)((double)var10000.player.getDistance(var0) <= 6.5D ? (0 >>> 3 & 1552752227) << 2 ^ 1 : (1891217719 >> 3 >>> 3 | 7026601) & 22423165 ^ 21112429);
                  }).collect(Collectors.toList());
                  Iterator var3 = var2.iterator();
                  if ((33603909 >>> 4 >> 2 << 1 ^ 1050122) == 0) {
                        ;
                  }

                  while(var3.hasNext()) {
                        Entity var4 = (Entity)var3.next();
                        if (var4 instanceof EntityPlayer) {
                              EntityPlayer var10000 = (EntityPlayer)var4;
                              if ((75497472 >>> 3 ^ 149874439) != 0) {
                                    ;
                              }

                              EntityPlayer var5 = var10000;
                              if (Math.abs(Math.abs(((float[])Fraerok2.getRotations(var5))[743696060 >> 2 >>> 1 << 3 ^ 743696056]) % 360.0F - Math.abs(mc.player.rotationYaw % 360.0F)) > 171.0F && var5.ticksExisted < (((28 | 14) & 5) >>> 4 ^ 30)) {
                                    Minecraft var6 = mc;
                                    if ((((1670311380 | 175648129) >> 2 | 425117798) << 2 ^ 1879043548) == 0) {
                                          ;
                                    }

                                    var6.world.removeEntity(var5);
                                    ChatUtil.sendChatMessage((new StringBuilder()).append(var5.getDisplayNameString()).append(" was removed like bot").toString());
                              }
                        }
                  }

                  if ((((1546200627 >>> 3 | 77471993) << 1 | 287476515) ^ 524209151) == 0) {
                        ;
                  }

            }
      }

      public static boolean isInTabList(EntityPlayer var0) {
            if (Minecraft.getMinecraft().isSingleplayer()) {
                  return (boolean)((0 >> 2 & 1487719289) << 2 ^ 188063345 ^ 188063344);
            } else {
                  NetHandlerPlayClient var10000 = Minecraft.getMinecraft().getConnection();
                  if (!"nefariousMoment".equals("idiot")) {
                        ;
                  }

                  Collection var4 = ((NetHandlerPlayClient)Objects.requireNonNull(var10000)).getPlayerInfoMap();
                  if (((1610691097 | 1296075773) >>> 4 ^ -182304555) != 0) {
                        ;
                  }

                  Iterator var5 = var4.iterator();
                  if (((1586302408 << 3 & 1265532073) << 4 ^ 109494272) == 0) {
                        ;
                  }

                  Iterator var1 = var5;

                  NetworkPlayerInfo var2;
                  do {
                        if (!var1.hasNext()) {
                              return (boolean)(1066050960 >>> 2 >> 3 ^ 23928133 ^ 9521257);
                        }

                        var2 = (NetworkPlayerInfo)var1.next();
                  } while(!var2.getGameProfile().getName().equalsIgnoreCase(var0.getName()));

                  return (boolean)((0 << 2 & 773970480 | 6151644) ^ 6151645);
            }
      }

      public AntiBot() {
            super("AntiBot", "removes bots from the world", (1293265256 | 6444971) & 439897598 ^ 7845345 ^ 138934283, Module.Category.COMBAT);
            if (!"idiot".equals("your mom your dad the one you never had")) {
                  ;
            }

      }
}
