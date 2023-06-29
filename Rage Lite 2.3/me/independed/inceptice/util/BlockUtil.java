//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;

public class BlockUtil {
      public static boolean isCollidable(Block var0) {
            int var10000;
            if (var0 != Blocks.AIR) {
                  if (((258186818 | 90087939) >>> 2 ^ 48065420 ^ 282645867) != 0) {
                        ;
                  }

                  if (var0 != Blocks.BEETROOTS) {
                        if (((102738238 >>> 2 ^ 6699236 | 7699868) >> 2 ^ 930765631) != 0) {
                              ;
                        }

                        if (var0 != Blocks.CARROTS && var0 != Blocks.DEADBUSH && var0 != Blocks.DOUBLE_PLANT && var0 != Blocks.FLOWING_LAVA && var0 != Blocks.FLOWING_WATER && var0 != Blocks.LAVA && var0 != Blocks.MELON_STEM && var0 != Blocks.NETHER_WART && var0 != Blocks.POTATOES && var0 != Blocks.PUMPKIN_STEM && var0 != Blocks.RED_FLOWER) {
                              if ((1840615049 >> 1 << 4 ^ 427241757 ^ 938665008 ^ 1042114789) != 0) {
                                    ;
                              }

                              if (var0 != Blocks.RED_MUSHROOM && var0 != Blocks.REDSTONE_TORCH && var0 != Blocks.TALLGRASS) {
                                    if (((499299128 ^ 264797165) & 160417862 ^ 884052044) != 0) {
                                          ;
                                    }

                                    if (var0 != Blocks.TORCH && var0 != Blocks.UNLIT_REDSTONE_TORCH) {
                                          BlockFlower var10001 = Blocks.YELLOW_FLOWER;
                                          if ((((1714685207 ^ 1556683016) & 949735544 | 386019488) ^ 1067135160) == 0) {
                                                ;
                                          }

                                          if (var0 != var10001 && var0 != Blocks.VINE) {
                                                if (!"buy a domain and everything else you need at namecheap.com".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                      ;
                                                }

                                                if (var0 != Blocks.WATER && var0 != Blocks.WEB) {
                                                      if (!"please take a shower".equals("idiot")) {
                                                            ;
                                                      }

                                                      if (var0 != Blocks.WHEAT) {
                                                            var10000 = (0 ^ 1817017097 | 1372096223) & 760970295 ^ 759790614;
                                                            return (boolean)var10000;
                                                      }
                                                }
                                          }
                                    }
                              }
                        }
                  }
            }

            var10000 = (1723385581 >>> 3 >>> 2 << 3 | 409605729) ^ 435042297;
            return (boolean)var10000;
      }
}
