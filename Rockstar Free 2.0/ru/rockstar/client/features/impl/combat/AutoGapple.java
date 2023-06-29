package ru.rockstar.client.features.impl.combat;

import java.util.Comparator;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.world.InventoryHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class AutoGapple extends Feature {

    public static NumberSetting health;
    public static NumberSetting timePredict;
    private boolean isActive;
    public static boolean gDebug = false;
    public BooleanSetting wait = new BooleanSetting("Wait", false, () -> true);
    public BooleanSetting gapplePredict = new BooleanSetting("Gapple Prediction", false, () -> true);
    public BooleanSetting shieldSwap = new BooleanSetting("AutoSwap", false, () -> true);
    public BooleanSetting ncpFix = new BooleanSetting("NCP Fix", false, () -> true);
    public BooleanSetting distanceCheck = new BooleanSetting("Distance Check", false, () -> true);
    public BooleanSetting healthCheck = new BooleanSetting("Health Check", false, () -> true);
    public BooleanSetting custom = new BooleanSetting("Custom Health", true, () -> healthCheck.getBoolValue());

    public AutoGapple() {
        super("AutoGApple", "јвтоматически ест €блоко при опредленном здоровье",0, Category.COMBAT);
        health = new NumberSetting("Health", 15, 1, 20, 1, () -> true);
        timePredict = new NumberSetting("Gapple Prediction Time", 1, 0, 1.5f, 0.1f, () -> gapplePredict.getBoolValue());
        addSettings(health,gapplePredict, timePredict, healthCheck,custom, shieldSwap, distanceCheck, ncpFix, wait);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
    	if (shieldSwap.getBoolValue()) {
    		int shield = InventoryHelper.getShieldAtHotbar();
     		int gapple = InventoryHelper.getItemAtHotbar();
     		if (!isGoldenApple(mc.player.getHeldItemOffhand())) {
     			mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, AutoGapple.mc.player);
				
				mc.playerController.windowClick(0, gapple, 1, ClickType.PICKUP, AutoGapple.mc.player);
				
     		}
    	}
    	if (isGoldenApple(mc.player.getHeldItemOffhand())) {
    		this.setSuffix("" + (int) health.getNumberValue(), true);
    		if (distanceCheck.getBoolValue()) {
    			for (Entity entity : mc.world.loadedEntityList) {
    				if (entity.getDistanceToEntity(mc.player) > 10) {
        				return;
        			}
    			}
    		}
    		boolean sgapple = mc.player.isPotionActive(MobEffects.ABSORPTION);
    		
    		boolean bgapple = mc.player.isPotionActive(MobEffects.ABSORPTION);
    		
    		//float hp = healthCheck.getBoolValue() ? (sgapple ? health.getNumberValue() + 4 : (bgapple ? custom.getBoolValue() ? health.getNumberValue() + 16 : 20 : health.getNumberValue())) : health.getNumberValue();
    		
    		float hp = healthCheck.getBoolValue() ? (bgapple ? custom.getBoolValue() ? health.getNumberValue() + 4 : 19.9f : health.getNumberValue()) : health.getNumberValue();
    		
    		
    			//mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
    			if (mc.player == null || mc.world == null)
                    return;
    			if (isGoldenApple(mc.player.getHeldItemOffhand())) {
    				gDebug = false;
    			}
    			 
    			 if (isGoldenApple(mc.player.getHeldItemOffhand()) && mc.player.getHealth() > hp) {
    				 gDebug = false;
    			 }
                if (isGoldenApple(mc.player.getHeldItemOffhand()) && mc.player.getHealth() <= hp) {
                	if (mc.currentScreen != null) {
                        final GuiScreen guiScreen = mc.currentScreen;
                        guiScreen.allowUserInput = true;
                    }
                	if (ncpFix.getBoolValue()) {
   					 gDebug = true;
                	}
                	if (wait.getBoolValue()) {
                		if (timerHelper.hasReached(300)) {
                			isActive = true;
                			KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                		}
                	} else {
                			isActive = true;
                			KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                	}
                } else
                if (isActive) {
                	gDebug = false;
                	isActive = false;
                	KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(),false);
                } else 
                	if (gapplePredict.getBoolValue()) {
                		if (!timerHelper.hasReached(1000 * timePredict.getNumberValue())) {
                    		KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                    	} else {
                    		KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                    		timerHelper.reset();
                    		timerHelper.resetwatermark();
                    	}
                	}
    	}
    }

    private boolean isGoldenApple(ItemStack itemStack) {
        return (itemStack != null && !itemStack.isEmpty() && itemStack.getItem() instanceof ItemAppleGold);
    }
}
