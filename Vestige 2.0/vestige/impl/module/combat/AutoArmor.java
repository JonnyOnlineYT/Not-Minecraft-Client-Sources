package vestige.impl.module.combat;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.NumberSetting;
import vestige.impl.module.player.InvManager;
import vestige.util.misc.TimerUtil;

@ModuleInfo(name = "AutoArmor", category = Category.COMBAT)
public class AutoArmor extends Module {
	
    private final TimerUtil timer = new TimerUtil();
	
	public final NumberSetting delay = new NumberSetting("Delay", this, 100, 0, 300, 10, true);
	public final BooleanSetting invOpen = new BooleanSetting("Open Inventory", this, false);
	
	public AutoArmor() {
		this.registerSettings(delay, invOpen);
	}

	@Listener
	public void onUpdate(UpdateEvent event) {
		if(Vestige.getInstance().getModuleManager().getModule(InvManager.class).isEnabled())
			return;
		
		long delay = (long) this.delay.getCurrentValue();
		if(invOpen.isEnabled() && !(mc.currentScreen instanceof GuiInventory)) {
			return;
		}
		if(mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat) {
			if(timer.getTimeElapsed() >= delay) {
				getBestArmor();
			}
		}
	}
	
	 public void getBestArmor(){
	    	for(int type = 1; type < 5; type++){
	    		if(mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()){
	    			ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
	    			if(isBestArmor(is, type)){
	    				continue;
	    			} else {
	    				drop(4 + type);
	    			}
	    		}
	    		for (int i = 9; i < 45; i++) {
	    			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	    				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	    				if(isBestArmor(is, type) && getProtection(is) > 0){
	    					shiftClick(i);
	    					timer.reset();
	    					if(delay.getCurrentValue() > 0)
	    						return;
	    				}
	    			}
	            }
	        }
	    }
	    public boolean isBestArmor(ItemStack stack, int type) {
	    	float prot = getProtection(stack);
	    	String strType = "";
	    	if(type == 1){
	    		strType = "helmet";
	    	}else if(type == 2){
	    		strType = "chestplate";
	    	}else if(type == 3){
	    		strType = "leggings";
	    	}else if(type == 4){
	    		strType = "boots";
	    	}
	    	if(!stack.getUnlocalizedName().contains(strType)){
	    		return false;
	    	}
	    	for (int i = 5; i < 45; i++) {
	            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	                if(getProtection(is) > prot && is.getUnlocalizedName().contains(strType))
	                	return false;
	            }
	        }
	    	return true;
	    }
	    public void shiftClick(int slot){
	    	mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
	    }

	    public void drop(int slot){
	    	mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
	    }
	    public static float getProtection(ItemStack stack){
	    	float prot = 0;
	    	if ((stack.getItem() instanceof ItemArmor)) {
	    		ItemArmor armor = (ItemArmor)stack.getItem();
	    		prot += armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075D;
	    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack)/100d;
	    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack)/100d;
	    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack)/100d;
	    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack)/50d;   	
	    		//prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180308_g.effectId, stack)/100d;   	
	    	}
		    return prot;
	    }
	
	
				
}