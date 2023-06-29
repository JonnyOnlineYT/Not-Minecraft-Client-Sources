//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module;

import java.lang.reflect.Field;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;

public class Wrapper
{
    private static Wrapper theWrapper;
    public static Minecraft mc;
    public static FontRenderer fr;
    
    public static Wrapper getInstance() {
        return Wrapper.theWrapper;
    }
    
    public static float getCooldown() {
        return Wrapper.mc.player.getCooledAttackStrength(0.0f);
    }
    
    public static Block getBlock(final BlockPos pos) {
        return Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
    }
    
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
    
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }
    
    public static World getWorld() {
        return (World)getMinecraft().world;
    }
    
    public static <T, E> void setPrivateValue(final Class<? super T> classToAccess, final T instance, final E value, final String... fieldNames) {
        try {
            findField(classToAccess, fieldNames).set(instance, value);
        }
        catch (Exception ex) {}
    }
    
    private static Field findField(final Class<?> clazz, final String... fieldNames) {
        Exception failed = null;
        final int length = fieldNames.length;
        int i = 0;
        while (i < length) {
            final String fieldName = fieldNames[i];
            try {
                final Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            }
            catch (Exception e) {
                failed = e;
                ++i;
                continue;
            }
            break;
        }
        return null;
    }
    
    static {
        Wrapper.theWrapper = new Wrapper();
        Wrapper.mc = Minecraft.getMinecraft();
        Wrapper.fr = Minecraft.getMinecraft().fontRenderer;
    }
}
