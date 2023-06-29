package splash.client.modules.player;


import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import splash.Splash;
import splash.Splash.GAMEMODE;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.network.EventPacketSend;
import splash.client.events.player.EventPlayerUpdate;
import splash.utilities.math.MathUtils;
import splash.utilities.player.InventoryUtils;
import splash.utilities.player.PlayerUtils;
import splash.utilities.time.Stopwatch;

public class InventoryManager extends Module {
	public BooleanValue<Boolean> clean = new BooleanValue<>("Clean", true, this);
	public BooleanValue<Boolean> keepAxe = new BooleanValue<>("Axe", true, this);
	public BooleanValue<Boolean> keepShovel = new BooleanValue<>("Shovel", true, this);
	public BooleanValue<Boolean> keepPickAxe = new BooleanValue<>("Pickaxe", true, this);
	public BooleanValue<Boolean> swordSlot = new BooleanValue<>("Sword lot", true, this);
	public BooleanValue<Boolean> cleanBad = new BooleanValue<>("Best Items", true, this);
	public BooleanValue<Boolean> autoArmor = new BooleanValue<>("Auto Armor", true, this);
	public NumberValue<Integer> swordsSlot = new NumberValue<>("Sword Slot", 1, 1, 9, this);
    public NumberValue<Integer> delay = new NumberValue<>("Delay", 150, 1, 1000, this);
	Stopwatch timer;
	private int lastSlot = -1;
	private int exploitTime;
	
    public InventoryManager() {
        super("InventoryManager", "Automatically manages your inventory", ModuleCategory.PLAYER);  
        timer = new Stopwatch();
    }
    
	@Override
	public void onEnable() { 
		super.onEnable();
	}
	
	@Override
	public void onDisable() { 
		super.onDisable();
	}
	
	@Collect
	public void onPacketSend(EventPacketSend e) {
		if (e.getSentPacket() instanceof C09PacketHeldItemChange) {
			C09PacketHeldItemChange packet = (C09PacketHeldItemChange)e.getSentPacket();
			lastSlot = packet.getSlotId();
		}
	}
	
