package me.wintware.client.utils.animation;

import javafx.animation.TranslateTransition;

public class AnimationUtil {
   private static float defaultSpeed = 0.125F;
   private static TranslateTransition tt;

   public static float moveUD(float current, float end, float minSpeed) {
      return moveUD(current, end, defaultSpeed, minSpeed);
   }

   public static float moveUD(float current, float end, float smoothSpeed, float minSpeed) {
      float movement = (end - current) * smoothSpeed;
      if (movement > 0.0F) {
         movement = Math.max(minSpeed, movement);
         movement = Math.min(end - current, movement);
      } else if (movement < 0.0F) {
         movement = Math.min(-minSpeed, movement);
         movement = Math.max(end - current, movement);
      }

      return current + movement;
   }

   public static double animate(double target, double current, double speed) {
      boolean larger = target > current;
      if (speed < 0.0D) {
         speed = 0.0D;
      } else if (speed > 1.0D) {
         speed = 1.0D;
      }

      double dif = Math.max(target, current) - Math.min(target, current);
      double factor = dif * speed;
      if (factor < 0.1D) {
         factor = 0.1D;
      }

      if (larger) {
         current += factor;
      } else {
         current -= factor;
      }

      return current;
   }
}
