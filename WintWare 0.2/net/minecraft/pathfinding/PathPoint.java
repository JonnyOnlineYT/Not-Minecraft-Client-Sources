package net.minecraft.pathfinding;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;

public class PathPoint {
   public final int xCoord;
   public final int yCoord;
   public final int zCoord;
   private final int hash;
   public int index = -1;
   public float totalPathDistance;
   public float distanceToNext;
   public float distanceToTarget;
   public PathPoint previous;
   public boolean visited;
   public float distanceFromOrigin;
   public float cost;
   public float costMalus;
   public PathNodeType nodeType;

   public PathPoint(int x, int y, int z) {
      this.nodeType = PathNodeType.BLOCKED;
      this.xCoord = x;
      this.yCoord = y;
      this.zCoord = z;
      this.hash = makeHash(x, y, z);
   }

   public PathPoint cloneMove(int x, int y, int z) {
      PathPoint pathpoint = new PathPoint(x, y, z);
      pathpoint.index = this.index;
      pathpoint.totalPathDistance = this.totalPathDistance;
      pathpoint.distanceToNext = this.distanceToNext;
      pathpoint.distanceToTarget = this.distanceToTarget;
      pathpoint.previous = this.previous;
      pathpoint.visited = this.visited;
      pathpoint.distanceFromOrigin = this.distanceFromOrigin;
      pathpoint.cost = this.cost;
      pathpoint.costMalus = this.costMalus;
      pathpoint.nodeType = this.nodeType;
      return pathpoint;
   }

   public static int makeHash(int x, int y, int z) {
      return y & 255 | (x & 32767) << 8 | (z & 32767) << 24 | (x < 0 ? Integer.MIN_VALUE : 0) | (z < 0 ? '耀' : 0);
   }

   public float distanceTo(PathPoint pathpointIn) {
      float f = (float)(pathpointIn.xCoord - this.xCoord);
      float f1 = (float)(pathpointIn.yCoord - this.yCoord);
      float f2 = (float)(pathpointIn.zCoord - this.zCoord);
      return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
   }

   public float distanceToSquared(PathPoint pathpointIn) {
      float f = (float)(pathpointIn.xCoord - this.xCoord);
      float f1 = (float)(pathpointIn.yCoord - this.yCoord);
      float f2 = (float)(pathpointIn.zCoord - this.zCoord);
      return f * f + f1 * f1 + f2 * f2;
   }

   public float distanceManhattan(PathPoint p_186281_1_) {
      float f = (float)Math.abs(p_186281_1_.xCoord - this.xCoord);
      float f1 = (float)Math.abs(p_186281_1_.yCoord - this.yCoord);
      float f2 = (float)Math.abs(p_186281_1_.zCoord - this.zCoord);
      return f + f1 + f2;
   }

   public boolean equals(Object p_equals_1_) {
      if (!(p_equals_1_ instanceof PathPoint)) {
         return false;
      } else {
         PathPoint pathpoint = (PathPoint)p_equals_1_;
         return this.hash == pathpoint.hash && this.xCoord == pathpoint.xCoord && this.yCoord == pathpoint.yCoord && this.zCoord == pathpoint.zCoord;
      }
   }

   public int hashCode() {
      return this.hash;
   }

   public boolean isAssigned() {
      return this.index >= 0;
   }

   public String toString() {
      return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
   }

   public static PathPoint createFromBuffer(PacketBuffer buf) {
      PathPoint pathpoint = new PathPoint(buf.readInt(), buf.readInt(), buf.readInt());
      pathpoint.distanceFromOrigin = buf.readFloat();
      pathpoint.cost = buf.readFloat();
      pathpoint.costMalus = buf.readFloat();
      pathpoint.visited = buf.readBoolean();
      pathpoint.nodeType = PathNodeType.values()[buf.readInt()];
      pathpoint.distanceToTarget = buf.readFloat();
      return pathpoint;
   }
}