	@Collect
	public void onUpdate(EventPlayerUpdate e) {
		if (mc.thePlayer.isUsingItem() || mc.currentScreen != null || mc.thePlayer.isUsingItem() || mc.gameSettings.keyBindUseItem.isKeyDown()) {
			timer.reset();
			return;
		}
		if (e.getStage().equals(Stage.POST)) {
			int delay = this.delay.getValue();
			if (timer.delay(delay)) {
				mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
				invManager(delay);
				if (autoArmor.getValue()) {
					autoArmor(delay);
				}
				mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(0));
				timer.reset();
			}
		} else {
			if (lastSlot != -1 && lastSlot != mc.thePlayer.inventory.currentItem) {
				mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
			}
		}
	}
 
	private void invManager(int delay) {
		int bestSword = -1;
		float bestDamage = 1F;
		
	    for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
	    	ItemStack item = mc.thePlayer.inventory.mainInventory[k];
	    	if (item != null) {
	    		if (item.getItem() instanceof ItemSword) {
	    			ItemSword is = (ItemSword) item.getItem();
	    			float damage = is.getDamageVsEntity();
	    	    	damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, item) * 1.26F + 
	    	    			EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, item) * 0.01f;
			    	if (damage > bestDamage) {
			    		bestDamage = damage;
			    		bestSword = k;
			    	}
	    		}
	    	}
	    }
		int swordSlot = swordsSlot.getValue();
		if (bestSword != -1 && bestSword != swordSlot - 1) {
			for (int i = 0; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
				Slot s = mc.thePlayer.inventoryContainer.inventorySlots.get(i);
				if (s.getHasStack() && s.getStack() == mc.thePlayer.inventory.mainInventory[bestSword] && timer.delay(delay)) {
					int slot = swordSlot - 1;
					mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, s.slotNumber, slot, 2, mc.thePlayer);
                    if (exploitTime++ > 4 && onServer("hypixel")) {
                        exploitTime = 0;
                        mc.playerController.windowClick(0, (int) MathUtils.getRandomInRange(11, 17), 0, 1, mc.thePlayer);
                    }
					timer.reset();
					return;
				}
			}
		}
		if (clean.getValue()) {
			for (int i = 9; i < 45; ++i) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {

                    final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

	                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	                    if (shouldDrop(is, i) && timer.delay(delay)) {
	                        drop(i);
	                        timer.reset();
	                        break;
	                    }
	                }
				}
			}
		}
	}
	
	private void autoArmor(double delay) {
		int bestHelm = getBestHelmet();
		if (mc.thePlayer.inventory.armorItemInSlot(3) == null) {
			if (bestHelm != -1) {
				if (bestHelm < 9 && mc.thePlayer.inventory.getStackInSlot(bestHelm).getItem() instanceof ItemArmor) {
					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(bestHelm));
					mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(bestHelm)));
				} else {
					shiftClick(bestHelm);
				}
				timer.reset();
				return;
			}
		} else if (bestHelm != -1 && mc.thePlayer.inventory.armorItemInSlot(3) != mc.thePlayer.inventoryContainer.getSlot(bestHelm).getStack()){
			drop(5);
			timer.reset();
			return;
		}
		int bestChest = getBestChestplate();
		if (mc.thePlayer.inventory.armorItemInSlot(2) == null) {
			if (bestChest != -1) {
				if (bestChest < 9 && mc.thePlayer.inventory.getStackInSlot(bestChest).getItem() instanceof ItemArmor) {
					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(bestChest));
					mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(bestChest)));
				} else {
					shiftClick(bestChest);
				}
				timer.reset();
				return;
			}
		} else if (bestChest != -1 && mc.thePlayer.inventory.armorItemInSlot(2) != mc.thePlayer.inventoryContainer.getSlot(bestChest).getStack()){
			drop(6);
			timer.reset();
			return;
		}
		int bestLegs = getBestLeggings();
		if (mc.thePlayer.inventory.armorItemInSlot(1) == null) {
			if (bestLegs != -1) {
				if (bestLegs < 9 && mc.thePlayer.inventory.getStackInSlot(bestLegs).getItem() instanceof ItemArmor) {
					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(bestLegs));
					mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(bestLegs)));
				} else {
					shiftClick(bestLegs);
					timer.reset();
					return;
				}
			}
		} else if (bestLegs != -1 && mc.thePlayer.inventory.armorItemInSlot(1) != mc.thePlayer.inventoryContainer.getSlot(bestLegs).getStack()){
			drop(7);
			timer.reset();
			return;
		}
		int bestBoot = getBestBoots();
		if (mc.thePlayer.inventory.armorItemInSlot(0) == null) {
			if (bestBoot != -1) {
				if (bestBoot < 9 && mc.thePlayer.inventory.getStackInSlot(bestBoot).getItem() instanceof ItemArmor) {
					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(bestBoot));
					mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(bestBoot)));
				} else {
					shiftClick(bestBoot);
				}
				timer.reset();
				return;
			}
		} else if (bestBoot != -1 && mc.thePlayer.inventory.armorItemInSlot(0) != mc.thePlayer.inventoryContainer.getSlot(bestBoot).getStack()){
			drop(8);
			timer.reset();
			return;
		}
		
		boolean dropped = false;
	    
		for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (is != null && is.getItem() instanceof ItemArmor && !dropped) {
					dropped = true;
					timer.reset();
					drop(i);
			    	return;
		    	}
            }
	    }
	}
	
	public void drop(final int slot) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
        if (exploitTime++ > 4 && onServer("hypixel")) {
            exploitTime = 0;
            mc.playerController.windowClick(0, (int) MathUtils.getRandomInRange(11, 17), 0, 1, mc.thePlayer);
        }
    }
    
    public boolean shouldDrop(ItemStack is, int k) {
    	int bestSword = InventoryUtils.getSwordSlot();
    	if (is.hasDisplayName()) return false;
    	if (is.getItem() instanceof ItemSword) {
			if (bestSword != -1 && bestSword != k) {
				return true;
			}
		}
		int bestPick = InventoryUtils.getPickaxeSlot();
		if (is.getItem() instanceof ItemPickaxe) {
			if (!keepPickAxe.getValue()) {
				return true;
			}
			if (bestPick != -1 && bestPick != k) {
				return true;
			}
		}
		
		int bestAxe = InventoryUtils.getAxeSlot();
		if (is.getItem() instanceof ItemAxe) {
			if (!keepAxe.getValue()) {
				return true;
			}
			if (bestAxe != -1 && bestAxe != k) {
				return true;
			}
		}
		
		int bestShovel = InventoryUtils.getShovelSlot();
		if (InventoryUtils.isShovel(is.getItem())) {
			if (!keepShovel.getValue()) {
				return true;
			}
			if (bestShovel != -1 && bestShovel != k) {
				return true;
			}
		}
		if (cleanBad.getValue() && PlayerUtils.isBad(is)) {
			return true;
		}
    	return false;
    }
    
    private int getBestHelmet() {
		int bestSword = -1;
		float bestValue = 0F;
		
	    for (int k = 0; k < 36; k++) {
	    	if (mc.thePlayer.inventoryContainer.getSlot(k).getHasStack()) {
		    	ItemStack item = mc.thePlayer.inventoryContainer.getSlot(k).getStack();
		    	if (item != null) {
		    		if (item.getItem() instanceof ItemArmor) {
		    			ItemArmor ia = (ItemArmor) item.getItem();
		    			float value = getValue(item, ia);
				    	if (ia.armorType == 0 && value > bestValue) {
				    		bestValue = value;
				    		bestSword = k;
				    	}
		    		}
		    	}
	    	}
	    }
	    
	    for (int k = 0; k < 9; k++) {
	    	ItemStack item = mc.thePlayer.inventory.getStackInSlot(k);
	    	if (item != null) {
	    		if (item.getItem() instanceof ItemArmor) {
	    			ItemArmor ia = (ItemArmor) item.getItem();
	    			float value = getValue(item, ia);
			    	if (ia.armorType == 0 && value > bestValue) {
			    		bestValue = value;
			    		bestSword = k;
			    	}
	    		}
	    	}
	    }
		return bestSword;
	}
	
	private int getBestChestplate() {
		int bestSword = -1;
		float bestValue = 0F;
		
	    for (int k = 0; k < 36; k++) {
	    	if (mc.thePlayer.inventoryContainer.getSlot(k).getHasStack()) {
	    		ItemStack item = mc.thePlayer.inventoryContainer.getSlot(k).getStack();
		    	if (item != null) {
		    		if (item.getItem() instanceof ItemArmor) {
		    			ItemArmor ia = (ItemArmor) item.getItem();
		    			float value = getValue(item, ia);
				    	if (ia.armorType == 1 && value > bestValue) {
				    		bestValue = value;
				    		bestSword = k;
				    	}
		    		}
		    	}
	    	}
	    }
	    
	    for (int k = 0; k < 9; k++) {
	    	ItemStack item = mc.thePlayer.inventory.getStackInSlot(k);
	    	if (item != null) {
	    		if (item.getItem() instanceof ItemArmor) {
	    			ItemArmor ia = (ItemArmor) item.getItem();
	    			float value = getValue(item, ia);
			    	if (ia.armorType == 1 && value > bestValue) {
			    		bestValue = value;
			    		bestSword = k;
			    	}
	    		}
	    	}
	    }
		return bestSword;
	}
	
	private int getBestLeggings() {
		int bestSword = -1;
		float bestValue = 0F;
		
	    for (int k = 0; k < 36; k++) {
	    	if (mc.thePlayer.inventoryContainer.getSlot(k).getHasStack()) {
	    		ItemStack item = mc.thePlayer.inventoryContainer.getSlot(k).getStack();
	    		if (item != null) {
		    		if (item.getItem() instanceof ItemArmor) {
		    			ItemArmor ia = (ItemArmor) item.getItem();
		    			float value = getValue(item, ia);
				    	if (ia.armorType == 2 && value > bestValue) {
				    		bestValue = value;
				    		bestSword = k;
				    	}
		    		}
		    	}
	    	}
	    }
	    
	    for (int k = 0; k < 9; k++) {
	    	ItemStack item = mc.thePlayer.inventory.getStackInSlot(k);
	    	if (item != null) {
	    		if (item.getItem() instanceof ItemArmor) {
	    			ItemArmor ia = (ItemArmor) item.getItem();
	    			float value = getValue(item, ia);
			    	if (ia.armorType == 2 && value > bestValue) {
			    		bestValue = value;
			    		bestSword = k;
			    	}
	    		}
	    	}
	    }
		return bestSword;
	}
	
	private int getBestBoots() {
		int bestSword = -1;
		float bestValue = 0F;
		
	    for (int k = 0; k < 36; k++) {
	    	if (mc.thePlayer.inventoryContainer.getSlot(k).getHasStack()) {
	    		ItemStack item = mc.thePlayer.inventoryContainer.getSlot(k).getStack();
	    		
	    		if (item != null) {
		    		if (item.getItem() instanceof ItemArmor) {
		    			ItemArmor ia = (ItemArmor) item.getItem();
		    			float value = getValue(item, ia);
				    	if (ia.armorType == 3 && value > bestValue) {
				    		bestValue = value;
				    		bestSword = k;
				    	}
		    		}
		    	}
	    	}
	    }
	    
	    for (int k = 0; k < 9; k++) {
	    	ItemStack item = mc.thePlayer.inventory.getStackInSlot(k);
	    	if (item != null) {
	    		if (item.getItem() instanceof ItemArmor) {
	    			ItemArmor ia = (ItemArmor) item.getItem();
	    			float value = getValue(item, ia);
			    	if (ia.armorType == 3 && value > bestValue) {
			    		bestValue = value;
			    		bestSword = k;
			    	}
	    		}
	    	}
	    }
		return bestSword;
	}
	
	private float getValue(ItemStack is, ItemArmor ia) {
		int type = 0;
		if (ia.armorType == 0) {type = 0;}
		if (ia.armorType == 3) {type = 1;}
		if (ia.armorType == 2) {type = 2;}
		if (ia.armorType == 1) {type = 3;}
		
		int render = 0;
		if (ia.renderIndex == 0) {render = 0;}
		if (ia.renderIndex == 1) {render = 1;}
		if (ia.renderIndex == 4) {render = 2;}
		if (ia.renderIndex == 2) {render = 3;}
		if (ia.renderIndex == 3) {render = 4;}
		
		float value = (type + 1) * (render + 1);
		value += EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, is) * 2.5f;
    	value += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, is) * 1.25f;
    	value += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, is) * 1f;
		
		return value;
	}
	
    public void shiftClick(int slot) {
    	if (mc.thePlayer.inventoryContainer.getSlot(slot).getHasStack()) {
    		Slot s = mc.thePlayer.inventoryContainer.getSlot(slot);
    		if (s.getStack().getItem() instanceof ItemArmor) {
            	Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, 0, 1, Minecraft.getMinecraft().thePlayer);
                if (exploitTime++ > 4 && onServer("hypixel")) {
                    exploitTime = 0;
                    mc.playerController.windowClick(0, (int) MathUtils.getRandomInRange(11, 17), 0, 1, mc.thePlayer);
                }
    		}
    	}
    }
}
