package net.minecraft.util;

import net.minecraft.client.settings.GameSettings;

public class MovementInputFromOptions extends MovementInput {
   private final GameSettings gameSettings;

   public MovementInputFromOptions(GameSettings gameSettingsIn) {
      this.gameSettings = gameSettingsIn;
   }

   public void updatePlayerMoveState() {
      moveStrafe = 0.0F;
      moveForward = 0.0F;
      if (this.gameSettings.keyBindForward.isKeyDown()) {
         ++moveForward;
         this.forwardKeyDown = true;
      } else {
         this.forwardKeyDown = false;
      }

      if (this.gameSettings.keyBindBack.isKeyDown()) {
         --moveForward;
         this.backKeyDown = true;
      } else {
         this.backKeyDown = false;
      }

      if (this.gameSettings.keyBindLeft.isKeyDown()) {
         ++moveStrafe;
         this.leftKeyDown = true;
      } else {
         this.leftKeyDown = false;
      }

      if (this.gameSettings.keyBindRight.isKeyDown()) {
         --moveStrafe;
         this.rightKeyDown = true;
      } else {
         this.rightKeyDown = false;
      }

      this.jump = this.gameSettings.keyBindJump.isKeyDown();
      this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
      if (this.sneak) {
         moveStrafe = (float)((double)moveStrafe * 0.3D);
         moveForward = (float)((double)moveForward * 0.3D);
      }

   }
}
