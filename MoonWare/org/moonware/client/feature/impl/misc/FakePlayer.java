package org.moonware.client.feature.impl.misc;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;

import java.util.UUID;

public class FakePlayer extends Feature {

    public FakePlayer() {
        super("FakePlayer", "", Type.Other);
    }
    private final BooleanSetting armor = new BooleanSetting("Armors", false);

    private ItemStack[] armors = { new ItemStack(Items.DIAMOND_BOOTS),
            new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Items.DIAMOND_CHESTPLATE),
            new ItemStack(Items.DIAMOND_HELMET) };

    @Override
    public void onEnable() {
        if (Minecraft.player == null || Minecraft.player.isDead) {
            toggle();
            return;
        }

        EntityOtherPlayerMP clonedPlayer = new EntityOtherPlayerMP(Minecraft.world,
                new GameProfile(UUID.fromString("fdee323e-7f0c-4c15-8d1c-0f277442342a"), "Xuilan"));
        clonedPlayer.copyLocationAndAnglesFrom(Minecraft.player);
        clonedPlayer.rotationYawHead = Minecraft.player.rotationYawHead;
        clonedPlayer.rotationYaw = Minecraft.player.rotationYaw;
        clonedPlayer.rotationPitch = Minecraft.player.rotationPitch;
        clonedPlayer.setGameType(GameType.SURVIVAL);
        clonedPlayer.setHealth(20);
        Minecraft.world.addEntityToWorld(-1234, clonedPlayer);

        if (armor.isToggle())
            for (int i = 0; i < 4; i++) {
                // Create base
                ItemStack item = armors[i];
                // Add enchants
                item.addEnchantment(i == 2 ? Enchantments.BLAST_PROTECTION : Enchantments.PROTECTION, 4);
                // Add it to the player
                clonedPlayer.inventory.armorInventory.set(i, item);
            }

        clonedPlayer.onLivingUpdate();
    }

    @Override
    public void onDisable() {
        if (Minecraft.world != null) {
            Minecraft.world.removeEntityFromWorld(-1234);
        }
    }

}

