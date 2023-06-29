/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils.accessor;

import net.minecraft.util.math.BlockPos;

public interface IPlayerControllerMP {
     void setIsHittingBlock(boolean var1);

     BlockPos getCurrentBlock();

     void callSyncCurrentPlayItem();
}

