package me.independed.inceptice.particles;

import java.util.Random;
import javax.vecmath.Vector2f;
import org.lwjgl.opengl.Display;

public class Particle {
      private Vector2f pos;
      private static final Random random = new Random();
      private Vector2f velocity;
      private float alpha;
      private float size;

      public double distance(float var1, float var2, float var3, float var4) {
            return Math.sqrt((double)((var1 - var3) * (var1 - var3) + (var2 - var4) * (var2 - var4)));
      }

      public void tick(int var1, float var2) {
            if ((((1187942383 >>> 4 ^ 27583892) & 35361562 | 101921) ^ 626475) == 0) {
                  ;
            }

            Vector2f var10000 = this.pos;
            var10000.x += this.velocity.getX() * (float)var1 * var2;
            var10000 = this.pos;
            var10000.y += this.velocity.getY() * (float)var1 * var2;
            if (this.alpha < 255.0F) {
                  if ((84414608 >> 4 ^ 5275913) == 0) {
                        ;
                  }

                  float var10001 = this.alpha;
                  float var10002 = 0.05F * (float)var1;
                  if (((580722178 ^ 401636146) >>> 3 ^ 1055490003) != 0) {
                        ;
                  }

                  this.alpha = var10001 + var10002;
            }

            if (this.pos.getX() > (float)Display.getWidth()) {
                  if ((1545718546 << 4 >> 4 << 3 ^ -519153520) == 0) {
                        ;
                  }

                  this.pos.setX(0.0F);
            }

            var10000 = this.pos;
            if (((1622296897 ^ 1369465464) >>> 3 ^ 102909287) == 0) {
                  ;
            }

            if (var10000.getX() < 0.0F) {
                  this.pos.setX((float)Display.getWidth());
            }

            if (this.pos.getY() > (float)Display.getHeight()) {
                  this.pos.setY(0.0F);
            }

            if ((1887280688 << 3 >>> 4 ^ 132388202 ^ 266001010) == 0) {
                  ;
            }

            if (this.pos.getY() < 0.0F) {
                  var10000 = this.pos;
                  if ((((1950475572 ^ 159375660) & 713364712) << 4 >> 1 << 1 ^ -2143272832) == 0) {
                        ;
                  }

                  var10000.setY((float)Display.getHeight());
            }

      }

      public void setVelocity(Vector2f var1) {
            this.velocity = var1;
      }

      public void setY(float var1) {
            this.pos.setY(var1);
      }

      public void setX(float var1) {
            if ((1728833235 >>> 3 >>> 4 ^ 13506509) == 0) {
                  ;
            }

            this.pos.setX(var1);
      }

      public Particle(Vector2f var1, float var2, float var3, float var4) {
            this.velocity = var1;
            Vector2f var10001 = new Vector2f;
            if (((1885648650 | 33554984) << 3 << 4 ^ 844862720) == 0) {
                  ;
            }

            var10001.<init>(var2, var3);
            this.pos = var10001;
            this.size = var4;
      }

      static {
            if (((1767111138 | 517481704) & 47504686 ^ 8856298 ^ -547718957) != 0) {
                  ;
            }

      }

      public void setSize(float var1) {
            this.size = var1;
      }

      public float getDistanceTo(float var1, float var2) {
            return (float)this.distance(this.getX(), this.getY(), var1, var2);
      }

      public float getDistanceTo(Particle var1) {
            float var10001 = var1.getX();
            float var10002 = var1.getY();
            if (!"yo mama name maurice".equals("idiot")) {
                  ;
            }

            return this.getDistanceTo(var10001, var10002);
      }

      public float getX() {
            return this.pos.getX();
      }

      public Vector2f getVelocity() {
            if ((16927760 >>> 4 ^ 121610947) != 0) {
                  ;
            }

            return this.velocity;
      }

      public float getY() {
            return this.pos.getY();
      }

      public static Particle generateParticle() {
            Vector2f var0 = new Vector2f((float)(Math.random() * 2.0D - 1.0D), (float)(Math.random() * 2.0D - 1.0D));
            float var1 = (float)random.nextInt(Display.getWidth());
            float var2 = (float)random.nextInt(Display.getHeight());
            float var10000 = (float)(Math.random() * 4.0D);
            if (!"please take a shower".equals("you're dogshit")) {
                  ;
            }

            ++var10000;
            if (!"you probably spell youre as your".equals("i hope you catch fire ngl")) {
                  ;
            }

            float var3 = var10000;
            Particle var4 = new Particle;
            if (((62650025 | 48100291) >> 3 ^ 805977 ^ 108026055) != 0) {
                  ;
            }

            var4.<init>(var0, var1, var2, var3);
            if (!"you probably spell youre as your".equals("stringer is a good obfuscator")) {
                  ;
            }

            return var4;
      }

      public float getAlpha() {
            if (!"please dont crack my plugin".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            return this.alpha;
      }

      public float getSize() {
            if ((((1212564282 >> 2 ^ 27509296) >>> 4 | 9852018) ^ 1720073681) != 0) {
                  ;
            }

            return this.size;
      }
}
