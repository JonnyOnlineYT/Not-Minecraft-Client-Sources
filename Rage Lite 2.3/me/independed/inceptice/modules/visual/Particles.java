//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Particles extends Module {
      public BooleanSetting spitSet;
      public BooleanSetting heartSet = new BooleanSetting("Heart", this, (boolean)((1386794132 ^ 810038165) >>> 3 << 2 ^ 829456256));
      public BooleanSetting redStoneSet = new BooleanSetting("RedStone", this, (boolean)(6314000 >>> 1 >> 1 ^ 1578500));
      public BooleanSetting fireWorkSet;
      public BooleanSetting cloudSet = new BooleanSetting("Cloud", this, (boolean)((665125224 >> 3 | 15062594) & 4375714 ^ 4244514));
      public BooleanSetting smokeSet = new BooleanSetting("Smoke", this, (boolean)(562325604 << 4 >>> 2 ^ 101818768));
      public BooleanSetting portalSet;
      public BooleanSetting flameSett = new BooleanSetting("Flame", this, (boolean)(0 >>> 4 << 2 << 2 ^ 1));

      public Particles() {
            super("Particles", "spawn particles", (1721002244 >> 1 ^ 259159893 | 456363980) << 2 << 1 ^ -102859016, Module.Category.RENDER);
            BooleanSetting var10001 = new BooleanSetting;
            int var10005 = (1565430132 >>> 4 & 56668376) << 4 ^ 336199936;
            if (((46115760 ^ 33598414) << 4 ^ 1206985377) != 0) {
                  ;
            }

            var10001.<init>("FireWork", this, (boolean)var10005);
            this.fireWorkSet = var10001;
            if (!"i hope you catch fire ngl".equals("you're dogshit")) {
                  ;
            }

            this.portalSet = new BooleanSetting("Portal", this, (boolean)((79305651 >>> 1 & 25397756 | '턁') ^ 121305));
            this.spitSet = new BooleanSetting("Spit", this, (boolean)((1654853518 ^ 990177763) >>> 2 << 1 >> 2 ^ 188021965));
            Setting[] var1 = new Setting[(6 | 3) >>> 3 & 1890140769 & 2108808285 ^ 8];
            var1[(436208643 << 2 | 665811451) ^ 1873771007] = this.heartSet;
            var1[(0 & 73629455) >> 3 ^ 1] = this.cloudSet;
            var1[(1 | 0) ^ 0 ^ 0 ^ 3] = this.flameSett;
            int var10003 = (2 << 2 | 4) << 3 ^ 99;
            if (((606181004 >> 3 | 11502661) << 1 ^ 800348556) != 0) {
                  ;
            }

            var1[var10003] = this.smokeSet;
            var1[(1 >>> 1 | 89154350) ^ 89154346] = this.redStoneSet;
            var1[(4 ^ 1 | 0) << 1 >> 3 ^ 4] = this.fireWorkSet;
            var10003 = (2 | 1) << 1 << 1 ^ 10;
            BooleanSetting var10004 = this.portalSet;
            if (((1003893450 ^ 282230442) << 2 & 220447774 ^ 1420244786) != 0) {
                  ;
            }

            var1[var10003] = var10004;
            var1[(1 | 0) & 0 ^ 7] = this.spitSet;
            this.addSettings(var1);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  double var4;
                  WorldClient var10000;
                  EnumParticleTypes var10001;
                  double var10002;
                  double var10003;
                  EntityPlayerSP var10004;
                  if (this.heartSet.isEnabled()) {
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.HEART;
                        var10002 = mc.player.posX;
                        var10003 = mc.player.posY + 0.2D;
                        var10004 = mc.player;
                        if (!"please take a shower".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        var4 = var10004.posZ;
                        if ((((1183759403 >> 4 | 11371985) >> 3 | 4191698) ^ 12582394) == 0) {
                              ;
                        }

                        if (!"you're dogshit".equals("you're dogshit")) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var10003, var4, 0.0D, 0.0D, 0.0D, new int[1106887518 << 3 >> 3 >>> 2 ^ 8286423]);
                  }

                  if (!"yo mama name maurice".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  if (this.cloudSet.isEnabled()) {
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.CLOUD;
                        if (((520266471 | 50960865 | 397500590) << 2 ^ -1641182607) != 0) {
                              ;
                        }

                        var10002 = mc.player.posX;
                        var10003 = mc.player.posY;
                        if (!"nefariousMoment".equals("you probably spell youre as your")) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var10003 + 0.2D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[828010211 >> 3 >> 3 << 1 ^ 25875318]);
                  }

                  if (this.flameSett.isEnabled()) {
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.FLAME;
                        var10002 = mc.player.posX;
                        var10003 = mc.player.posY;
                        if ((((1715941105 ^ 518191699 | 1983978223) >> 4 | 126746796) ^ 133062126) == 0) {
                              ;
                        }

                        var10003 += 0.2D;
                        if (!"please get a girlfriend and stop cracking plugins".equals("minecraft")) {
                              ;
                        }

                        var4 = mc.player.posZ;
                        if ((((1571026945 | 681105934) & 722667135 | 388736845) ^ 1060874063) == 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var10003, var4, 0.0D, 0.0D, 0.0D, new int[(571626816 >>> 1 ^ 195558628) >> 4 ^ 27979108]);
                  }

                  if (this.smokeSet.isEnabled()) {
                        mc.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, mc.player.posX, mc.player.posY + 0.2D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[1019192820 << 3 >>> 3 ^ 482321908]);
                  }

                  int[] var10008;
                  if (this.redStoneSet.isEnabled()) {
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.REDSTONE;
                        var10002 = mc.player.posX;
                        var10003 = mc.player.posY + 0.1D;
                        if (!"minecraft".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        var4 = mc.player.posZ;
                        if (((1607861190 ^ 61212224) >> 4 ^ -1323107037) != 0) {
                              ;
                        }

                        var10008 = new int[(663961256 ^ 85265168) >>> 4 >>> 4 >>> 1 ^ 1131290];
                        if ((1627459977 >>> 4 << 3 >> 2 ^ -1248305377) != 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var10003, var4, 0.0D, 0.0D, 0.0D, var10008);
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.REDSTONE;
                        var10002 = mc.player.posX;
                        var10003 = mc.player.posY;
                        if (!"ape covered in human flesh".equals("yo mama name maurice")) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var10003 + 0.2D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[(1415003000 ^ 275406793) >>> 1 >>> 4 << 1 ^ 71554634]);
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.REDSTONE;
                        var10002 = mc.player.posX;
                        var10003 = mc.player.posY + 0.3D;
                        var10004 = mc.player;
                        if ((((1356762864 ^ 1236012000) & 125387632) >> 1 ^ -1792240784) != 0) {
                              ;
                        }

                        var4 = var10004.posZ;
                        int var8 = 272822901 >>> 3 >> 3 ^ 4262857;
                        if ((2148513 >>> 1 & 224075 ^ 24640) == 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var10003, var4, 0.0D, 0.0D, 0.0D, new int[var8]);
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.REDSTONE;
                        var10002 = mc.player.posX;
                        var10003 = mc.player.posY;
                        if ((66369 << 4 ^ 1061904) == 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var10003 + 0.4D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[(679934406 >> 3 ^ 67931047 ^ 13388046) << 2 ^ 121665092]);
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.REDSTONE;
                        var10002 = mc.player.posX;
                        var10003 = mc.player.posY + 0.5D;
                        var4 = mc.player.posZ;
                        if (!"minecraft".equals("please get a girlfriend and stop cracking plugins")) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var10003, var4, 0.0D, 0.0D, 0.0D, new int[307663075 >>> 2 & 49094665 ^ 8724488]);
                  }

                  boolean var2 = this.fireWorkSet.isEnabled();
                  if ((70406245 >> 2 >>> 3 >> 4 ^ -1418942592) != 0) {
                        ;
                  }

                  if (var2) {
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.FIREWORKS_SPARK;
                        Minecraft var5 = mc;
                        if (((1366775336 | 1089425917) << 2 << 4 >> 3 & 161594842 ^ -819568498) != 0) {
                              ;
                        }

                        var10002 = var5.player.posX;
                        if ((277430784 >>> 4 >>> 3 ^ 718172215) != 0) {
                              ;
                        }

                        var10003 = mc.player.posY + 0.5D;
                        var10004 = mc.player;
                        if (((355096706 | 10206002) << 1 ^ 729282404) == 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var10003, var10004.posZ, 0.0D, 0.0D, 0.0D, new int[((1523704326 ^ 1041834742) & 863913029) >> 3 ^ 27801912 ^ 94449968]);
                  }

                  if (this.portalSet.isEnabled()) {
                        mc.world.spawnParticle(EnumParticleTypes.PORTAL, mc.player.posX, mc.player.posY + 0.2D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[(39895332 << 2 | 73960221) ^ 233541533]);
                        if ((((100520031 | 33209089) << 1 & 33816696 | 4423605) << 3 ^ 1186581194) != 0) {
                              ;
                        }

                        mc.world.spawnParticle(EnumParticleTypes.PORTAL, mc.player.posX, mc.player.posY + 0.2D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[2030204595 >> 2 << 4 ^ 1839324984 ^ -1985477128]);
                        var10000 = mc.world;
                        if (((835741664 >>> 2 >> 1 | 15458175) ^ 117170175) == 0) {
                              ;
                        }

                        var10001 = EnumParticleTypes.PORTAL;
                        EntityPlayerSP var6 = mc.player;
                        if ((621983872 >> 1 >> 3 >>> 1 ^ 721957126) != 0) {
                              ;
                        }

                        var10002 = var6.posX;
                        var10003 = mc.player.posY + 0.2D;
                        var4 = mc.player.posZ;
                        var10008 = new int[(1266546186 ^ 153217088 ^ 541320949) >> 2 ^ 256126673 ^ 398708990];
                        if ((21004640 ^ 3206307 ^ 2456174 ^ 1845104523) != 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var10003, var4, 0.0D, 0.0D, 0.0D, var10008);
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.PORTAL;
                        var10002 = mc.player.posX;
                        Minecraft var7 = mc;
                        if (!"you probably spell youre as your".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var7.player.posY + 0.2D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[1140990034 >> 3 >>> 4 >>> 3 ^ 336056 ^ 1318960]);
                        mc.world.spawnParticle(EnumParticleTypes.PORTAL, mc.player.posX, mc.player.posY + 0.2D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[(30705842 ^ 2302897) >>> 2 ^ 8252096]);
                  }

                  if (((20121620 ^ 18848507) >> 3 >> 1 ^ -1974553681) != 0) {
                        ;
                  }

                  if (this.spitSet.isEnabled()) {
                        Minecraft var3 = mc;
                        if (((813196771 << 4 ^ 119021244) >> 2 >>> 2 ^ 648200) == 0) {
                              ;
                        }

                        var10000 = var3.world;
                        var10001 = EnumParticleTypes.SPIT;
                        var10002 = mc.player.posX;
                        if ((170010021 >> 4 << 4 ^ 170010016) == 0) {
                              ;
                        }

                        var10003 = mc.player.posY + 0.2D;
                        if ((1912910037 >> 1 << 2 & 555036833 & 404929156 ^ 8320) == 0) {
                              ;
                        }

                        var4 = mc.player.posZ;
                        if (((2065391719 ^ 1534431532 | 224964412) >> 2 ^ 190562271) == 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var10003, var4, 0.0D, 0.0D, 0.0D, new int[459531814 >>> 2 << 4 ^ 1838127248]);
                  }

            }
      }
}
