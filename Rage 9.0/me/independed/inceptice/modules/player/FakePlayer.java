//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.passive.EntityDonkey;

public class FakePlayer extends Module {
      private EntityOtherPlayerMP Original;
      private EntityDonkey RidingEntity;

      public FakePlayer() {
            int var10003 = 159567406 << 2 >>> 1 ^ 319134812;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please go outside")) {
                  ;
            }

            super("FakePlayer", "second player", var10003, Module.Category.PLAYER);
      }

      public void onDisable() {
            if (mc.player == null) {
                  if (((308706799 | 113764434 | 50969829 | 290231189) ^ 722260362) != 0) {
                        ;
                  }

            } else {
                  super.onDisable();
                  if (this.Original != null) {
                        if (this.Original.isRiding()) {
                              EntityOtherPlayerMP var10000 = this.Original;
                              if (((1498873838 | 1256157407) & 575389250 ^ -1731293913) != 0) {
                                    ;
                              }

                              var10000.dismountRidingEntity();
                        }

                        mc.world.removeEntity(this.Original);
                  }

                  if (this.RidingEntity != null) {
                        mc.world.removeEntity(this.RidingEntity);
                  }

            }
      }

      public void onEnable() {
            if (mc.player != null) {
                  if (mc.world == null) {
                        this.toggle();
                  } else {
                        if (((1142166882 << 1 ^ 238485056) << 4 ^ 1642940480) == 0) {
                              ;
                        }

                        this.Original = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
                        this.Original.copyLocationAndAnglesFrom(mc.player);
                        this.Original.rotationYaw = mc.player.rotationYaw;
                        this.Original.rotationYawHead = mc.player.rotationYawHead;
                        this.Original.inventory.copyInventory(mc.player.inventory);
                        mc.world.addEntityToWorld(((517431770 ^ 355826196) << 1 | 117836839) ^ -399097922, this.Original);
                        if (mc.player.isRiding() && mc.player.getRidingEntity() instanceof EntityDonkey) {
                              EntityDonkey var1 = (EntityDonkey)mc.player.getRidingEntity();
                              EntityDonkey var10001 = new EntityDonkey;
                              WorldClient var10003 = mc.world;
                              if (((384912980 >>> 3 | 44386038) ^ -1094316357) != 0) {
                                    ;
                              }

                              var10001.<init>(var10003);
                              this.RidingEntity = var10001;
                              this.RidingEntity.copyLocationAndAnglesFrom(var1);
                              this.RidingEntity.setChested(var1.hasChest());
                              mc.world.addEntityToWorld((274924820 ^ 196946070 ^ 243951612) << 2 ^ -1432278534, this.RidingEntity);
                              if (!"stringer is a good obfuscator".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              this.Original.startRiding(this.RidingEntity, (boolean)(0 << 4 >> 1 >>> 2 ^ 1));
                        }

                  }
            }
      }
}
