package ru.rockstar.client.features.impl.combat;


import java.util.ArrayList;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.event.event.EventRender2D;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.api.utils.world.InventoryHelper;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class AutoTotem extends Feature
{
    public ListSetting nmsMode = new ListSetting("NoMovingSwapMode", "Rage", () -> true, "Ignore", "Rage");
    public static NumberSetting health;
    public static BooleanSetting countTotem;
    public static BooleanSetting noGappleSwap;
    public static BooleanSetting noArmBool;
    public static BooleanSetting checkCrystal;
    public static NumberSetting radiusToCrystal;
    public static BooleanSetting checkTNT;
    public static NumberSetting radiusToTNT;
    public static BooleanSetting noMoving;
    public static BooleanSetting checkFall;
    public static NumberSetting fallDistance;
    public static BooleanSetting saveEnchTotems;
    public static BooleanSetting swapBack;
    private int totemCount;
    private boolean returnOld;
    
    public AutoTotem() {
        super("AutoTotem", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0431\u0435\u0440\u0435\u0442 \u0432 \u0440\u0443\u043a\u0443 \u0442\u043e\u0442\u0435\u043c \u043f\u0440\u0438 \u043e\u043f\u0440\u0435\u0434\u043b\u0435\u043d\u043d\u043e\u043c \u0437\u0434\u043e\u0440\u043e\u0432\u044c\u0435", 0, Category.COMBAT);
        AutoTotem.health = new NumberSetting("Health Amount", 4, 1.0f, 20.0f, 0.5f, () -> true);
        AutoTotem.noGappleSwap = new BooleanSetting("NoGappleSwap", true, () -> true);
        AutoTotem.noArmBool = new BooleanSetting("Check Armor", true, () -> true);
        AutoTotem.noMoving = new BooleanSetting("No Moving Swap", false, () -> true);
        AutoTotem.countTotem = new BooleanSetting("Count Totem", false, () -> true);
        AutoTotem.checkCrystal = new BooleanSetting("Check Crystal", false, () -> true);
        AutoTotem.radiusToCrystal = new NumberSetting("Distance to Crystal", 6.0f, 1.0f, 8.0f, 1.0f, () -> AutoTotem.checkCrystal.getBoolValue());
        AutoTotem.checkTNT = new BooleanSetting("Check TNT", false, () -> true);
        AutoTotem.radiusToTNT = new NumberSetting("Distance to TNT", 6.0f, 1.0f, 8.0f, 1.0f, () -> AutoTotem.checkTNT.getBoolValue());
        AutoTotem.checkFall = new BooleanSetting("Check Fall", false, () -> true);
        AutoTotem.fallDistance = new NumberSetting("Fall Distance", 15.0f, 5.0f, 125.0f, 5.0f, () -> AutoTotem.checkFall.getBoolValue());
        AutoTotem.swapBack = new BooleanSetting("Swap Back", true, () -> true);
        AutoTotem.saveEnchTotems = new BooleanSetting("Save Enchanted Totems", true, () -> true);
        this.addSettings(AutoTotem.health, noGappleSwap, noArmBool, AutoTotem.noMoving, nmsMode, AutoTotem.countTotem, AutoTotem.swapBack, AutoTotem.saveEnchTotems, AutoTotem.checkCrystal, AutoTotem.radiusToCrystal, AutoTotem.checkTNT, AutoTotem.radiusToTNT, AutoTotem.checkFall, AutoTotem.fallDistance);
    }
    
    @Override
    public void onDisable() {
        this.returnOld = false;
        super.onDisable();
    }
    
    private int fountTotemCountNoDonat() {
        int count = 0;
        for (int i = 0; i < AutoTotem.mc.player.inventory.getSizeInventory(); ++i) {
            final ItemStack stack = AutoTotem.mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING && !stack.isItemEnchanted()) {
                ++count;
            }
        }
        return count;
    }
    
    private int fountTotemCount() {
        int count = 0;
        for (int i = 0; i < AutoTotem.mc.player.inventory.getSizeInventory(); ++i) {
            final ItemStack stack = AutoTotem.mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                ++count;
            }
        }
        return count;
    }
    
    private boolean checkCrystal() {
        if (!AutoTotem.checkCrystal.getBoolValue()) {
            return false;
        }
        for (final Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal && AutoTotem.mc.player.getDistanceToEntity(entity) <= AutoTotem.radiusToCrystal.getNumberValue()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkTNT() {
        if (!AutoTotem.checkTNT.getBoolValue()) {
            return false;
        }
        for (final Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (entity instanceof EntityTNTPrimed && AutoTotem.mc.player.getDistanceToEntity(entity) <= AutoTotem.radiusToTNT.getNumberValue()) {
                return true;
            }
            if (entity instanceof EntityMinecartTNT && AutoTotem.mc.player.getDistanceToEntity(entity) <= AutoTotem.radiusToTNT.getNumberValue()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkFall(final float fallDist) {
        return AutoTotem.checkFall.getBoolValue() && AutoTotem.mc.player.fallDistance > fallDist;
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D event) {
        if (this.fountTotemCount() > 0 && AutoTotem.countTotem.getBoolValue()) {
            AutoTotem.mc.mntsb.drawStringWithShadow(new StringBuilder(String.valueOf(this.fountTotemCount())).toString(), event.getResolution().getScaledWidth() / 2.0f + 19.0f, event.getResolution().getScaledHeight() / 2.0f, -1);
            for (int i = 0; i < AutoTotem.mc.player.inventory.getSizeInventory(); ++i) {
                final ItemStack stack = AutoTotem.mc.player.inventory.getStackInSlot(i);
                if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                    GlStateManager.pushMatrix();
                    GlStateManager.disableBlend();
                    AutoTotem.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, event.getResolution().getScaledWidth() / 2 + 4, event.getResolution().getScaledHeight() / 2 - 7);
                    GlStateManager.popMatrix();
                }
            }
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (AutoTotem.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        
        float health;
        if (mc.player.isPotionActive(MobEffects.RESISTANCE)) {
        	health = mc.player.getHealth() / 1.5f;
        } else {
        	health = mc.player.getHealth();
        }
        if (this.returnOld && !this.checkTNT() && !this.checkCrystal() && !this.checkFall(AutoTotem.fallDistance.getNumberValue())) {
           if (health > AutoTotem.health.getNumberValue()) {
        	   KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
        	   int t = -1;
               for (int i = 0; i < 45; ++i) {
                   if (AutoTotem.mc.player.inventory.getStackInSlot(i).isEmpty()) {
                       t = i;
                       break;
                   }
               }
               if (t == -1) {
                   return;
               }
               if (AutoTotem.swapBack.getBoolValue()) {
               	 	AutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, AutoTotem.mc.player);
                 	AutoTotem.mc.playerController.windowClick(0, t, 0, ClickType.PICKUP, AutoTotem.mc.player);
               }

               this.returnOld = false;
           }
        }
        this.totemCount = AutoTotem.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (AutoTotem.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++this.totemCount;
        }
        else if (AutoTotem.mc.player.inventory.getItemStack().isEmpty()) {
        	 int iteration = mc.player.getTotalArmorValue();
            if (this.totemCount == 0) {
                return;
            }
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (AutoTotem.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    if (!AutoTotem.mc.player.inventory.getStackInSlot(i).isItemEnchanted() || this.fountTotemCountNoDonat() <= 0 || !AutoTotem.saveEnchTotems.getBoolValue()) {
                        t = i;
                        break;
                    }
                    t = -1;
                }
            }
            if (t == -1) {
                return;
            } 
            if ((mc.player.isHandActive() && mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) && noGappleSwap.getBoolValue()) {
            	return;
            }
            	if (((noArmBool.getBoolValue() ? (iteration != 20 ? (health <= 25 - iteration) : (health <= AutoTotem.health.getNumberValue())) : (health <= AutoTotem.health.getNumberValue())) || this.checkTNT() || this.checkCrystal() || this.checkFall(AutoTotem.fallDistance.getNumberValue())) && AutoTotem.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
            		 if (AutoTotem.noMoving.getBoolValue() && MovementHelper.isMoving()) {
                   	  switch (nmsMode.getOptions()) {
                   	  case "Rage":
                   		  mc.player.motionX = 0;
                   		  mc.player.motionY = 0;
                   		  mc.player.motionZ = 0;
                       	  return;
                         case "Ignore":
                       	  return;
                   	  }
                   }
            		if (returnOld) {
                    	AutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, AutoTotem.mc.player);
                    }
            		AutoTotem.mc.playerController.windowClick(0, InventoryHelper.getTotemAtHotbar(), 0, ClickType.PICKUP, AutoTotem.mc.player);
                    AutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, AutoTotem.mc.player);
                    this.returnOld = true;
                }
        }
    }
}
