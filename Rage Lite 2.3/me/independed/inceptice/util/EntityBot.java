//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

public class EntityBot {
      private boolean invisible;
      private UUID uuid;
      private String name;
      private int id;
      private boolean ground;

      public EntityBot(EntityPlayer var1) {
            this.name = String.valueOf(var1.getGameProfile().getName());
            this.id = var1.getEntityId();
            this.uuid = var1.getGameProfile().getId();
            this.invisible = var1.isInvisible();
            this.ground = var1.onGround;
            if ((((559739470 ^ 368272908) >>> 2 ^ 160863749 ^ 50730633) & 24898224 ^ 1106890196) != 0) {
                  ;
            }

      }

      public int getId() {
            return this.id;
      }

      public boolean isGround() {
            return this.ground;
      }

      public String getName() {
            return this.name;
      }

      public boolean isInvisible() {
            return this.invisible;
      }

      public UUID getUuid() {
            return this.uuid;
      }
}
