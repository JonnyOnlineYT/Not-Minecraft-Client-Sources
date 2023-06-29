//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

public class EntityBot {
      private boolean invisible;
      private UUID uuid;
      private int id;
      private String name;
      private boolean ground;

      public EntityBot(EntityPlayer var1) {
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("intentMoment")) {
                  ;
            }

            super();
            if (((184127873 >>> 1 >>> 1 ^ 39320521 | 10830443) & 14489461 ^ 1662053975) != 0) {
                  ;
            }

            if (((2072975334 >>> 4 & 73514708) << 1 ^ -1395727440) != 0) {
                  ;
            }

            this.name = String.valueOf(var1.getGameProfile().getName());
            this.id = var1.getEntityId();
            GameProfile var10001 = var1.getGameProfile();
            if (((1084795392 ^ 860282098 | 20292127) & 743711624 ^ 542384776) == 0) {
                  ;
            }

            this.uuid = var10001.getId();
            this.invisible = var1.isInvisible();
            this.ground = var1.onGround;
            if (((516067720 | 359392564) << 2 ^ 747670778) != 0) {
                  ;
            }

      }

      public boolean isGround() {
            return this.ground;
      }

      public UUID getUuid() {
            return this.uuid;
      }

      public boolean isInvisible() {
            return this.invisible;
      }

      public String getName() {
            return this.name;
      }

      public int getId() {
            return this.id;
      }
}
