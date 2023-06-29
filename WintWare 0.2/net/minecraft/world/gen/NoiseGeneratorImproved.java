package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorImproved extends NoiseGenerator {
   private final int[] permutations;
   public double xCoord;
   public double yCoord;
   public double zCoord;
   private static final double[] GRAD_X = new double[]{1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D};
   private static final double[] GRAD_Y = new double[]{1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D};
   private static final double[] GRAD_Z = new double[]{0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D, 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D};
   private static final double[] GRAD_2X = new double[]{1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D};
   private static final double[] GRAD_2Z = new double[]{0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D, 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D};

   public NoiseGeneratorImproved() {
      this(new Random());
   }

   public NoiseGeneratorImproved(Random p_i45469_1_) {
      this.permutations = new int[512];
      this.xCoord = p_i45469_1_.nextDouble() * 256.0D;
      this.yCoord = p_i45469_1_.nextDouble() * 256.0D;
      this.zCoord = p_i45469_1_.nextDouble() * 256.0D;

      int l;
      for(l = 0; l < 256; this.permutations[l] = l++) {
      }

      for(l = 0; l < 256; ++l) {
         int j = p_i45469_1_.nextInt(256 - l) + l;
         int k = this.permutations[l];
         this.permutations[l] = this.permutations[j];
         this.permutations[j] = k;
         this.permutations[l + 256] = this.permutations[l];
      }

   }

   public final double lerp(double p_76311_1_, double p_76311_3_, double p_76311_5_) {
      return p_76311_3_ + p_76311_1_ * (p_76311_5_ - p_76311_3_);
   }

   public final double grad2(int p_76309_1_, double p_76309_2_, double p_76309_4_) {
      int i = p_76309_1_ & 15;
      return GRAD_2X[i] * p_76309_2_ + GRAD_2Z[i] * p_76309_4_;
   }

   public final double grad(int p_76310_1_, double p_76310_2_, double p_76310_4_, double p_76310_6_) {
      int i = p_76310_1_ & 15;
      return GRAD_X[i] * p_76310_2_ + GRAD_Y[i] * p_76310_4_ + GRAD_Z[i] * p_76310_6_;
   }

   public void populateNoiseArray(double[] noiseArray, double xOffset, double yOffset, double zOffset, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale, double noiseScale) {
      int l1;
      double d2;
      double d4;
      int j6;
      double d5;
      int k6;
      int j3;
      double d6;
      int i5;
      int k;
      if (ySize == 1) {
         int i5 = false;
         int j5 = false;
         int j = false;
         int k5 = false;
         double d14 = 0.0D;
         double d15 = 0.0D;
         l1 = 0;
         double d16 = 1.0D / noiseScale;

         for(int j2 = 0; j2 < xSize; ++j2) {
            d2 = xOffset + (double)j2 * xScale + this.xCoord;
            int i6 = (int)d2;
            if (d2 < (double)i6) {
               --i6;
            }

            int k2 = i6 & 255;
            d2 -= (double)i6;
            d4 = d2 * d2 * d2 * (d2 * (d2 * 6.0D - 15.0D) + 10.0D);

            for(j6 = 0; j6 < zSize; ++j6) {
               d5 = zOffset + (double)j6 * zScale + this.zCoord;
               k6 = (int)d5;
               if (d5 < (double)k6) {
                  --k6;
               }

               j3 = k6 & 255;
               d5 -= (double)k6;
               d6 = d5 * d5 * d5 * (d5 * (d5 * 6.0D - 15.0D) + 10.0D);
               i5 = this.permutations[k2] + 0;
               int j5 = this.permutations[i5] + j3;
               int j = this.permutations[k2 + 1] + 0;
               k = this.permutations[j] + j3;
               d14 = this.lerp(d4, this.grad2(this.permutations[j5], d2, d5), this.grad(this.permutations[k], d2 - 1.0D, 0.0D, d5));
               d15 = this.lerp(d4, this.grad(this.permutations[j5 + 1], d2, 0.0D, d5 - 1.0D), this.grad(this.permutations[k + 1], d2 - 1.0D, 0.0D, d5 - 1.0D));
               double d21 = this.lerp(d6, d14, d15);
               int i7 = l1++;
               noiseArray[i7] += d21 * d16;
            }
         }
      } else {
         i5 = 0;
         double d0 = 1.0D / noiseScale;
         k = -1;
         int l = false;
         int i1 = false;
         int j1 = false;
         int k1 = false;
         int l1 = false;
         int i2 = false;
         double d1 = 0.0D;
         d2 = 0.0D;
         double d3 = 0.0D;
         d4 = 0.0D;

         for(j6 = 0; j6 < xSize; ++j6) {
            d5 = xOffset + (double)j6 * xScale + this.xCoord;
            k6 = (int)d5;
            if (d5 < (double)k6) {
               --k6;
            }

            j3 = k6 & 255;
            d5 -= (double)k6;
            d6 = d5 * d5 * d5 * (d5 * (d5 * 6.0D - 15.0D) + 10.0D);

            for(int k3 = 0; k3 < zSize; ++k3) {
               double d7 = zOffset + (double)k3 * zScale + this.zCoord;
               int l3 = (int)d7;
               if (d7 < (double)l3) {
                  --l3;
               }

               int i4 = l3 & 255;
               d7 -= (double)l3;
               double d8 = d7 * d7 * d7 * (d7 * (d7 * 6.0D - 15.0D) + 10.0D);

               for(int j4 = 0; j4 < ySize; ++j4) {
                  double d9 = yOffset + (double)j4 * yScale + this.yCoord;
                  int k4 = (int)d9;
                  if (d9 < (double)k4) {
                     --k4;
                  }

                  int l4 = k4 & 255;
                  d9 -= (double)k4;
                  double d10 = d9 * d9 * d9 * (d9 * (d9 * 6.0D - 15.0D) + 10.0D);
                  if (j4 == 0 || l4 != k) {
                     k = l4;
                     int l = this.permutations[j3] + l4;
                     int i1 = this.permutations[l] + i4;
                     int j1 = this.permutations[l + 1] + i4;
                     int k1 = this.permutations[j3 + 1] + l4;
                     l1 = this.permutations[k1] + i4;
                     int i2 = this.permutations[k1 + 1] + i4;
                     d1 = this.lerp(d6, this.grad(this.permutations[i1], d5, d9, d7), this.grad(this.permutations[l1], d5 - 1.0D, d9, d7));
                     d2 = this.lerp(d6, this.grad(this.permutations[j1], d5, d9 - 1.0D, d7), this.grad(this.permutations[i2], d5 - 1.0D, d9 - 1.0D, d7));
                     d3 = this.lerp(d6, this.grad(this.permutations[i1 + 1], d5, d9, d7 - 1.0D), this.grad(this.permutations[l1 + 1], d5 - 1.0D, d9, d7 - 1.0D));
                     d4 = this.lerp(d6, this.grad(this.permutations[j1 + 1], d5, d9 - 1.0D, d7 - 1.0D), this.grad(this.permutations[i2 + 1], d5 - 1.0D, d9 - 1.0D, d7 - 1.0D));
                  }

                  double d11 = this.lerp(d10, d1, d2);
                  double d12 = this.lerp(d10, d3, d4);
                  double d13 = this.lerp(d8, d11, d12);
                  int j7 = i5++;
                  noiseArray[j7] += d13 * d0;
               }
            }
         }
      }

   }
}
