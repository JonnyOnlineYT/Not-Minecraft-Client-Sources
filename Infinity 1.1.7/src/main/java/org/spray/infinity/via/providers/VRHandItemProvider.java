package org.spray.infinity.via.providers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.HandItemProvider;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class VRHandItemProvider extends HandItemProvider {
    public Item clientItem = null;
    public Map<UUID, Item> serverPlayers = new ConcurrentHashMap<>();

    @Override
    public Item getHandItem(UserConnection info) {
        Item serverItem;
        if (info.isClientSide()) {
            return getClientItem();
        } else if ((serverItem = serverPlayers.get(info.getProtocolInfo().getUuid())) != null) {
            return new DataItem(serverItem);
        }
        return super.getHandItem(info);
    }

    private Item getClientItem() {
        if (clientItem == null) {
            return new DataItem(0, (byte) 0, (short) 0, null);
        }
        return new DataItem(clientItem);
    }


    private void tickClient() {
        ClientPlayerEntity p = MinecraftClient.getInstance().player;
        if (p != null) {
            clientItem = fromNative(p.getInventory().getMainHandStack());
        }
    }

    private void tickServer(World world) {
        serverPlayers.clear();
        world.getPlayers().forEach(it -> serverPlayers
                .put(it.getUuid(), fromNative(it.getInventory().getMainHandStack())));
    }

    private Item fromNative(ItemStack original) {
        Identifier iid = Registry.ITEM.getId(original.getItem());
        if (iid == null) return new DataItem(0, (byte) 0, (short) 0, null);
        int id = swordId(iid.toString());
        return new DataItem(id, (byte) original.getCount(), (short) original.getDamage(), null);
    }

    private int swordId(String id) {
        // https://github.com/ViaVersion/ViaVersion/blob/8de26a0ad33f5b739f5394ed80f69d14197fddc7/common/src/main/java/us/myles/ViaVersion/protocols/protocol1_9to1_8/Protocol1_9To1_8.java#L86
        switch (id) {
            case "minecraft:iron_sword":
                return 267;
            case "minecraft:wooden_sword":
                return 268;
            case "minecraft:golden_sword":
                return 272;
            case "minecraft:diamond_sword":
                return 276;
            case "minecraft:stone_sword":
                return 283;
        }
        return 0;
    }
}
