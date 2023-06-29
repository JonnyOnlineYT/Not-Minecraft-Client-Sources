//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.combat;

import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.world.GameType;
import net.minecraft.entity.player.EntityPlayer;
import black.nigger.wildclient.Wrapper;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class Reach extends Module
{
    public Reach() {
        super("Reach", "Reach", Category.COMBAT);
    }
    
    public static void setReach(final Entity entity, final double range) {
        final Minecraft mc = Wrapper.INSTANCE.mc();
        final EntityPlayer player = (EntityPlayer)Wrapper.INSTANCE.player();
        if (player == entity) {
            class 1RangePlayerController extends PlayerControllerMP
            {
                private float range;
                
                public 1RangePlayerController(final Minecraft mcIn, final NetHandlerPlayClient netHandler) {
                    super(mcIn, netHandler);
                    this.range = (float)Wrapper.INSTANCE.player().getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
                }
                
                public float getBlockReachDistance() {
                    return this.range;
                }
                
                public void setBlockReachDistance(final float range) {
                    this.range = range;
                }
            }
            if (!(mc.playerController instanceof 1RangePlayerController)) {
                final GameType gameType = (GameType)ReflectionHelper.getPrivateValue((Class)PlayerControllerMP.class, (Object)mc.playerController, new String[0]);
                final NetHandlerPlayClient netClient = (NetHandlerPlayClient)ReflectionHelper.getPrivateValue((Class)PlayerControllerMP.class, (Object)mc.playerController, new String[0]);
                final 1RangePlayerController controller = new 1RangePlayerController(mc, netClient);
                final boolean isFlying = player.capabilities.isFlying;
                final boolean allowFlying = player.capabilities.allowFlying;
                controller.setGameType(gameType);
                player.capabilities.isFlying = isFlying;
                player.capabilities.allowFlying = allowFlying;
                mc.playerController = controller;
            }
            ((1RangePlayerController)mc.playerController).setBlockReachDistance((float)range);
        }
    }
    
    @Override
    public void onEnable() {
        setReach((Entity)Reach.mc.player, 10.0);
    }
    
    @Override
    public void onDisable() {
        setReach((Entity)Reach.mc.player, 3.2);
    }
}
