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
            double var10003 = Minecraft.getMinecraft().getRenderManager().viewerPosY;
            if (((1551155249 >>> 3 | 120656129 | 157443090) << 1 ^ 536869422) == 0) {
                  ;
            }

            var10000.<init>(var10002, var10003 + (double)Minecraft.getMinecraft().player.getEyeHeight(), Minecraft.getMinecraft().getRenderManager().viewerPosZ);
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            var10000 = var10000.add(Minecraft.getMinecraft().player.getLookVec());
            if (((1626028415 | 1575628557 | 428979326) << 3 ^ 1807496798) != 0) {
                  ;
            }

            Vec3d var1 = var10000;
            if (!"intentMoment".equals("shitted on you harder than archybot")) {
                  ;
            }

            GL11.glLineWidth(2.0F);
            if (!"ape covered in human flesh".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            GL11.glBegin(0 >> 2 >>> 2 ^ 659491292 ^ 295463725 ^ 919823088);
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
                  if ((0 ^ 0) == 0) {
                        ;
                  }

                  Entity var3 = (Entity)var2.next();
                  Vec3d var4 = var3.getEntityBoundingBox().getCenter();
                  if (var3 instanceof EntityItem) {
                        if (((86150184 | 49546689) ^ 133598697) == 0) {
                              ;
                        }

                        GL11.glColor4f(0.5F, 0.5F, 1.0F, 0.5F);
                  } else {
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please dont crack my plugin")) {
                              ;
                        }

                        Minecraft var6 = Minecraft.getMinecraft();
                        if (!"ape covered in human flesh".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        float var5 = var6.player.getDistance(var3) / 20.0F;
                        GL11.glColor4f(2.0F - var5, var5, 0.0F, 0.5F);
                  }

                  GL11.glVertex3d(var1.x, var1.y, var1.z);
                  GL11.glVertex3d(var4.x, var4.y, var4.z);
            }

            if (!"you're dogshit".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            GL11.glEnd();
      }

      public static void drawESPBoxes(List var0, int var1, float var2) {
            if ((1154834375 << 2 >>> 2 & 75873504 ^ -2076793309) != 0) {
                  ;
            }

            GL11.glLineWidth(2.0F);
            Iterator var3 = var0.iterator();

            while(true) {
                  if ((1708742917 >> 2 >>> 3 >> 2 ^ 6427992 ^ 11118570) != 0) {
                  }

                  if (!var3.hasNext()) {
                        return;
                  }

                  Entity var4 = (Entity)var3.next();
                  GL11.glPushMatrix();
                  Vec3d var5 = EntityUtil.getInterpolatedPos(var4, var2);
                  double var10000 = var5.x;
                  double var10001 = var5.y;
                  if (((2047115748 | 1322738321) ^ 2070787423 ^ 2266841 ^ 93896819) == 0) {
                        ;
                  }

                  GL11.glTranslated(var10000, var10001, var5.z);
                  var10000 = (double)var4.width + 0.1D;
                  var10001 = (double)var4.height + 0.1D;
                  double var10002 = (double)var4.width;
                  if ((141558272 ^ 105839745 ^ 239009409) == 0) {
                        ;
                  }

                  GL11.glScaled(var10000, var10001, var10002 + 0.1D);
                  if (var4 instanceof EntityItem) {
                        GL11.glColor4f(0.5F, 0.5F, 1.0F, 0.5F);
                  } else {
                        float var7 = Minecraft.getMinecraft().player.getDistance(var4);
                        if (!"please go outside".equals("please dont crack my plugin")) {
                              ;
                        }

                        float var6 = var7 / 20.0F;
                        var7 = 2.0F - var6;
                        if ((((735173708 | 140203781) >> 1 | 6234230) ^ 19778359 ^ -968570209) != 0) {
                              ;
                        }

                        GL11.glColor4f(var7, var6, 0.0F, 0.5F);
                  }

                  if (((371466498 >> 4 | 5585413 | 9994795) ^ 1104650924) != 0) {
                        ;
                  }

                  GL11.glCallList(var1);
                  GL11.glPopMatrix();
                  if ((1609458500 >>> 1 << 4 & 1281765749 ^ 1281495072) == 0) {
                        ;
                  }
            }
      }

      public static void drawOutlinedBox(AxisAlignedBB var0) {
            if (!"please take a shower".equals("shitted on you harder than archybot")) {
                  ;
            }

            GL11.glBegin((0 & 1563825164 & 888367420) >>> 4 ^ 1);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            if (!"ape covered in human flesh".equals("please dont crack my plugin")) {
                  ;
            }

            double var10000 = var0.minX;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.maxY, var0.maxZ);
            var10000 = var0.minX;
            double var10001 = var0.maxY;
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("ape covered in human flesh")) {
                  ;
            }

            double var10002 = var0.maxZ;
            if (!"nefariousMoment".equals("you're dogshit")) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var10002);
            if ((1046206293 >> 3 << 3 >>> 1 >> 1 ^ -60170318) != 0) {
                  ;
            }

            GL11.glVertex3d(var0.minX, var0.maxY, var0.minZ);
            var10000 = var0.minX;
            var10001 = var0.minY;
            if ((((355342590 >>> 4 ^ 14167350) & 18987633 ^ 15720731) >> 4 ^ -1062583221) != 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.minZ);
            if (!"please get a girlfriend and stop cracking plugins".equals("i hope you catch fire ngl")) {
                  ;
            }

            GL11.glVertex3d(var0.maxX, var0.minY, var0.maxZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.maxZ);
            if (!"idiot".equals("ape covered in human flesh")) {
                  ;
            }

            GL11.glVertex3d(var0.minX, var0.minY, var0.maxZ);
            var10000 = var0.minX;
            var10001 = var0.minY;
            if ((1500496306 << 4 << 2 << 2 << 3 ^ 2114818048) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.minX, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.maxZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            var10000 = var0.minX;
            if ((((416119872 ^ 139612451) >>> 3 & 32729073) >> 4 ^ 79450) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.minY, var0.maxZ);
            if (!"intentMoment".equals("please go outside")) {
                  ;
            }

            GL11.glVertex3d(var0.minX, var0.maxY, var0.maxZ);
            GL11.glEnd();
      }

      public static void drawSolidBox(AxisAlignedBB var0) {
            if ((((663369438 | 110747817 | 19306809) ^ 322682345) >> 1 ^ 392129222 ^ 220023757) == 0) {
                  ;
            }

            GL11.glBegin((4 >>> 1 | 1) << 2 & 2 ^ 7);
            if ((1927276264 >> 2 << 2 & 1842376728 ^ 688769868 ^ 1239264068) == 0) {
                  ;
            }

            GL11.glVertex3d(var0.minX, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.minZ);
            if ((943013870 << 2 >>> 3 << 4 << 2 ^ 111672768) == 0) {
                  ;
            }

            GL11.glVertex3d(var0.maxX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.minZ);
            double var10000 = var0.maxX;
            double var10001 = var0.maxY;
            double var10002 = var0.minZ;
            if (!"buy a domain and everything else you need at namecheap.com".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var10002);
            if ((((1502139246 ^ 100500772) << 4 | 2146089743) ^ -81) == 0) {
                  ;
            }

            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            var10000 = var0.maxX;
            var10001 = var0.minY;
            if ((((1462090415 << 4 | 915396284) & 690642727 | 429778863) ^ 966786991) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.maxZ);
            var10000 = var0.minX;
            var10001 = var0.minY;
            if ((((954199082 | 153841994) << 4 | 727057632) ^ -1073752352) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.maxZ);
            var10000 = var0.maxX;
            if (!"stop skidding".equals("intentMoment")) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.minY, var0.maxZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            var10000 = var0.minX;
            var10001 = var0.maxY;
            var10002 = var0.maxZ;
            if (!"stringer is a good obfuscator".equals("intentMoment")) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var10002);
            GL11.glVertex3d(var0.minX, var0.minY, var0.minZ);
            var10000 = var0.minX;
            var10001 = var0.minY;
            if ((1279987999 >>> 2 >>> 4 >> 3 ^ 926734 ^ 2621830) == 0) {
                  ;
            }

            var10002 = var0.maxZ;
            if (((21006229 ^ 2651383 ^ 11842438) >>> 1 ^ 15605874) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var10002);
            var10000 = var0.minX;
            if (!"i hope you catch fire ngl".equals("please dont crack my plugin")) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.minZ);
            if (((427845848 ^ 394194219) >> 2 << 4 ^ 1006245824) == 0) {
                  ;
            }

            var10000 = var0.minX;
            if (((1721448114 << 3 << 1 & 1355333462 | 535544660) ^ 1609296724) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.minY, var0.minZ);
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stop skidding")) {
                  ;
            }

            var10000 = var0.maxX;
            if (!"you probably spell youre as your".equals("idiot")) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.maxZ);
            if (!"idiot".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            if ((1993144399 >>> 1 >>> 1 >> 4 ^ 31142881) == 0) {
                  ;
            }

            var10000 = var0.minX;
            var10001 = var0.minY;
            if ((1832093012 >> 2 >>> 1 & 29606780 ^ 25313320) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            var10000 = var0.maxX;
            var10001 = var0.maxY;
            if (!"i hope you catch fire ngl".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            var10002 = var0.minZ;
            if ((159768908 >> 1 << 4 ^ 1002138647 ^ 2006214263) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var10002);
            if ((566056497 << 4 ^ 417823299 ^ -1114136496) != 0) {
                  ;
            }

            GL11.glEnd();
      }
}
