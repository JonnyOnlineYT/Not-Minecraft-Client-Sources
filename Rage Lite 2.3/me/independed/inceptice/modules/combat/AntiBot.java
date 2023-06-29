//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import com.mojang.authlib.GameProfile;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AntiBot extends Module {
      public AntiBot() {
            super("AntiBot", "removes bots from the world", (1440303716 ^ 179156067) << 3 ^ -72880072, Module.Category.COMBAT);
      }

      public static boolean checkArmor(EntityPlayer var0) {
            List var10000 = (List)var0.getArmorInventoryList();
            if ((((744515003 << 4 | '\uf573') >>> 3 ^ 249089281) & 174994779 ^ 1096125024) != 0) {
                  ;
            }

            List var1 = var10000;
            if (!((ItemStack)var1.get(((1550589607 ^ 965918812) & 1320265139) >>> 1 ^ 576209497)).getItem().equals(Items.DIAMOND_BOOTS)) {
                  int var4 = (1394267409 >>> 3 & 82439648) << 2 ^ 25502336;
                  if ((104800759 >>> 2 << 1 ^ 454200777) != 0) {
                        ;
                  }

                  return (boolean)var4;
            } else {
                  int var10001 = (0 << 3 | 1202712372) >> 1 ^ 601356187;
                  if (((1786200254 << 2 >> 3 | 1559345982) ^ -33834113) == 0) {
                        ;
                  }

                  if (!((ItemStack)var1.get(var10001)).getItem().equals(Items.DIAMOND_LEGGINGS)) {
                        return (boolean)((2057998817 | 1674759700) << 1 ^ 1806435163 ^ -1671840591);
                  } else if (!((ItemStack)var1.get(((0 | 1426690573) ^ 450191201) & 554362197 ^ 17352006)).getItem().equals(Items.DIAMOND_CHESTPLATE)) {
                        return (boolean)((191521032 << 1 << 3 ^ 567602918) >> 4 ^ -109629466);
                  } else {
                        ItemStack var2 = (ItemStack)var1.get(2 >> 3 >>> 1 ^ 3);
                        if (!"minecraft".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        Item var3 = var2.getItem();
                        ItemArmor var5 = Items.DIAMOND_HELMET;
                        if (!"please go outside".equals("you're dogshit")) {
                              ;
                        }

                        return (boolean)(!var3.equals(var5) ? 466049994 >>> 4 >>> 2 ^ 7282031 : (0 ^ 153128975) << 1 ^ 246520732 ^ 485538691);
                  }
            }
      }

      public static boolean isInTabList(EntityPlayer var0) {
            if (Minecraft.getMinecraft().isSingleplayer()) {
                  return (boolean)(((0 ^ 469186848) & 378633887) << 1 ^ 623013889);
            } else {
                  Iterator var1 = ((NetHandlerPlayClient)Objects.requireNonNull(Minecraft.getMinecraft().getConnection())).getPlayerInfoMap().iterator();

                  while(var1.hasNext()) {
                        Object var10000 = var1.next();
                        if (((1604783297 | 1547247055) ^ 1112105216 ^ 470536405 ^ 33401882) == 0) {
                              ;
                        }

                        NetworkPlayerInfo var2 = (NetworkPlayerInfo)var10000;
                        GameProfile var4 = var2.getGameProfile();
                        if (((2040474646 << 3 | 1018447785) << 1 ^ 481758673) != 0) {
                              ;
                        }

                        boolean var5 = var4.getName().equalsIgnoreCase(var0.getName());
                        if ((27136 ^ 675896857) != 0) {
                              ;
                        }

                        if (var5) {
                              return (boolean)((0 | 705108407 | 460540554 | 35031812) ^ 997710782);
                        }

                        if (((1962725335 ^ 50941694) >> 4 ^ -604989776) != 0) {
                              ;
                        }
                  }

                  return (boolean)((753578590 << 2 ^ 950158452) >>> 3 & 275387160 ^ 274731264);
            }
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            Minecraft var10000 = mc;
            if ((17318145 >>> 1 & 1511695 & 148428 ^ 0) == 0) {
                  ;
            }

            if (var10000.player != null && mc.world != null) {
                  Object var6 = mc.world.loadedEntityList.stream().filter((var0) -> {
                        if (((145145350 >>> 4 | 5878539) ^ 196834990) != 0) {
                              ;
                        }

                        return (boolean)(var0 != mc.player ? (0 & 1688004229) >> 3 ^ 1 : ((2009182868 ^ 1432087504) >>> 2 | 1635326) ^ 146733055);
                  }).filter((var0) -> {
                        boolean var10000 = var0 instanceof EntityPlayer;
                        if (!"yo mama name maurice".equals("idiot")) {
                              ;
                        }

                        return var10000;
                  }).filter((var0) -> {
                        return (boolean)(mc.player.getDistance(var0) <= 10.0F ? (0 | 1449027610 | 329983263) ^ 1476357406 : ((2070111486 | 1059702706) >>> 3 ^ 145902284) << 3 ^ 989442456);
                  }).collect(Collectors.toList());
                  if (((1213433996 << 1 & 1593185851 | 245560734) ^ 291584928 ^ -1177270787) != 0) {
                        ;
                  }

                  List var2 = (List)var6;
                  Iterator var3 = var2.iterator();

                  while(var3.hasNext()) {
                        Entity var4 = (Entity)var3.next();
                        if (var4 instanceof EntityPlayer) {
                              EntityPlayer var5 = (EntityPlayer)var4;
                              if (var5.ticksExisted < (1 >>> 3 >> 3 ^ 2) && var5.getHealth() < 20.0F && var5.getHealth() > 20.0F) {
                                    if (((2556042 | 1673880) & 3871497 ^ 3365460 ^ 328024659) != 0) {
                                          ;
                                    }

                                    if (!var5.isDead) {
                                          mc.world.removeEntity(var5);
                                          ChatUtil.sendChatMessage((new StringBuilder()).append(var5.getDisplayNameString()).append(" was removed like bot").toString());
                                    }
                              }
                        }

                        if ((((720794858 ^ 684326568) >> 4 >> 3 >> 3 | 20987) ^ -892658554) != 0) {
                              ;
                        }
                  }

            }
      }
}
