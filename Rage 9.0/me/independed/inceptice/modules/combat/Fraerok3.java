//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Fraerok3 extends Module {
      public NumberSetting heightSet;
      public NumberSetting widthSet;

      public Fraerok3() {
            int var10003 = 536922126 >> 3 >>> 2 >> 3 ^ 2097352;
            if (((67928007 | 56388762) >> 1 ^ 89799510) != 0) {
                  ;
            }

            super("H1tBox", "bigger players range attack", var10003, Module.Category.COMBAT);
            if ((1820315292 << 2 >>> 1 >> 2 ^ 373286734) == 0) {
                  ;
            }

            this.widthSet = new NumberSetting("Width", this, 1.0D, 1.0D, 10.0D, 0.1D);
            this.heightSet = new NumberSetting("Height", this, 2.0D, 1.0D, 10.0D, 0.1D);
            Setting[] var10001 = new Setting[((0 | 1786306906) & 198575514) >> 2 >>> 1 ^ 21626913];
            var10001[(301625988 >> 1 & 102067549) >> 2 << 4 << 3 ^ 44378112] = this.widthSet;
            var10001[(0 << 1 | 1392213039 | 1285531404) ^ 1593835310] = this.heightSet;
            this.addSettings(var10001);
      }

      public boolean check(EntityLivingBase var1) {
            boolean var10000 = var1 instanceof EntityPlayerSP;
            if (!"buy a domain and everything else you need at namecheap.com".equals("please take a shower")) {
                  ;
            }

            if (var10000) {
                  return (boolean)((891983168 << 1 & 1381785793) << 1 ^ -2069348096);
            } else if (var1 == mc.player) {
                  return (boolean)((1172823326 >> 3 >> 4 | 3203093) << 3 >>> 3 ^ 12316607);
            } else if (var1.isDead) {
                  return (boolean)((693096932 << 3 ^ 1150183688) & 131362331 ^ 114320904);
            } else if (var1.getHealth() < 0.0F) {
                  int var2 = (5497447 | 5193497) << 3 ^ 50330616;
                  if (((1952986795 >>> 2 << 2 | 147148058 | 1406825450) ^ 2147450874) == 0) {
                        ;
                  }

                  return (boolean)var2;
            } else {
                  return (boolean)(!(var1 instanceof EntityPlayer) ? (1709376928 ^ 547193977) >>> 1 & 304615870 ^ 36176044 : ((0 & 1882288253) >>> 3 | 1593733927) ^ 1593733926);
            }
      }

      public void setEntityBoundingBoxSize(EntityPlayer var1, float var2, float var3) {
            if ((((1166376136 ^ 802482544) >> 3 ^ 16514265) << 2 ^ 919026104) == 0) {
                  ;
            }

            EntitySize var4 = this.getEntitySize(var1);
            var1.width = var4.width;
            var1.height = var4.height;
            if (((905827666 | 834520414) ^ 328838497 ^ 2133671530) != 0) {
                  ;
            }

            double var5 = (double)var2 / 2.0D;
            if ((((134300755 ^ 72081776) & 27439699) >>> 1 ^ 86017) == 0) {
                  ;
            }

            AxisAlignedBB var10001 = new AxisAlignedBB;
            if (!"you probably spell youre as your".equals("you're dogshit")) {
                  ;
            }

            double var10003 = var1.posX - var5;
            double var10004 = var1.posY;
            double var10005 = var1.posZ;
            if (!"please take a shower".equals("intentMoment")) {
                  ;
            }

            var10001.<init>(var10003, var10004, var10005 - var5, var1.posX + var5, var1.posY + (double)var3, var1.posZ + var5);
            var1.setEntityBoundingBox(var10001);
      }

      @SubscribeEvent
      public void onClientTick(ClientTickEvent var1) {
            this.setHitBoxForEntities();
            if (((590047097 ^ 111660480) >>> 3 ^ 436761 ^ 1982172375) != 0) {
                  ;
            }

            if (((296628011 | 67272378 | 1992634) << 2 ^ 1157262842) != 0) {
                  ;
            }

      }

      public EntitySize getEntitySize(EntityPlayer var1) {
            EntitySize var2 = new EntitySize(0.6F, 1.8F);
            return var2;
      }

      public void setHitBoxForEntities() {
            if (mc.player != null) {
                  EntityPlayerSP var10000 = mc.player;
                  if (((2142937738 >>> 4 ^ 11667532 | 49723433) >>> 3 ^ 16774989) == 0) {
                        ;
                  }

                  if (!var10000.isDead) {
                        Stream var6 = mc.world.loadedEntityList.stream().filter((var0) -> {
                              int var10000 = var0 != mc.player ? (0 >> 1 >>> 3 & 352211896) << 4 ^ 1 : 1072555974 >> 3 ^ 133107960 ^ 1223680;
                              if (!"yo mama name maurice".equals("please go outside")) {
                                    ;
                              }

                              return (boolean)var10000;
                        }).filter((var0) -> {
                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please dont crack my plugin")) {
                                    ;
                              }

                              EntityPlayerSP var10000 = mc.player;
                              if ((469685715 << 2 & 175055784 & 132844110 ^ 40567304) == 0) {
                                    ;
                              }

                              return (boolean)(var10000.getDistance(var0) <= 200.0F ? 0 << 2 >> 4 >> 2 << 3 ^ 1 : (1508084378 >> 2 << 2 | 1242174859) ^ 31407953 ^ 358491894 ^ 1332381244);
                        }).filter((var0) -> {
                              return (boolean)(!var0.isDead ? (0 | 534206614) ^ 164759519 ^ 369450824 : (1005594958 >> 1 << 1 ^ 788709369) & 135429893 ^ 1185285);
                        }).filter((var0) -> {
                              return var0 instanceof EntityPlayer;
                        });
                        Function var10001 = (var0) -> {
                              if (((270540800 >> 4 | 1219119) & 3601983 ^ -1868485510) != 0) {
                                    ;
                              }

                              return Float.valueOf(mc.player.getDistance(var0));
                        };
                        if ((((69207237 ^ 34269742) >>> 4 | 5009964) ^ 7274222) == 0) {
                              ;
                        }

                        List var1 = (List)var6.sorted(Comparator.comparing(var10001)).collect(Collectors.toList());
                        Iterator var2 = var1.iterator();

                        while(var2.hasNext()) {
                              Entity var3 = (Entity)var2.next();
                              float var4 = (float)this.widthSet.getValue();
                              if (((501205686 << 1 >> 4 & 56666046) >> 3 ^ 581497334) != 0) {
                                    ;
                              }

                              float var5 = (float)this.heightSet.getValue();
                              this.setEntityBoundingBoxSize((EntityPlayer)var3, var4, var5);
                        }

                        return;
                  }
            }

      }
}
