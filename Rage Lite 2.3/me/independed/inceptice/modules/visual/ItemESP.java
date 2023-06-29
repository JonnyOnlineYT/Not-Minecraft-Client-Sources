//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.independed.inceptice.friends.FriendManager;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.util.EntityUtil;
import me.independed.inceptice.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ItemESP extends Module {
      public transient BooleanSetting showItemNames;
      private transient List ENTITIES;
      private transient int BOX;
      public transient BooleanSetting tracers = new BooleanSetting("Tracers", this, (boolean)(37831564 >>> 1 >> 1 ^ 6005707 ^ 13366056));

      public ItemESP() {
            super("ItemESP", "shows item through the walls", 596749899 >>> 4 << 3 ^ 298374944, Module.Category.RENDER);
            if (((1968653346 >> 2 | 448549929) << 1 ^ 1073457746) == 0) {
                  ;
            }

            this.showItemNames = new BooleanSetting("ShowName", this, (boolean)((0 | 864417599) >>> 2 & 142398541 ^ 140530764));
            this.ENTITIES = new ArrayList();
            int var10001 = 268444288 ^ 268444288;
            if (((2041641119 >> 2 ^ 127584444 ^ 298812212) >> 3 ^ -233498783) != 0) {
                  ;
            }

            this.BOX = var10001;
            List var10000 = this.settings;
            if (((1209413879 >> 2 & 255426619) >> 3 << 2 ^ 16826396) == 0) {
                  ;
            }

            var10000.add(this.tracers);
            this.settings.add(this.showItemNames);
      }

      public void onRenderWorldLast(float var1) {
            if (mc.player != null) {
                  if ((523414379 >>> 2 & 37254025 ^ -1032329314) != 0) {
                        ;
                  }

                  GL11.glPushAttrib(((8586 & 6836 | 119) >>> 2 ^ 60 | 0) ^ 24580);
                  if ((((318543594 | 26952052 | 262951240) & 153426644) >> 1 ^ 76712298) == 0) {
                        ;
                  }

                  GL11.glDisable(((2127 << 3 | 976) << 2 | 10625) ^ 74240);
                  GL11.glDisable((1419 >> 4 ^ 3) & 20 ^ 2913);
                  GL11.glDisable((1318 ^ 657) >> 1 ^ 2187);
                  GL11.glEnable((1711 ^ 1002) << 1 >> 2 ^ 2368);
                  GL11.glBlendFunc(587 ^ 342 ^ 204 ^ 211, (142 ^ 22 ^ 78 | 107) ^ 1020);
                  GL11.glEnable(((1207 & 1161) >> 4 | 43) ^ 2891);
                  GL11.glLineWidth(2.0F);
                  GL11.glPushMatrix();
                  double var10000 = -Minecraft.getMinecraft().getRenderManager().viewerPosX;
                  Minecraft var10001 = Minecraft.getMinecraft();
                  if ((((1880675751 >>> 1 >> 4 | 36533418) & 26866326 | 7879117) ^ -1817410758) != 0) {
                        ;
                  }

                  GL11.glTranslated(var10000, -var10001.getRenderManager().viewerPosY, -Minecraft.getMinecraft().getRenderManager().viewerPosZ);
                  this.ENTITIES.clear();
                  Iterator var2 = Minecraft.getMinecraft().world.loadedEntityList.iterator();
                  if ((852042784 >>> 3 >>> 1 >> 1 >> 3 ^ 3328292) == 0) {
                        ;
                  }

                  while(var2.hasNext()) {
                        Entity var3 = (Entity)var2.next();
                        if (var3 instanceof EntityItem) {
                              if (((15450119 | 5586301 | 13046483) << 2 ^ 43488480) != 0) {
                                    ;
                              }

                              this.ENTITIES.add(var3);
                        }
                  }

                  ItemESP.drawESPBoxes(this.ENTITIES, this.BOX, var1);
                  if (this.tracers.isEnabled()) {
                        List var4 = this.ENTITIES;
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please dont crack my plugin")) {
                              ;
                        }

                        RenderUtil.drawESPTracers(var4);
                  }

                  GL11.glPopAttrib();
                  if (this.showItemNames.isEnabled()) {
                        this.drawStackNames(var1);
                  }

                  if ((((246920627 ^ 127379293) >>> 4 | 2024469) ^ 1016613808) != 0) {
                        ;
                  }

                  GL11.glPopMatrix();
            }
      }

      private void drawStackNames(float var1) {
            Iterator var10000 = this.ENTITIES.iterator();
            if (!"intentMoment".equals("stop skidding")) {
                  ;
            }

            Iterator var2 = var10000;

            while(var2.hasNext()) {
                  Entity var3 = (Entity)var2.next();
                  GL11.glPushMatrix();
                  if ((((1172016260 | 9677959) >>> 2 ^ 183662855) & 189440692 ^ 184582692) == 0) {
                        ;
                  }

                  if (!"yo mama name maurice".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  Vec3d var4 = EntityUtil.getInterpolatedPos(var3, var1);
                  GL11.glTranslated(var4.x, var4.y, var4.z);
                  EntityItem var6 = (EntityItem)var3;
                  if (!"intentMoment".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  ItemStack var5 = var6.getItem();
                  FontRenderer var7 = Minecraft.getMinecraft().fontRenderer;
                  String var10001 = (new StringBuilder()).append(var5.getCount()).append("x ").append(var5.getDisplayName()).toString();
                  if ((((51023672 ^ 37653664) >> 3 ^ 2081150) >>> 4 ^ 234512) == 0) {
                        ;
                  }

                  int var10005 = 1090522768 >>> 2 ^ 272630692;
                  float var10006 = Minecraft.getMinecraft().getRenderManager().playerViewY;
                  float var10007 = Minecraft.getMinecraft().getRenderManager().playerViewX;
                  int var10008 = Minecraft.getMinecraft().gameSettings.thirdPersonView == (((1 << 3 & 2) << 4 ^ 1767429630) << 3 ^ 1254535154) ? 0 >>> 2 ^ 754873213 ^ 754873212 : 1191970820 >>> 3 >> 2 ^ 37249088;
                  int var10009 = (350650229 >>> 3 ^ 39240962 | 12365169) >>> 1 ^ 8345598;
                  if (!"intentMoment".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  EntityRenderer.drawNameplate(var7, var10001, 0.0F, 1.0F, 0.0F, var10005, var10006, var10007, (boolean)var10008, (boolean)var10009);
                  GL11.glPopMatrix();
            }

      }

      public static void drawESPBoxes(List var0, int var1, float var2) {
            if (((74606900 ^ 13246346 ^ 37525481 | 58948924 | 34139527) ^ 126877183) == 0) {
                  ;
            }

            GL11.glLineWidth(2.0F);
            Iterator var3 = var0.iterator();

            while(var3.hasNext()) {
                  Entity var4 = (Entity)var3.next();
                  GL11.glPushMatrix();
                  Vec3d var10000 = EntityUtil.getInterpolatedPos(var4, var2);
                  if (((((178608201 | 108798987 | 212768831) & 177425642) >> 3 | 9270507) ^ 1149810181) != 0) {
                        ;
                  }

                  Vec3d var5 = var10000;
                  double var7 = var5.x;
                  double var10001 = var5.y;
                  if (!"yo mama name maurice".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  GL11.glTranslated(var7, var10001, var5.z);
                  var7 = (double)var4.width + 0.1D;
                  var10001 = (double)var4.height + 0.1D;
                  float var10002 = var4.width;
                  if ((1834291558 >> 2 >>> 2 ^ 114643222) == 0) {
                        ;
                  }

                  double var10 = (double)var10002;
                  if ((13371456 >>> 1 ^ 1899508 ^ 7107635 ^ -1262353911) != 0) {
                        ;
                  }

                  GL11.glScaled(var7, var10001, var10 + 0.1D);
                  if (var4 instanceof EntityPlayer && FriendManager.isFriend(var4.getName())) {
                        if (!"stop skidding".equals("please take a shower")) {
                              ;
                        }

                        if (((643037976 ^ 338072774 | 438446328 | 844912657) ^ 271124662) != 0) {
                              ;
                        }

                        GL11.glColor4f(0.9F, 0.2F, 1.0F, 0.5F);
                  } else {
                        if (!"yo mama name maurice".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        boolean var8 = var4 instanceof EntityItem;
                        if (!"buy a domain and everything else you need at namecheap.com".equals("yo mama name maurice")) {
                              ;
                        }

                        if (var8) {
                              if ((247713600 >> 1 & 47091320 ^ 479780616) != 0) {
                                    ;
                              }

                              GL11.glColor4f(0.5F, 0.5F, 1.0F, 0.5F);
                        } else {
                              float var6 = Minecraft.getMinecraft().player.getDistance(var4) / 20.0F;
                              if (((980132003 | 770789251) >> 4 >> 1 << 4 ^ 536737744) == 0) {
                                    ;
                              }

                              float var9 = 2.0F - var6;
                              if (((281644926 | 71637762) >> 1 >>> 2 << 4 ^ 698040048) == 0) {
                                    ;
                              }

                              GL11.glColor4f(var9, var6, 0.0F, 0.5F);
                        }
                  }

                  GL11.glCallList(var1);
                  GL11.glPopMatrix();
            }

      }
}
