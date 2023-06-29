//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.block.Block;

public class XRayManager {
      public static LinkedList xrayList;

      public static void add(XRayData var0) {
            if (Block.getBlockById(var0.getId()) != null) {
                  LinkedList var1 = XRayManager.getDataById(var0.getId());
                  if (var1.isEmpty()) {
                        XRayManager.addData(var0);
                  } else {
                        if ((1049458977 >>> 2 >>> 3 >> 1 ^ 16397796) == 0) {
                              ;
                        }

                        int var2 = 1073746048 >>> 4 >> 3 & 1177287 ^ 1;
                        int var3 = (33687712 | 4317933) ^ 38005485;
                        Iterator var4 = var1.iterator();
                        if (('邑' >> 4 ^ -996787731) != 0) {
                              ;
                        }

                        while(var4.hasNext()) {
                              XRayData var5 = (XRayData)var4.next();
                              if (var5.getId() == var0.getId()) {
                                    var2 = (0 >> 4 & 867173563 | 2083645586) >>> 2 << 3 ^ -127676127;
                              }

                              if (((524943631 | 199393593) << 4 & 1390527285 ^ 1851099295) != 0) {
                                    ;
                              }

                              if (var5.getMeta() == var0.getMeta()) {
                                    var3 = 0 >> 2 & 1381650017 ^ 933804267 ^ 933804266;
                              }
                        }

                        if (var2 != 0) {
                              if (((1049625688 >> 4 ^ 55934425 | 7871355) ^ -633659818) != 0) {
                                    ;
                              }

                              if (var3 != 0) {
                                    return;
                              }
                        }

                        if ((((667549288 << 2 & 384304083 | 28558529) & 45706248) << 4 ^ -1263030553) != 0) {
                              ;
                        }

                        XRayManager.addData(var0);
                  }
            }
      }

      public static XRayData getDataByMeta(int var0) {
            XRayData var1 = null;
            Iterator var2 = xrayList.iterator();

            while(true) {
                  if ((274739996 << 4 ^ 11647110 ^ 112363846) == 0) {
                        ;
                  }

                  if (!"minecraft".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  if (!var2.hasNext()) {
                        return var1;
                  }

                  XRayData var3 = (XRayData)var2.next();
                  int var10000 = var3.getMeta();
                  if (!"you're dogshit".equals("you're dogshit")) {
                        ;
                  }

                  if (var10000 == var0) {
                        var1 = var3;
                        if (((646134773 >>> 1 | 156767448) << 3 ^ 496849611 ^ -1632609740) != 0) {
                              ;
                        }
                  }
            }
      }

      static {
            LinkedList var10000 = new LinkedList();
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            xrayList = var10000;
            if ((278564 >>> 2 ^ 660935174) != 0) {
                  ;
            }

      }

      public static void removeData(int var0) {
            Iterator var1 = XRayManager.getDataById(var0).iterator();

            while(var1.hasNext()) {
                  Object var10000 = var1.next();
                  if ((((216282640 >> 1 | 84589635) & 21774769) >> 2 ^ 5374016) == 0) {
                        ;
                  }

                  XRayData var2 = (XRayData)var10000;
                  if (xrayList.contains(var2)) {
                        xrayList.remove(var2);
                  }
            }

      }

      public static void addData(XRayData var0) {
            if (((427330775 ^ 327301586 ^ 84699298 ^ 16061911 | 162598866) ^ 269838676) != 0) {
                  ;
            }

            LinkedList var10000 = xrayList;
            if ((((490903338 >>> 3 >> 3 ^ 784788) & 8058732) << 3 ^ 677527139) != 0) {
                  ;
            }

            var10000.add(var0);
      }

      public static LinkedList getDataById(int var0) {
            LinkedList var1 = new LinkedList();
            Iterator var2 = xrayList.iterator();

            while(true) {
                  if (((491220970 << 1 & 964775800) << 4 ^ -2012941056) != 0) {
                  }

                  if (!var2.hasNext()) {
                        return var1;
                  }

                  XRayData var3 = (XRayData)var2.next();
                  if (var3.getId() == var0) {
                        var1.add(var3);
                  }
            }
      }
}
