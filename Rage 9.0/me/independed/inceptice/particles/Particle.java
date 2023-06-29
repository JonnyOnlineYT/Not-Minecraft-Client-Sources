package me.independed.inceptice.particles;

import java.util.Random;
import javax.vecmath.Vector2f;
import org.lwjgl.opengl.Display;

public class Particle {
      private float size;
      private float alpha;
      private Vector2f velocity;
      private static final Random random;
      private Vector2f pos;

      public float getX() {
            return this.pos.getX();
      }

      public float getAlpha() {
            float var10000 = this.alpha;
            if (((218415307 | 49110600) & 255613279 & 154890464 ^ 153636928) == 0) {
                  ;
            }

            return var10000;
      }

      public float getDistanceTo(Particle var1) {
            float var10001 = var1.getX();
            float var10002 = var1.getY();
            if ((4521988 ^ 4521988) == 0) {
                  ;
            }

            return this.getDistanceTo(var10001, var10002);
      }

      public static Particle generateParticle() {
            Vector2f var0 = new Vector2f((float)(Math.random() * 2.0D - 1.0D), (float)(Math.random() * 2.0D - 1.0D));
            float var1 = (float)random.nextInt(Display.getWidth());
            if ((111873969 << 1 >>> 2 << 1 ^ -246618188) != 0) {
                  ;
            }

            float var2 = (float)random.nextInt(Display.getHeight());
            float var3 = (float)(Math.random() * 4.0D) + 1.0F;
            Particle var10000 = new Particle;
            if ((1561492291 << 3 << 2 << 2 ^ -1992449664) == 0) {
                  ;
            }

            var10000.<init>(var0, var1, var2, var3);
            return var10000;
      }

      public void setVelocity(Vector2f var1) {
            this.velocity = var1;
      }

      public float getDistanceTo(float var1, float var2) {
            return (float)this.distance(this.getX(), this.getY(), var1, var2);
      }

      public void tick(int var1, float var2) {
            Vector2f var10000 = this.pos;
            float var10001 = var10000.x;
            float var10002 = this.velocity.getX() * (float)var1 * var2;
            if (!"intentMoment".equals("shitted on you harder than archybot")) {
                  ;
            }

            var10000.x = var10001 + var10002;
            var10000 = this.pos;
            var10001 = var10000.y;
            var10002 = this.velocity.getY() * (float)var1 * var2;
            if (((1339242884 >>> 1 >>> 1 ^ 178911429) >> 2 ^ -1896028325) != 0) {
                  ;
            }

            var10000.y = var10001 + var10002;
            if (!"please go outside".equals("please take a shower")) {
                  ;
            }

            if ((((605139648 ^ 86501877) >> 1 >>> 2 | 38137564 | 75118643) ^ 109051903) == 0) {
                  ;
            }

            float var3 = this.alpha;
            if ((203735297 >> 1 >> 4 ^ 6321648 ^ -290136818) != 0) {
                  ;
            }

            if (var3 < 255.0F) {
                  if (((2010822512 >> 2 >>> 1 & 68543776) << 3 ^ -194313093) != 0) {
                        ;
                  }

                  var10001 = this.alpha + 0.05F * (float)var1;
                  if (((342677901 >> 2 & 69620315 ^ 53676757 ^ 82174765) >> 2 ^ 16072430) == 0) {
                        ;
                  }

                  this.alpha = var10001;
            }

            var3 = this.pos.getX();
            var10001 = (float)Display.getWidth();
            if ((940881174 >> 2 >>> 3 >> 1 ^ 14701268) == 0) {
                  ;
            }

            if (var3 > var10001) {
                  var10000 = this.pos;
                  if ((((1978499181 ^ 795616444) << 2 >>> 1 & 536854046) >> 1 ^ 176260097) == 0) {
                        ;
                  }

                  var10000.setX(0.0F);
            }

            if (this.pos.getX() < 0.0F) {
                  if (!"please get a girlfriend and stop cracking plugins".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  this.pos.setX((float)Display.getWidth());
            }

            float var5;
            int var4 = (var5 = this.pos.getY() - (float)Display.getHeight()) == 0.0F ? 0 : (var5 < 0.0F ? -1 : 1);
            if ((16777217 << 3 ^ 134217736) == 0) {
                  ;
            }

            if (var4 > 0) {
                  if ((486782859 << 4 & 291905542 ^ -289912287) != 0) {
                        ;
                  }

                  this.pos.setY(0.0F);
            }

            var10000 = this.pos;
            if (!"please dont crack my plugin".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            if (var10000.getY() < 0.0F) {
                  if ((33849636 >> 2 << 2 ^ 33849636) == 0) {
                        ;
                  }

                  this.pos.setY((float)Display.getHeight());
            }

      }

      static {
            if ((((39621515 | 39258935) >> 3 ^ 232427) >>> 1 ^ 250066 ^ 77374178) != 0) {
                  ;
            }

            Random var10000 = new Random;
            if (!"please dont crack my plugin".equals("you're dogshit")) {
                  ;
            }

            var10000.<init>();
            random = var10000;
            if ((558096 >>> 1 >> 4 ^ -1136757667) != 0) {
                  ;
            }

      }

      public Vector2f getVelocity() {
            if ((272773443 << 2 >>> 4 ^ 68193360) == 0) {
                  ;
            }

            return this.velocity;
      }

      public double distance(float var1, float var2, float var3, float var4) {
            if (!"your mom your dad the one you never had".equals("stop skidding")) {
                  ;
            }

            return Math.sqrt((double)((var1 - var3) * (var1 - var3) + (var2 - var4) * (var2 - var4)));
      }

      public void setSize(float var1) {
            this.size = var1;
      }

      public float getY() {
            return this.pos.getY();
      }

      public Particle(Vector2f var1, float var2, float var3, float var4) {
            this.velocity = var1;
            this.pos = new Vector2f(var2, var3);
            this.size = var4;
      }

      public void setY(float var1) {
            this.pos.setY(var1);
      }

      public void setX(float var1) {
            if (!"you probably spell youre as your".equals("your mom your dad the one you never had")) {
                  ;
            }

            if (!"ape covered in human flesh".equals("stringer is a good obfuscator")) {
                  ;
            }

            this.pos.setX(var1);
      }

      public float getSize() {
            return this.size;
      }
}
