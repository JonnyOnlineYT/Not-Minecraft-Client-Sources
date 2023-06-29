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
      public BooleanSetting heartSet = new BooleanSetting("Heart", this, (boolean)(1776751156 << 1 & 1413716624 ^ 1346502656));
      public BooleanSetting portalSet;
      public BooleanSetting spitSet;
      public BooleanSetting flameSett;
      public BooleanSetting cloudSet;
      public BooleanSetting smokeSet;
      public BooleanSetting fireWorkSet;
      public BooleanSetting redStoneSet;

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player == null) {
                  if (!"nefariousMoment".equals("i hope you catch fire ngl")) {
                        ;
                  }

            } else {
                  if (!"you probably spell youre as your".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  double var4;
                  WorldClient var10000;
                  EnumParticleTypes var10001;
                  double var10002;
                  EntityPlayerSP var10003;
                  double var10004;
                  if (this.heartSet.isEnabled()) {
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.HEART;
                        var10002 = mc.player.posX;
                        var10003 = mc.player;
                        if (!"idiot".equals("yo mama name maurice")) {
                              ;
                        }

                        var4 = var10003.posY + 0.2D;
                        var10004 = mc.player.posZ;
                        if ((1282430185 >> 3 << 3 ^ 613491128 ^ 1759606096) == 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var4, var10004, 0.0D, 0.0D, 0.0D, new int[((2091885652 | 899033241) & 1614977884 & 1600303139) >> 2 ^ 268476416]);
                  }

                  if (this.cloudSet.isEnabled()) {
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.CLOUD;
                        if (!"you're dogshit".equals("ape covered in human flesh")) {
                              ;
                        }

                        var10002 = mc.player.posX;
                        var4 = mc.player.posY + 0.2D;
                        Minecraft var5 = mc;
                        if ((922704703 >>> 2 >>> 3 & 9453358 ^ 1076274293) != 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var4, var5.player.posZ, 0.0D, 0.0D, 0.0D, new int[1712530218 >> 4 ^ 56270424 ^ 87796842]);
                        if ((1208287540 << 3 ^ 1076365728) == 0) {
                              ;
                        }
                  }

                  if (this.flameSett.isEnabled()) {
                        if (!"please get a girlfriend and stop cracking plugins".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        mc.world.spawnParticle(EnumParticleTypes.FLAME, mc.player.posX, mc.player.posY + 0.2D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[(1965748419 << 1 ^ 2027171228) >> 4 ^ -114811711]);
                  }

                  if (this.smokeSet.isEnabled()) {
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.SMOKE_LARGE;
                        EntityPlayerSP var2 = mc.player;
                        if (((2032093591 | 86479692) >>> 1 & 376934096 ^ 370642112) == 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var2.posX, mc.player.posY + 0.2D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[((1530649547 ^ 779937803) & 1726377671) >>> 1 << 2 ^ -930331264]);
                  }

                  if (this.redStoneSet.isEnabled()) {
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.REDSTONE;
                        var10002 = mc.player.posX;
                        var4 = mc.player.posY;
                        if (!"intentMoment".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var4 + 0.1D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[(1336377373 << 1 & 1107645489) >> 3 ^ 4235270]);
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.REDSTONE;
                        var10002 = mc.player.posX;
                        var4 = mc.player.posY;
                        if (((2445989 >>> 1 ^ 1196721) & 24358 & 16639 ^ 12301894) != 0) {
                              ;
                        }

                        var4 += 0.2D;
                        if (((1000594066 | 330836122) << 4 >>> 1 >> 3 >> 1 ^ 1510926018) != 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var4, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[(107012240 | 86904697 | 50599763) >> 4 >>> 4 ^ 487167]);
                        var10000 = mc.world;
                        if (((821505782 << 2 << 3 & 69275244) >>> 3 >> 4 ^ 1439520763) != 0) {
                              ;
                        }

                        var10000.spawnParticle(EnumParticleTypes.REDSTONE, mc.player.posX, mc.player.posY + 0.3D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[((699770606 | 509505854) >> 4 & 44754714) >> 4 ^ 2797169]);
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.REDSTONE;
                        var10002 = mc.player.posX;
                        var4 = mc.player.posY + 0.4D;
                        var10004 = mc.player.posZ;
                        if (((132909035 ^ 127883991 | 6027817) >> 3 ^ 1026046864) != 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var4, var10004, 0.0D, 0.0D, 0.0D, new int[(274736376 ^ 131901914) & 279761563 ^ 208948200 ^ 483970026]);
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.REDSTONE;
                        var10002 = mc.player.posX;
                        var4 = mc.player.posY + 0.5D;
                        EntityPlayerSP var6 = mc.player;
                        if ((42579688 >>> 3 ^ 697341152) != 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var4, var6.posZ, 0.0D, 0.0D, 0.0D, new int[916269587 << 4 >>> 2 ^ 443852876]);
                        if ((((34045957 | 22796690) >>> 3 & 7063037) >> 4 ^ 441363) == 0) {
                              ;
                        }
                  }

                  if (this.fireWorkSet.isEnabled()) {
                        mc.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, mc.player.posX, mc.player.posY + 0.5D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[138937108 >> 1 & 59606149 & 106438 ^ 128]);
                  }

                  boolean var3 = this.portalSet.isEnabled();
                  if ((843268067 >>> 3 & 67901372 ^ -1839521121) != 0) {
                        ;
                  }

                  if (var3) {
                        mc.world.spawnParticle(EnumParticleTypes.PORTAL, mc.player.posX, mc.player.posY + 0.2D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[((1706163295 ^ 1073333676) << 4 | 1317533281) ^ 1891602558 ^ -1640468721]);
                        mc.world.spawnParticle(EnumParticleTypes.PORTAL, mc.player.posX, mc.player.posY + 0.2D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[(1765957356 | 916775660 | 1805958028) >>> 4 & 53989840 ^ 53887440]);
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.PORTAL;
                        var10002 = mc.player.posX;
                        var10003 = mc.player;
                        if (((1531925368 >>> 2 & 224412948) << 4 & 521717087 ^ -2034611613) != 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var10003.posY + 0.2D, mc.player.posZ, 0.0D, 0.0D, 0.0D, new int[(411372578 | 258265071) >>> 2 >>> 4 ^ 8361791]);
                        if (!"please go outside".equals("please dont crack my plugin")) {
                              ;
                        }

                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.PORTAL;
                        if ((((530098265 | 464861930) & 187463483) >> 4 ^ 661279706) != 0) {
                              ;
                        }

                        var10002 = mc.player.posX;
                        var4 = mc.player.posY + 0.2D;
                        var10004 = mc.player.posZ;
                        int var10008 = ((1306242973 | 848812064 | 20266471) & 1623724077) >> 3 ^ 202965509;
                        if (!"shitted on you harder than archybot".equals("idiot")) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var4, var10004, 0.0D, 0.0D, 0.0D, new int[var10008]);
                        var10000 = mc.world;
                        var10001 = EnumParticleTypes.PORTAL;
                        var10002 = mc.player.posX;
                        var4 = mc.player.posY + 0.2D;
                        var10004 = mc.player.posZ;
                        int[] var7 = new int[(502070617 << 1 ^ 427837375) & 375701871 ^ 37781773];
                        if (((1427321544 >> 2 & 183938444) >> 3 ^ 1648394327) != 0) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var4, var10004, 0.0D, 0.0D, 0.0D, var7);
                        if (((34603840 | 24715174) >>> 4 >>> 1 << 3 ^ 479461755) != 0) {
                              ;
                        }
                  }

                  if (this.spitSet.isEnabled()) {
                        var10000 = mc.world;
                        if ((66274983 << 2 << 1 ^ -599534990) != 0) {
                              ;
                        }

                        var10001 = EnumParticleTypes.SPIT;
                        var10002 = mc.player.posX;
                        var4 = mc.player.posY + 0.2D;
                        var10004 = mc.player.posZ;
                        if (!"stringer is a good obfuscator".equals("intentMoment")) {
                              ;
                        }

                        var10000.spawnParticle(var10001, var10002, var4, var10004, 0.0D, 0.0D, 0.0D, new int[1124671976 >> 1 & 389829445 ^ 17043524]);
                  }

            }
      }

      public Particles() {
            super("Particles", "spawn particles", (1911813801 >> 2 >>> 2 ^ 85243699) << 4 ^ 548963728, Module.Category.RENDER);
            BooleanSetting var10001 = new BooleanSetting;
            int var10005 = 418 & 390 ^ 386;
            if ((1422456212 >>> 3 >>> 1 >> 1 ^ 43997738 ^ 3743110) == 0) {
                  ;
            }

            var10001.<init>("Cloud", this, (boolean)var10005);
            this.cloudSet = var10001;
            this.flameSett = new BooleanSetting("Flame", this, (boolean)((0 & 2067175835 | 2114427685) ^ 2114427684));
            this.smokeSet = new BooleanSetting("Smoke", this, (boolean)((1769549077 >>> 2 | 412420199 | 449903182 | 238136071) & 345276446 ^ 345276430));
            this.redStoneSet = new BooleanSetting("RedStone", this, (boolean)((1264594598 << 3 << 1 | 694557868) ^ -1083769108));
            var10001 = new BooleanSetting;
            if ((1309940209 << 4 >> 3 >> 1 ^ 1575393821) != 0) {
                  ;
            }

            var10001.<init>("FireWork", this, (boolean)((1705519828 | 1284834468) >>> 2 ^ 460278717));
            this.fireWorkSet = var10001;
            this.portalSet = new BooleanSetting("Portal", this, (boolean)(1091075151 >>> 3 << 4 >> 1 & 1529994719 ^ 1090542664));
            this.spitSet = new BooleanSetting("Spit", this, (boolean)(2029753037 << 1 << 1 << 3 ^ 527587744));
            if ((11666700 << 4 ^ -1880894640) != 0) {
                  ;
            }

            Setting[] var1 = new Setting[(4 ^ 1 | 4) & 4 ^ 12];
            int var10003 = 28509636 << 2 >> 3 >> 2 ^ 3563704;
            if (!"yo mama name maurice".equals("shitted on you harder than archybot")) {
                  ;
            }

            var1[var10003] = this.heartSet;
            var1[((0 ^ 884359670) & 625815209 ^ 180869485) & 75110952 ^ 71440393] = this.cloudSet;
            var1[(1 << 2 >>> 3 & 1997614363 | 178621216) ^ 178621218] = this.flameSett;
            var1[(1 ^ 0) >> 1 << 3 >>> 2 ^ 3] = this.smokeSet;
            var1[(2 >> 2 << 3 | 991593390) ^ 991593386] = this.redStoneSet;
            var10003 = ((2 | 0 | 0) << 1 | 2) ^ 3;
            BooleanSetting var10004 = this.fireWorkSet;
            if (!"your mom your dad the one you never had".equals("shitted on you harder than archybot")) {
                  ;
            }

            var1[var10003] = var10004;
            var1[(4 | 0 | 3) << 4 ^ 118] = this.portalSet;
            var1[(5 ^ 2) & 4 ^ 1 ^ 2] = this.spitSet;
            this.addSettings(var1);
      }
}
