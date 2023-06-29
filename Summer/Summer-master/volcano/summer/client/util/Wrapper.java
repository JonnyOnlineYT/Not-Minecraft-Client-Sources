package volcano.summer.client.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public final class Wrapper {

    public static EntityRenderer getEntityRenderer() {
        return getMinecraft().entityRenderer;
    }

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static EntityPlayerSP getPlayer() {
        return getMinecraft().thePlayer;
    }

    public static WorldClient getWorld() {
        return getMinecraft().theWorld;
    }

    public static FontRenderer getFontRenderer() {
        return getMinecraft().fontRendererObj;
    }

    public static PlayerControllerMP getPlayerController() {
        return getMinecraft().playerController;
    }

    public static NetHandlerPlayClient getSendQueue() {
        return getPlayer().sendQueue;
    }

    public static GameSettings getGameSettings() {
        return getMinecraft().gameSettings;
    }

    public static InventoryPlayer getInventory() {
        return getPlayer().inventory;
    }

    public static String getDirection() {
        return getMinecraft().getRenderViewEntity().getHorizontalFacing().name();
    }

    public static ScaledResolution getResolution() {
        return new ScaledResolution(getMinecraft(), 0, 0);
    }

    public static Timer getTimer() {
        return getMinecraft().getTimer();
    }

    public static Block getBlock(BlockPos pos) {
        return getMinecraft().theWorld.getBlockState(pos).getBlock();
    }

    public static void addChatMessage(String message) {
        getPlayer().addChatMessage(new ChatComponentText(message));
    }

    public static GuiScreen getCurrentScreen() {
        return getMinecraft().currentScreen;
    }

    public static List<Entity> getLoadedEntities() {
        return getWorld().getLoadedEntityList();
    }

    public static List<EntityPlayer> getLoadedPlayers() {
        return getWorld().playerEntities;
    }

    public static List<EntityPlayer> getLoadedPlayersNoNPCs() {
        List<EntityPlayer> loadedPlayers = new ArrayList<>(getLoadedPlayers());
        loadedPlayers.removeIf(player -> getPlayer().sendQueue.func_175102_a(player.getUniqueID()) == null);
        return loadedPlayers;
    }

    public static List<TileEntity> getLoadedTileEntities() {
        return getWorld().loadedTileEntityList;
    }

    public static void sendPacket(Packet<?> packet) {
        getSendQueue().addToSendQueue(packet);
    }

    public static void sendPacketDirect(Packet<?> packet) {
        getSendQueue().getNetworkManager().sendPacket(packet);
    }

}
