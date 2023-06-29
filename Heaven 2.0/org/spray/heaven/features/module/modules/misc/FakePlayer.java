package org.spray.heaven.features.module.modules.misc;

import java.util.UUID;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;

@ModuleInfo(name = "FakePlayer", category = Category.MISC, visible = true, key = Keyboard.KEY_NONE)
public class FakePlayer extends Module {

	private final Setting armor = register(new Setting("Armors", false));

	private ItemStack[] armors = new ItemStack[] { new ItemStack(Items.DIAMOND_BOOTS),
			new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Items.DIAMOND_CHESTPLATE),
			new ItemStack(Items.DIAMOND_HELMET) };

	@Override
	public void onEnable() {
		if (mc.player == null || mc.player.isDead) {
			disable();
			return;
		}

		EntityOtherPlayerMP clonedPlayer = new EntityOtherPlayerMP(mc.world,
				new GameProfile(UUID.fromString("fdee323e-7f0c-4c15-8d1c-0f277442342a"), "FakeSpray"));
		clonedPlayer.copyLocationAndAnglesFrom(mc.player);
		clonedPlayer.rotationYawHead = mc.player.rotationYawHead;
		clonedPlayer.rotationYaw = mc.player.rotationYaw;
		clonedPlayer.rotationPitch = mc.player.rotationPitch;
		clonedPlayer.setGameType(GameType.SURVIVAL);
		clonedPlayer.setHealth(20);
		mc.world.addEntityToWorld(-1234, clonedPlayer);

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
		if (mc.world != null) {
			mc.world.removeEntityFromWorld(-1234);
		}
	}

}
