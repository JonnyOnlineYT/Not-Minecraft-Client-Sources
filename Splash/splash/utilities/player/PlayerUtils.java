package splash.utilities.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import splash.Splash;
import splash.api.friend.Friend;
import splash.client.modules.combat.AntiBot;
import splash.utilities.math.MathUtils;

/**
 * Author: Ice Created: 00:16, 14-Jun-20 Project: Client
 */
public class PlayerUtils {
	
	public static boolean isValid(EntityLivingBase entity, double range,boolean invisible, boolean teams, boolean dead, boolean players, boolean animals, boolean monsters) {
		if (entity == Minecraft.getMinecraft().thePlayer)
			return false;

		if (teams && entity != null && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			return !isOnSameTeam(player);
		}
		if (entity instanceof EntityArmorStand)
			return false;
		if (invisible && entity.isInvisible())
			return false;
		if (dead && (entity.isDead || entity.getHealth() <= 0))
			return false;
		return (entity != null) && entity != Minecraft.getMinecraft().thePlayer
				&& (entity instanceof EntityPlayer && players || entity instanceof EntityAnimal && animals
						|| entity instanceof EntityMob && monsters || entity instanceof EntityVillager && animals)

				&& entity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) <= range
				&& !entity.getDisplayName().getFormattedText().toLowerCase().contains("[npc]")
				&& !AntiBot.bots.contains(entity) && !Splash.INSTANCE.getFriendManager().isFriend(entity.getName());
	}

	public static boolean isOnSameTeam(EntityPlayer entity) {
		if (!(entity.getTeam() != null && mc.thePlayer.getTeam() != null))
			return false;
		return entity.getDisplayName().getFormattedText().charAt(1) == mc.thePlayer.getDisplayName().getFormattedText().charAt(1);
	}
	
	public static void swapToItem() {
		Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId,Minecraft.getMinecraft().thePlayer.inventory.currentItem, 9, 2, Minecraft.getMinecraft().thePlayer);
		Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, 9,Minecraft.getMinecraft().thePlayer.inventory.currentItem, 2, Minecraft.getMinecraft().thePlayer);
	}

	public static void swapBackToItem() {
		Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, 9, Minecraft.getMinecraft().thePlayer.inventory.currentItem, 2, Minecraft.getMinecraft().thePlayer);
		Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId,Minecraft.getMinecraft().thePlayer.inventory.currentItem, 9, 2, Minecraft.getMinecraft().thePlayer);
	}

	public static boolean isHoldingSword() {
		if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
			return true;
		}
		return false;
	}
	
	public static int getEmptyHotbarSlot() {
        for (int k = 0; k < 9; ++k) {
            if (mc.thePlayer.inventory.mainInventory[k] == null) {
                return k;
            }
        }
		return -1;
	}
	
    public static boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isBad(final ItemStack item) {
    	return !(item.getItem() instanceof ItemArmor || item.getItem() instanceof ItemTool || item.getItem() instanceof ItemBlock || item.getItem() instanceof ItemSword || item.getItem() instanceof ItemEnderPearl || item.getItem() instanceof ItemFood || (item.getItem() instanceof ItemPotion && !isBadPotion(item))) && !item.getDisplayName().toLowerCase().contains(EnumChatFormatting.GRAY +"(right click)");
    }
    
    public static boolean isBadPotion(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect)o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
	public static Minecraft mc = Minecraft.getMinecraft();

	public static double getJumpBoostModifier(double baseJumpHeight) {
		if (mc.thePlayer.isPotionActive(Potion.jump)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
			baseJumpHeight += (float) (amplifier + 1) * 0.1F;
		}

		return baseJumpHeight;
	}

	public static int getHealthColor(final EntityLivingBase player) {
		final float f = player.getHealth();
		final float f2 = player.getMaxHealth();
		final float f3 = Math.max(0.0f, Math.min(f, f2) / f2);
		return Color.HSBtoRGB(f3 / 3.0f, 1.0f, 0.75f) | 0xFF000000;
	}

	public static boolean canBlock(boolean mobs, boolean players, double range, boolean invis) {
		for (Object obj : mc.theWorld.loadedEntityList) {
			if (obj instanceof EntityLivingBase) {
				EntityLivingBase o = (EntityLivingBase) obj;
				if (o.getDistanceToEntity((Entity) mc.thePlayer) > range)
					continue;
				if (o.isInvisible() && !invis)
					continue;
				if (Splash.INSTANCE.getFriendManager().isFriend(o.getName()))
					continue;
				if (o.isDead)
					continue;
				if (o == mc.thePlayer)
					continue;
				if (AntiBot.bots.contains(o))
					continue;
				if (!(o instanceof net.minecraft.entity.player.EntityPlayer) && players)
					continue;
				return true;
			}
		}
		return false;
	}

	public static List<EntityLivingBase> getTargets(boolean teams, final int maxTargets, final boolean mobs,
			final boolean players, final double range, final boolean invis) {
		final ArrayList<EntityLivingBase> list = new ArrayList<>();
		for (final Object obj : mc.theWorld.loadedEntityList) {
			EntityLivingBase o;
			if (!(obj instanceof EntityLivingBase)
					|| (o = (EntityLivingBase) obj).getDistanceToEntity(
							mc.thePlayer) > (((EntityLivingBase) obj).posY > mc.thePlayer.posY + 1 ? range + 2 : range)
					|| o.isInvisible() && !invis || o.isDead || o.getHealth() == 0.0f || o == mc.thePlayer
					|| AntiBot.bots.contains(o))
				continue;
			if (!(obj instanceof EntityPlayer))
				continue;
			if (list.size() >= maxTargets)
				continue;
			list.add(o);
		}
		return list;
	}

	public static EntityLivingBase getTarget(boolean teams, final boolean mobs, final boolean players,
			final double range, final boolean invis) {
		for (final Object obj : mc.theWorld.loadedEntityList) {
			EntityLivingBase o;
			if (!(obj instanceof EntityLivingBase)
					|| (o = (EntityLivingBase) obj).getDistanceToEntity(mc.thePlayer) > range
					|| o.isInvisible() && !invis || o.isDead || o.getHealth() == 0.0f || o == mc.thePlayer
					|| AntiBot.bots.contains(o))
				continue;
			if (!(obj instanceof EntityPlayer))
				continue;
			return o;
		}
		return null;
	}
}
