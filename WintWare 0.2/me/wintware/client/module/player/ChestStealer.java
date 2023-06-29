package me.wintware.client.module.player;

import java.util.Iterator;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.other.TimerUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class ChestStealer extends Module {
   private int cooldown = 1;
   TimerUtils timer = new TimerUtils();
   private boolean isStealing;
   private double first = 0.0D;
   private int sigmaclose;
   String[] list = new String[]{"menu", "selector", "game", "gui", "server", "inventory", "play", "teleporter", "shop", "melee", "armor", "block", "castle", "mini", "warp", "teleport", "user", "team", "tool", "sure", "trade", "cancel", "accept", "soul", "book", "recipe", "profile", "tele", "port", "map", "kit", "select", "lobby", "vault", "lock", "anticheat", "travel", "settings", "user", "preference", "compass", "cake", "wars", "buy", "upgrade", "ranged", "potions", "utility", "pvp"};

   public ChestStealer() {
      super("ChestStealer", Category.Player);
   }

   @EventTarget
   public void onPre(EventPreMotionUpdate event) {
      if (this.getState()) {
         if (this.timer.hasReached(2000.0D) && this.isStealing) {
            this.timer.reset();
            this.isStealing = false;
            Iterator var2 = mc.world.loadedTileEntityList.iterator();

            while(var2.hasNext()) {
               Object o = var2.next();
               if (o instanceof TileEntityChest) {
                  TileEntityChest chest = (TileEntityChest)o;
                  float x = (float)chest.getPos().getX();
                  float y = (float)chest.getPos().getY();
                  float z = (float)chest.getPos().getZ();
                  if (!this.isStealing && !chest.isInvalid() && mc.player.getDistance((double)x, (double)y, (double)z) < 4.0D && this.timer.hasReached(1000.0D) && mc.currentScreen == null) {
                     this.isStealing = true;
                     mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(chest.getPos(), this.getFacingDirection(chest.getPos()), EnumHand.MAIN_HAND, x, y, z));
                     this.timer.reset();
                     break;
                  }
               }
            }
         }

         if (mc.currentScreen instanceof GuiChest) {
            boolean item_found = false;
            if (this.cooldown > 0) {
               --this.cooldown;
               return;
            }

            GuiChest guiChest = (GuiChest)mc.currentScreen;
            String name = guiChest.lowerChestInventory.getDisplayName().getUnformattedComponentText().toLowerCase();
            String[] arrayOfString;
            int j = (arrayOfString = this.list).length;

            for(byte b = 0; b < j; ++b) {
               String str = arrayOfString[b];
               if (name.contains(str)) {
                  return;
               }
            }

            if (!this.isStealing) {
               this.first = 0.0D;
            }

            ++this.first;
            this.isStealing = true;
            if (this.first <= 0.0D) {
               return;
            }

            EntityPlayerSP player = mc.player;
            Container chest = player.openContainer;
            boolean hasSpace = false;

            int i;
            Slot slot;
            for(i = chest.inventorySlots.size() - 36; i < chest.inventorySlots.size(); ++i) {
               slot = chest.getSlot(i);
               if (slot == null || !slot.getHasStack()) {
                  hasSpace = true;
                  break;
               }
            }

            if (!hasSpace) {
               return;
            }

            while(this.cooldown == 0) {
               for(i = 0; i < chest.inventorySlots.size() - 36; ++i) {
                  slot = chest.getSlot(i);
                  ItemStack stack = guiChest.lowerChestInventory.getStackInSlot(i);
                  if (slot.getHasStack() && slot.getStack() != null && !this.isBad(stack)) {
                     mc.playerController.windowClick(mc.player.openContainer.windowId, i, 1, ClickType.QUICK_MOVE, mc.player);
                     mc.playerController.windowClick(guiChest.inventorySlots.windowId, i, 1, ClickType.QUICK_MOVE, mc.player);
                     this.cooldown = 10;
                     item_found = true;
                     break;
                  }
               }

               if (!item_found) {
                  mc.displayGuiScreen((GuiScreen)null);
                  player.connection.sendPacket(new CPacketCloseWindow(chest.windowId));
                  this.isStealing = false;
                  break;
               }

               hasSpace = false;

               for(i = chest.inventorySlots.size() - 36; i < chest.inventorySlots.size(); ++i) {
                  slot = chest.getSlot(i);
                  if (slot == null || !slot.getHasStack()) {
                     hasSpace = true;
                     break;
                  }
               }

               if (!hasSpace) {
                  return;
               }
            }
         } else {
            this.isStealing = false;
         }
      }

   }

   public void onDisable() {
      super.onDisable();
   }

   private EnumFacing getFacingDirection(BlockPos pos) {
      EnumFacing direction = null;
      direction = EnumFacing.UP;
      RayTraceResult rayResult = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ), new Vec3d((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D));
      return rayResult != null ? rayResult.sideHit : direction;
   }

   private boolean isBad(ItemStack item) {
      ItemStack is = null;
      float lastDamage = -1.0F;

      for(int i = 9; i < 45; ++i) {
         if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is1 = mc.player.inventoryContainer.getSlot(i).getStack();
            if (is1.getItem() instanceof ItemSword && item.getItem() instanceof ItemSword && lastDamage < this.getDamage(is1)) {
               lastDamage = this.getDamage(is1);
               is = is1;
            }
         }
      }

      if (is != null && is.getItem() instanceof ItemSword && item.getItem() instanceof ItemSword) {
         float currentDamage = this.getDamage(is);
         float itemDamage = this.getDamage(item);
         if (itemDamage > currentDamage) {
            return false;
         }
      }

      return item != null && (item.getItem().getUnlocalizedName().contains("tnt") || item.getItem().getUnlocalizedName().contains("stick") || item.getItem().getUnlocalizedName().contains("egg") && !item.getItem().getUnlocalizedName().contains("leg") || item.getItem().getUnlocalizedName().contains("string") || item.getItem().getUnlocalizedName().contains("flint") || item.getItem().getUnlocalizedName().contains("compass") || item.getItem().getUnlocalizedName().contains("feather") || item.getItem().getUnlocalizedName().contains("bucket") || item.getItem().getUnlocalizedName().contains("snow") || item.getItem().getUnlocalizedName().contains("fish") || item.getItem().getUnlocalizedName().contains("enchant") || item.getItem().getUnlocalizedName().contains("exp") || item.getItem().getUnlocalizedName().contains("shears") || item.getItem().getUnlocalizedName().contains("anvil") || item.getItem().getUnlocalizedName().contains("torch") || item.getItem().getUnlocalizedName().contains("seeds") || item.getItem().getUnlocalizedName().contains("leather") || item.getItem() instanceof ItemPickaxe || item.getItem() instanceof ItemGlassBottle || item.getItem() instanceof ItemTool || item.getItem().getUnlocalizedName().contains("piston") || item.getItem().getUnlocalizedName().contains("potion"));
   }

   public static boolean isSplashPotion(ItemStack stack) {
      return stack.getItem() == Items.SPLASH_POTION;
   }

   public static boolean hasEffect(ItemStack stack, Potion potion) {
      Iterator var2 = PotionUtils.getEffectsFromStack(stack).iterator();

      PotionEffect effect;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         effect = (PotionEffect)var2.next();
      } while(effect.getPotion() != potion);

      return true;
   }

   private float getDamage(ItemStack stack) {
      return !(stack.getItem() instanceof ItemSword) ? 0.0F : (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(1), stack) * 1.25F + ((ItemSword)stack.getItem()).attackDamage;
   }
}
