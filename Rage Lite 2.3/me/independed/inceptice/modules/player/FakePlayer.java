//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import com.mojang.authlib.GameProfile;
import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.passive.EntityDonkey;

public class FakePlayer extends Module {
      private EntityOtherPlayerMP Original;
      private EntityDonkey RidingEntity;

      public void onEnable() {
            if (mc.player != null) {
                  if (mc.world == null) {
                        if (((255486784 ^ 209785365) >> 1 ^ 2021845943) != 0) {
                              ;
                        }

                        this.toggle();
                  } else {
                        EntityOtherPlayerMP var10001 = new EntityOtherPlayerMP;
                        WorldClient var10003 = mc.world;
                        GameProfile var10004 = mc.player.getGameProfile();
                        if (!"yo mama name maurice".equals("nefariousMoment")) {
                              ;
                        }

                        var10001.<init>(var10003, var10004);
                        this.Original = var10001;
                        EntityOtherPlayerMP var10000 = this.Original;
                        Minecraft var4 = mc;
                        if ((1387830496 << 4 >> 4 ^ 45653216) == 0) {
                              ;
                        }

                        var10000.copyLocationAndAnglesFrom(var4.player);
                        if (!"stop skidding".equals("idiot")) {
                              ;
                        }

                        this.Original.rotationYaw = mc.player.rotationYaw;
                        var10000 = this.Original;
                        float var5 = mc.player.rotationYawHead;
                        if (!"you probably spell youre as your".equals("you probably spell youre as your")) {
                              ;
                        }

                        var10000.rotationYawHead = var5;
                        this.Original.inventory.copyInventory(mc.player.inventory);
                        WorldClient var2 = mc.world;
                        int var6 = ((478095475 | 272973165) << 1 ^ 389765687 | 270208234) ^ -1070650134;
                        if (((1662313548 ^ 71754674 | 1266365478) & 656409040 ^ -852869651) != 0) {
                              ;
                        }

                        var2.addEntityToWorld(var6, this.Original);
                        if ((577904317 >>> 2 >>> 4 ^ 9029754) == 0) {
                              ;
                        }

                        if (mc.player.isRiding() && mc.player.getRidingEntity() instanceof EntityDonkey) {
                              EntityDonkey var1 = (EntityDonkey)mc.player.getRidingEntity();
                              EntityDonkey var7 = new EntityDonkey(mc.world);
                              if (((1231288531 ^ 1066443320) << 4 ^ 564636362) != 0) {
                                    ;
                              }

                              this.RidingEntity = var7;
                              EntityDonkey var3 = this.RidingEntity;
                              if ((437482739 >> 2 << 3 ^ 874965472) == 0) {
                                    ;
                              }

                              var3.copyLocationAndAnglesFrom(var1);
                              if (!"i hope you catch fire ngl".equals("ape covered in human flesh")) {
                                    ;
                              }

                              this.RidingEntity.setChested(var1.hasChest());
                              mc.world.addEntityToWorld((750892590 | 23813913) >> 2 & 128866492 ^ -52799346, this.RidingEntity);
                              this.Original.startRiding(this.RidingEntity, (boolean)(0 >>> 4 & 424260267 ^ 1));
                        }

                  }
            }
      }

      public FakePlayer() {
            int var10003 = (1372397576 | 370404346) ^ 1474292730;
            if (!"your mom your dad the one you never had".equals("you probably spell youre as your")) {
                  ;
            }

            super("FakePlayer", "second player", var10003, Module.Category.PLAYER);
            if (!"yo mama name maurice".equals("please take a shower")) {
                  ;
            }

      }

      public void onDisable() {
            if (((613689329 >> 3 & 14269575) >>> 2 ^ 2367777) == 0) {
                  ;
            }

            if (mc.player != null) {
                  super.onDisable();
                  if (this.Original != null) {
                        boolean var10000 = this.Original.isRiding();
                        if (((1585700251 << 4 ^ 1721679272 | 1905591858) & 596241360 ^ 595681808) == 0) {
                              ;
                        }

                        if (var10000) {
                              this.Original.dismountRidingEntity();
                        }

                        if ((2070254729 >>> 1 >>> 4 ^ 64695460) == 0) {
                              ;
                        }

                        WorldClient var1 = mc.world;
                        EntityOtherPlayerMP var10001 = this.Original;
                        if ((1778880275 << 3 ^ 1236506081 ^ 428862841) == 0) {
                              ;
                        }

                        var1.removeEntity(var10001);
                  }

                  if (this.RidingEntity != null) {
                        mc.world.removeEntity(this.RidingEntity);
                  }

            }
      }
}
