package me.wintware.client.utils.animation;

public final class Translate {
   private double x;
   private double y;

   public Translate(float x, float y) {
      this.x = (double)x;
      this.y = (double)y;
   }

   public final void interpolate(double targetX, double targetY, double smoothing) {
      this.x = AnimationUtil.animate(targetX, this.x, smoothing);
      this.y = AnimationUtil.animate(targetY, this.y, smoothing);
   }

   public void animate(double newX, double newY) {
      this.x = AnimationUtil.animate(this.x, newX, 1.0D);
      this.y = AnimationUtil.animate(this.y, newY, 1.0D);
   }

   public double getX() {
      return this.x;
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getY() {
      return this.y;
   }

   public void setY(double y) {
      this.y = y;
   }
}
