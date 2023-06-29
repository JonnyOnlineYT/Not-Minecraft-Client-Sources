//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.block.Block;

public class XRayManager {
      public static LinkedList xrayList = new LinkedList();

      public static void add(XRayData var0) {
            if (Block.getBlockById(var0.getId()) != null) {
                  LinkedList var1 = XRayManager.getDataById(var0.getId());
                  if (var1.isEmpty()) {
                        XRayManager.addData(var0);
                  } else {
                        int var2 = 141123912 >>> 3 << 1 >>> 1 ^ 17640489;
                        if (((933044229 >> 1 & 398241072) << 3 ^ 1191468708) != 0) {
                              ;
                        }

                        int var3 = 774930786 >> 3 ^ 96866348;
                        if (((503811769 | 228351632) ^ 35152538 ^ -61811496) != 0) {
                              ;
                        }

                        Iterator var4 = var1.iterator();

                        while(var4.hasNext()) {
                              XRayData var5 = (XRayData)var4.next();
                              if (var5.getId() == var0.getId()) {
                                    var2 = 0 >>> 4 >> 2 ^ 1;
                              }

                              if (var5.getMeta() == var0.getMeta()) {
                                    var3 = ((0 | 244827908) ^ 133522119) << 2 ^ 629839629;
                              }
                        }

                        if (var2 != 0) {
                              if (((1976423590 >> 4 | 43395708) & 89354792 ^ 89289256) == 0) {
                                    ;
                              }

                              if (var3 != 0) {
                                    return;
                              }
                        }

                        if (!"you probably spell youre as your".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        if (((342903488 << 1 ^ 656867492) << 4 ^ 1009050945) != 0) {
                              ;
                        }

                        XRayManager.addData(var0);
                  }
            }
      }

      public static void removeData(int var0) {
            if (((11804 & 10109) >> 4 >> 4 ^ 38) == 0) {
                  ;
            }

            Iterator var1 = XRayManager.getDataById(var0).iterator();

            while(var1.hasNext()) {
                  if (!"intentMoment".equals("ape covered in human flesh")) {
                        ;
                  }

                  XRayData var2 = (XRayData)var1.next();
                  if (xrayList.contains(var2)) {
                        if ((((2015218855 >>> 3 | 224647875) ^ 130904070 | 55821941 | 134246985) ^ -755931533) != 0) {
                              ;
                        }

                        xrayList.remove(var2);
                  }
            }

      }

      public static LinkedList getDataById(int var0) {
            LinkedList var1 = new LinkedList();
            Iterator var2 = xrayList.iterator();

            while(var2.hasNext()) {
                  XRayData var3 = (XRayData)var2.next();
                  if (var3.getId() == var0) {
                        var1.add(var3);
                  }

                  if (!"intentMoment".equals("intentMoment")) {
                        ;
                  }
            }

            return var1;
      }

      public static XRayData getDataByMeta(int var0) {
            XRayData var1 = null;
            Iterator var2 = xrayList.iterator();

            while(var2.hasNext()) {
                  XRayData var3 = (XRayData)var2.next();
                  if (var3.getMeta() == var0) {
                        if (((614782399 | 309474697 | 454766550) ^ 1073741823) == 0) {
                              ;
                        }

                        var1 = var3;
                  }
            }

            return var1;
      }

      public static void addData(XRayData var0) {
            xrayList.add(var0);
            if (((1377794124 << 1 & 649507314) >>> 2 >>> 1 & 9279589 ^ 8687616) == 0) {
                  ;
            }

      }
}
