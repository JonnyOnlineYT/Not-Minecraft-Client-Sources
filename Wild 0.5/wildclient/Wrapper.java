//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.Packet;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;

public class Wrapper
{
    public static volatile Wrapper INSTANCE;
    
    public Minecraft mc() {
        return Minecraft.getMinecraft();
    }
    
    public EntityPlayerSP player() {
        return Wrapper.INSTANCE.mc().player;
    }
    
    public WorldClient world() {
        return Wrapper.INSTANCE.mc().world;
    }
    
    public GameSettings mcSettings() {
        return Wrapper.INSTANCE.mc().gameSettings;
    }
    
    public FontRenderer fontRenderer() {
        return Wrapper.INSTANCE.mc().fontRenderer;
    }
    
    public void sendPacket(final Packet packet) {
        this.player().connection.sendPacket(packet);
    }
    
    public InventoryPlayer inventory() {
        return this.player().inventory;
    }
    
    public PlayerControllerMP controller() {
        return Wrapper.INSTANCE.mc().playerController;
    }
    
    static {
        Wrapper.INSTANCE = new Wrapper();
    }
}
