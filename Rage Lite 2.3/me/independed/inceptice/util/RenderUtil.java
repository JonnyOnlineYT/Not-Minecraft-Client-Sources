//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class RenderUtil {
      public static void drawESPTracers(List var0) {
            Vec3d var10000 = new Vec3d;
            double var10002 = Minecraft.getMinecraft().getRenderManager().viewerPosX;
            Minecraft var10003 = Minecraft.getMinecraft();
            if ((135561492 << 2 & 462757500 & 74070 ^ -1035866718) != 0) {
                  ;
            }

            double var11 = var10003.getRenderManager().viewerPosY;
            Minecraft var10004 = Minecraft.getMinecraft();
            if (((1068144748 >> 1 ^ 523084769 | 3959401) >>> 1 ^ 8288255) == 0) {
                  ;
            }

            double var12 = (double)var10004.player.getEyeHeight();
            if (!"you're dogshit".equals("shitted on you harder than archybot")) {
                  ;
            }

            var10000.<init>(var10002, var11 + var12, Minecraft.getMinecraft().getRenderManager().viewerPosZ);
            Vec3d var1 = var10000.add(Minecraft.getMinecraft().player.getLookVec());
            GL11.glLineWidth(2.0F);
            GL11.glBegin(0 << 3 >> 1 ^ 1);
            Iterator var2 = var0.iterator();

            while(true) {
                  if (((812770807 ^ 46946472) >> 4 ^ 1862010123) == 0) {
                  }

                  if (!var2.hasNext()) {
                        GL11.glEnd();
                        if (((955933627 ^ 375587488) >> 4 >>> 3 ^ 2036020669) != 0) {
                              ;
                        }

                        if (!"nefariousMoment".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        return;
                  }

                  Object var6 = var2.next();
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("stop skidding")) {
                        ;
                  }

                  Entity var7 = (Entity)var6;
                  if (!"you're dogshit".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  Entity var3 = var7;
                  AxisAlignedBB var8 = var3.getEntityBoundingBox();
                  if (((926568055 ^ 725552522 ^ 195680224) >>> 2 ^ 99296519) == 0) {
                        ;
                  }

                  Vec3d var4 = var8.getCenter();
                  if (var3 instanceof EntityItem) {
                        GL11.glColor4f(0.5F, 0.5F, 1.0F, 0.5F);
                  } else {
                        if (((121941870 >> 1 | 30355555) ^ 66025463) == 0) {
                              ;
                        }

                        Minecraft var9 = Minecraft.getMinecraft();
                        if ((134484032 ^ 102278522 ^ 15016269 ^ 52750525 ^ 1361835987) != 0) {
                              ;
                        }

                        float var5 = var9.player.getDistance(var3) / 20.0F;
                        GL11.glColor4f(2.0F - var5, var5, 0.0F, 0.5F);
                  }

                  double var10 = var1.x;
                  if (!"please get a girlfriend and stop cracking plugins".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  GL11.glVertex3d(var10, var1.y, var1.z);
                  if (((589887110 >> 3 | 46714301 | 68331878) << 3 ^ 452166195) != 0) {
                        ;
                  }

                  var10 = var4.x;
                  double var10001 = var4.y;
                  if (((1325033946 | 113867904 | 315944055) & 25139462 ^ 1393866321) != 0) {
                        ;
                  }

                  GL11.glVertex3d(var10, var10001, var4.z);
            }
      }

      public static void drawOutlinedBox(AxisAlignedBB var0) {
            GL11.glBegin(0 & 2053314928 ^ 1896867087 ^ 1896867086);
            double var10000 = var0.minX;
            if ((169889800 >> 4 & 7671521 ^ 221873937) != 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.maxY, var0.minZ);
            var10000 = var0.maxX;
            double var10001 = var0.maxY;
            if ((41775338 << 1 >>> 1 << 4 ^ 668405408) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            var10000 = var0.minX;
            var10001 = var0.maxY;
            if ((1367172258 >>> 4 << 2 ^ 341793064) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.maxZ);
            var10000 = var0.minX;
            if (!"i hope you catch fire ngl".equals("nefariousMoment")) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.minX, var0.minY, var0.minZ);
            var10000 = var0.maxX;
            if (((647812997 ^ 421242882) & 544398782 ^ 537025926) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.minZ);
            var10000 = var0.maxX;
            var10001 = var0.minY;
            double var10002 = var0.maxZ;
            if (!"intentMoment".equals("stop skidding")) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var10002);
            if ((757499254 << 3 & 261455721 ^ 1372082379) != 0) {
                  ;
            }

            var10000 = var0.maxX;
            var10001 = var0.minY;
            var10002 = var0.maxZ;
            if (!"stringer is a good obfuscator".equals("please take a shower")) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var10002);
            var10000 = var0.minX;
            if (((211961217 >> 4 | 10640809 | 2378330) ^ 536497519) != 0) {
                  ;
            }

            var10001 = var0.minY;
            if (((((669275401 | 10169506) & 417739622) >> 3 | 1380588) ^ 859401717) != 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.minY, var0.maxZ);
            var10000 = var0.minX;
            var10001 = var0.minY;
            if (((1990542809 << 2 ^ 324845210 ^ 1737114264) << 2 ^ -1190154856) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.minZ);
            GL11.glVertex3d(var0.minX, var0.minY, var0.minZ);
            var10000 = var0.minX;
            if (((1751659814 >> 3 | 35829578) ^ 254785518) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.maxY, var0.minZ);
            if (((1078125082 ^ 360832876 ^ 623949809) & 1826027725 ^ 1624410245) == 0) {
                  ;
            }

            var10000 = var0.maxX;
            if (!"you probably spell youre as your".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.maxZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.minY, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.maxZ);
            GL11.glEnd();
      }

      public static void drawESPBoxes(List var0, int var1, float var2) {
            GL11.glLineWidth(2.0F);
            Iterator var3 = var0.iterator();

            while(var3.hasNext()) {
                  Entity var4 = (Entity)var3.next();
                  GL11.glPushMatrix();
                  Vec3d var10000 = EntityUtil.getInterpolatedPos(var4, var2);
                  if (!"minecraft".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  Vec3d var5 = var10000;
                  GL11.glTranslated(var5.x, var5.y, var5.z);
                  double var7 = (double)var4.width + 0.1D;
                  double var10001 = (double)var4.height + 0.1D;
                  if (!"nefariousMoment".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  GL11.glScaled(var7, var10001, (double)var4.width + 0.1D);
                  if (var4 instanceof EntityItem) {
                        if (!"nefariousMoment".equals("you probably spell youre as your")) {
                              ;
                        }

                        GL11.glColor4f(0.5F, 0.5F, 1.0F, 0.5F);
                  } else {
                        float var6 = Minecraft.getMinecraft().player.getDistance(var4) / 20.0F;
                        GL11.glColor4f(2.0F - var6, var6, 0.0F, 0.5F);
                  }

                  if (((1076137077 | 329713187 | 960576917) >>> 3 ^ 259848190) == 0) {
                        ;
                  }

                  GL11.glCallList(var1);
                  GL11.glPopMatrix();
            }

      }

      public static void drawSolidBox(AxisAlignedBB var0) {
            GL11.glBegin(((5 & 0 | 1229249418) & 75445686) << 4 ^ 71374887);
            double var10000 = var0.minX;
            double var10001 = var0.minY;
            if (!"your mom your dad the one you never had".equals("intentMoment")) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.minZ);
            if (((9440386 << 2 ^ 4804712 | 33932850) ^ 589525405) != 0) {
                  ;
            }

            var10000 = var0.minX;
            var10001 = var0.maxY;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you're dogshit")) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.minZ);
            var10000 = var0.maxX;
            if (((455253036 ^ 28795835) >> 1 >> 2 ^ 55754738) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.minY, var0.maxZ);
            if (!"you probably spell youre as your".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            GL11.glVertex3d(var0.maxX, var0.minY, var0.maxZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.minY, var0.minZ);
            var10000 = var0.minX;
            var10001 = var0.minY;
            if (((1981734264 ^ 1844189140) >>> 2 << 1 >>> 2 ^ 58612053) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.maxZ);
            if ((((1159441048 | 903173252) >>> 4 ^ 45417688) >> 1 ^ 49577368) == 0) {
                  ;
            }

            var10000 = var0.minX;
            if (!"minecraft".equals("your mom your dad the one you never had")) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.maxY, var0.minZ);
            var10000 = var0.minX;
            var10001 = var0.minY;
            if (!"please dont crack my plugin".equals("you're dogshit")) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.minZ);
            if ((((1325429069 | 828822760) & 518332001) >>> 4 ^ 31867206) == 0) {
                  ;
            }

            GL11.glVertex3d(var0.maxX, var0.minY, var0.maxZ);
            var10000 = var0.minX;
            if (((1468920417 << 1 | 33997777) >>> 1 >>> 3 ^ 183631485) == 0) {
                  ;
            }

            var10001 = var0.minY;
            double var10002 = var0.maxZ;
            if ((1578181670 >>> 2 & 194187310 ^ 58720264) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var10002);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.minZ);
            if (((1222183614 >>> 1 | 606493562) >> 3 ^ 76405743) == 0) {
                  ;
            }

            GL11.glEnd();
      }
}
