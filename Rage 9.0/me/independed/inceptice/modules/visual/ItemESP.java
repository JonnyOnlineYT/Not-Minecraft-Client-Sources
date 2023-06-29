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
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ItemESP extends Module {
      private transient int BOX;
      private transient List ENTITIES = new ArrayList();
      public transient BooleanSetting showItemNames = new BooleanSetting("ShowName", this, (boolean)(0 >> 4 & 1704267910 ^ 2089429640 ^ 2089429641));
      public transient BooleanSetting tracers = new BooleanSetting("Tracers", this, (boolean)((283907735 >>> 4 & 17261509) >>> 1 >>> 4 ^ 537098));

      public ItemESP() {
            super("ItemESP", "shows item through the walls", ((713029538 ^ 62699424 ^ 36640264) & 419153706) >> 4 ^ 9340000, Module.Category.RENDER);
            if (((2109014798 ^ 947002092) >>> 4 & 38330201 ^ 4739352) == 0) {
                  ;
            }

            int var10001 = 1455879005 >> 4 & 295319 & 92018 ^ 272;
            if (((659991222 >>> 1 & 198905834 | 28177597) ^ 61863423) == 0) {
                  ;
            }

            this.BOX = var10001;
            if (((38404352 | 1143403) ^ 775445473) != 0) {
                  ;
            }

            List var10000 = this.settings;
            if (!"please take a shower".equals("stop skidding")) {
                  ;
            }

            var10000.add(this.tracers);
            this.settings.add(this.showItemNames);
            if ((471298724 >> 2 << 2 ^ 81563155 ^ 702819856) != 0) {
                  ;
            }

      }

      public static void drawESPBoxes(List var0, int var1, float var2) {
            if (!"yo mama name maurice".equals("please take a shower")) {
                  ;
            }

            GL11.glLineWidth(2.0F);
            Iterator var3 = var0.iterator();

            while(true) {
                  if ("i hope you catch fire ngl".equals("yo mama name maurice")) {
                  }

                  if (!var3.hasNext()) {
                        return;
                  }

                  Entity var4 = (Entity)var3.next();
                  GL11.glPushMatrix();
                  if (((1222818569 << 2 >>> 2 | 75992220) ^ 216514461) != 0) {
                  }

                  Vec3d var5 = EntityUtil.getInterpolatedPos(var4, var2);
                  double var10000 = var5.x;
                  if (((((153097679 | 15524292) ^ 31854171) >> 4 ^ 7257615) >> 4 ^ 975131) == 0) {
                        ;
                  }

                  GL11.glTranslated(var10000, var5.y, var5.z);
                  GL11.glScaled((double)var4.width + 0.1D, (double)var4.height + 0.1D, (double)var4.width + 0.1D);
                  if (var4 instanceof EntityPlayer && FriendManager.isFriend(var4.getName())) {
                        GL11.glColor4f(0.9F, 0.2F, 1.0F, 0.5F);
                  } else {
                        if ((2054878854 >> 1 >> 2 ^ 66561432 ^ 213449544) == 0) {
                              ;
                        }

                        if (var4 instanceof EntityItem) {
                              GL11.glColor4f(0.5F, 0.5F, 1.0F, 0.5F);
                        } else {
                              float var6 = Minecraft.getMinecraft().player.getDistance(var4) / 20.0F;
                              GL11.glColor4f(2.0F - var6, var6, 0.0F, 0.5F);
                        }
                  }

                  GL11.glCallList(var1);
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  GL11.glPopMatrix();
            }
      }

      private void drawStackNames(float var1) {
            Iterator var2 = this.ENTITIES.iterator();

            while(var2.hasNext()) {
                  Entity var3 = (Entity)var2.next();
                  GL11.glPushMatrix();
                  if (((((664179825 | 235294330) ^ 129803790) >> 1 << 2 | 536775016) ^ 1610545640) == 0) {
                        ;
                  }

                  Vec3d var4 = EntityUtil.getInterpolatedPos(var3, var1);
                  GL11.glTranslated(var4.x, var4.y, var4.z);
                  ItemStack var5 = ((EntityItem)var3).getItem();
                  FontRenderer var10000 = Minecraft.getMinecraft().fontRenderer;
                  StringBuilder var10001 = new StringBuilder;
                  if (!"intentMoment".equals("please go outside")) {
                        ;
                  }

                  var10001.<init>();
                  String var6 = var10001.append(var5.getCount()).append("x ").append(var5.getDisplayName()).toString();
                  if (((1264482047 >> 3 ^ 80026928) >> 2 >> 3 ^ 7173791) == 0) {
                        ;
                  }

                  EntityRenderer.drawNameplate(var10000, var6, 0.0F, 1.0F, 0.0F, 185700235 >>> 1 & 15227883 ^ 8930241, Minecraft.getMinecraft().getRenderManager().playerViewY, Minecraft.getMinecraft().getRenderManager().playerViewX, (boolean)(Minecraft.getMinecraft().gameSettings.thirdPersonView == (((0 & 887118344 ^ 912189455) >>> 2 | 226153573) ^ 234880613) ? ((0 ^ 110588843) & 38879910) >>> 2 >> 3 ^ 1083924 : (139460996 | 94735494 | 127529015) & 39215899 ^ 39084307), (boolean)(1521183784 >>> 4 >> 3 << 3 >>> 1 ^ 47536992));
                  GL11.glPopMatrix();
            }

      }

      public void onRenderWorldLast(float var1) {
            if (mc.player != null) {
                  GL11.glPushAttrib(19480 >> 3 << 2 << 3 ^ 86117);
                  GL11.glDisable(3355 << 3 >> 4 ^ 2924);
                  GL11.glDisable(2138 << 1 << 3 & 32660 & 307 ^ 2673);
                  GL11.glDisable((1630 << 2 >>> 4 & 187) >>> 1 ^ 51 ^ 2858);
                  if (((1768323364 << 3 | 1006333030 | 1067374869 | 656169979) ^ Integer.MAX_VALUE) == 0) {
                        ;
                  }

                  GL11.glEnable((437 << 2 | 90) << 4 ^ 26114);
                  GL11.glBlendFunc(((634 ^ 197 | 418) ^ 83) >> 3 ^ 28 ^ 867, (32 & 1) >>> 1 << 1 ^ 771);
                  GL11.glEnable((95 | 92) >>> 2 >> 2 >>> 2 ^ 2849);
                  GL11.glLineWidth(2.0F);
                  GL11.glPushMatrix();
                  double var10000 = -Minecraft.getMinecraft().getRenderManager().viewerPosX;
                  RenderManager var10001 = Minecraft.getMinecraft().getRenderManager();
                  if ((840761273 >>> 1 >>> 2 >> 3 >>> 2 ^ 1069541085) != 0) {
                        ;
                  }

                  GL11.glTranslated(var10000, -var10001.viewerPosY, -Minecraft.getMinecraft().getRenderManager().viewerPosZ);
                  this.ENTITIES.clear();
                  Iterator var2 = Minecraft.getMinecraft().world.loadedEntityList.iterator();

                  while(var2.hasNext()) {
                        Entity var3 = (Entity)var2.next();
                        if (var3 instanceof EntityItem) {
                              if (!"buy a domain and everything else you need at namecheap.com".equals("ape covered in human flesh")) {
                                    ;
                              }

                              this.ENTITIES.add(var3);
                        }
                  }

                  List var4 = this.ENTITIES;
                  if (!"ape covered in human flesh".equals("please dont crack my plugin")) {
                        ;
                  }

                  ItemESP.drawESPBoxes(var4, this.BOX, var1);
                  if ((1667573487 << 3 << 1 ^ -15005695) != 0) {
                        ;
                  }

                  if (this.tracers.isEnabled()) {
                        RenderUtil.drawESPTracers(this.ENTITIES);
                  }

                  GL11.glPopAttrib();
                  if ((((1885281006 ^ 1456573119 | 332287074) ^ 833881816) >>> 1 ^ 54357333) == 0) {
                        ;
                  }

                  if (this.showItemNames.isEnabled()) {
                        if ((((1513537 | 539566) & 1259579) >>> 3 ^ 1165423057) != 0) {
                              ;
                        }

                        this.drawStackNames(var1);
                  }

                  GL11.glPopMatrix();
            }
      }
}
