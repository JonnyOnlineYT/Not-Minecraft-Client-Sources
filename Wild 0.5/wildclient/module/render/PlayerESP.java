//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.ArrayList;
import black.nigger.wildclient.module.Category;
import net.minecraft.entity.Entity;
import java.util.List;
import black.nigger.wildclient.module.Module;

public class PlayerESP extends Module
{
    private transient List<Entity> ENTITIES;
    
    public PlayerESP() {
        super("PlayerESP", "Player ESP", Category.RENDER);
        this.ENTITIES = new ArrayList<Entity>();
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        this.ENTITIES.clear();
        for (final EntityPlayer entity : Minecraft.getMinecraft().world.playerEntities) {
            if (!entity.isDead) {
                if (entity.getHealth() < 0.0f) {
                    continue;
                }
                if (entity == Minecraft.getMinecraft().player) {
                    continue;
                }
                if (entity.isInvisible()) {
                    continue;
                }
                entity.setGlowing(true);
                this.ENTITIES.add((Entity)entity);
            }
        }
    }
}
