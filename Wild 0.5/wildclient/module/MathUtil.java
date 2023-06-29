//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module;

import net.minecraft.util.math.Vec3d;

public class MathUtil
{
    public static float interpolateFloat(final float current, final float last, final float partialTicks) {
        final float interpolation = -(current - last) + (current - last) * partialTicks;
        return current + interpolation;
    }
    
    public static Vec3d interpolateVec3d(final Vec3d current, final Vec3d last, final float partialTicks) {
        return current.subtract(last).scale((double)partialTicks).add(last);
    }
}
