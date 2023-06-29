package net.minecraft.command;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandReplaceItem extends CommandBase {
   private static final Map<String, Integer> SHORTCUTS = Maps.newHashMap();

   public String getCommandName() {
      return "replaceitem";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.replaceitem.usage";
   }

   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if (args.length < 1) {
         throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
      } else {
         boolean flag;
         if ("entity".equals(args[0])) {
            flag = false;
         } else {
            if (!"block".equals(args[0])) {
               throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
            }

            flag = true;
         }

         byte i;
         if (flag) {
            if (args.length < 6) {
               throw new WrongUsageException("commands.replaceitem.block.usage", new Object[0]);
            }

            i = 4;
         } else {
            if (args.length < 4) {
               throw new WrongUsageException("commands.replaceitem.entity.usage", new Object[0]);
            }

            i = 2;
         }

         String s = args[i];
         int i = i + 1;
         int j = this.getSlotForShortcut(args[i]);

         Item item;
         try {
            item = getItemByText(sender, args[i]);
         } catch (NumberInvalidException var17) {
            if (Block.getBlockFromName(args[i]) != Blocks.AIR) {
               throw var17;
            }

            item = null;
         }

         ++i;
         int k = args.length > i ? parseInt(args[i++], 1, item.getItemStackLimit()) : 1;
         int l = args.length > i ? parseInt(args[i++]) : 0;
         ItemStack itemstack = new ItemStack(item, k, l);
         if (args.length > i) {
            String s1 = buildString(args, i);

            try {
               itemstack.setTagCompound(JsonToNBT.getTagFromJson(s1));
            } catch (NBTException var16) {
               throw new CommandException("commands.replaceitem.tagError", new Object[]{var16.getMessage()});
            }
         }

         if (flag) {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            BlockPos blockpos = parseBlockPos(sender, args, 1, false);
            World world = sender.getEntityWorld();
            TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity == null || !(tileentity instanceof IInventory)) {
               throw new CommandException("commands.replaceitem.noContainer", new Object[]{blockpos.getX(), blockpos.getY(), blockpos.getZ()});
            }

            IInventory iinventory = (IInventory)tileentity;
            if (j >= 0 && j < iinventory.getSizeInventory()) {
               iinventory.setInventorySlotContents(j, itemstack);
            }
         } else {
            Entity entity = getEntity(server, sender, args[1]);
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            if (entity instanceof EntityPlayer) {
               ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
            }

            if (!entity.replaceItemInInventory(j, itemstack)) {
               throw new CommandException("commands.replaceitem.failed", new Object[]{s, k, itemstack.func_190926_b() ? "Air" : itemstack.getTextComponent()});
            }

            if (entity instanceof EntityPlayer) {
               ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
            }
         }

         sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
         notifyCommandListener(sender, this, "commands.replaceitem.success", new Object[]{s, k, itemstack.func_190926_b() ? "Air" : itemstack.getTextComponent()});
      }
   }

   private int getSlotForShortcut(String shortcut) throws CommandException {
      if (!SHORTCUTS.containsKey(shortcut)) {
         throw new CommandException("commands.generic.parameter.invalid", new Object[]{shortcut});
      } else {
         return (Integer)SHORTCUTS.get(shortcut);
      }
   }

   public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
      if (args.length == 1) {
         return getListOfStringsMatchingLastWord(args, new String[]{"entity", "block"});
      } else if (args.length == 2 && "entity".equals(args[0])) {
         return getListOfStringsMatchingLastWord(args, server.getAllUsernames());
      } else if (args.length >= 2 && args.length <= 4 && "block".equals(args[0])) {
         return getTabCompletionCoordinate(args, 1, pos);
      } else if ((args.length != 3 || !"entity".equals(args[0])) && (args.length != 5 || !"block".equals(args[0]))) {
         return args.length == 4 && "entity".equals(args[0]) || args.length == 6 && "block".equals(args[0]) ? getListOfStringsMatchingLastWord(args, Item.REGISTRY.getKeys()) : Collections.emptyList();
      } else {
         return getListOfStringsMatchingLastWord(args, SHORTCUTS.keySet());
      }
   }

   public boolean isUsernameIndex(String[] args, int index) {
      return args.length > 0 && "entity".equals(args[0]) && index == 1;
   }

   static {
      int j1;
      for(j1 = 0; j1 < 54; ++j1) {
         SHORTCUTS.put("slot.container." + j1, j1);
      }

      for(j1 = 0; j1 < 9; ++j1) {
         SHORTCUTS.put("slot.hotbar." + j1, j1);
      }

      for(j1 = 0; j1 < 27; ++j1) {
         SHORTCUTS.put("slot.inventory." + j1, 9 + j1);
      }

      for(j1 = 0; j1 < 27; ++j1) {
         SHORTCUTS.put("slot.enderchest." + j1, 200 + j1);
      }

      for(j1 = 0; j1 < 8; ++j1) {
         SHORTCUTS.put("slot.villager." + j1, 300 + j1);
      }

      for(j1 = 0; j1 < 15; ++j1) {
         SHORTCUTS.put("slot.horse." + j1, 500 + j1);
      }

      SHORTCUTS.put("slot.weapon", 98);
      SHORTCUTS.put("slot.weapon.mainhand", 98);
      SHORTCUTS.put("slot.weapon.offhand", 99);
      SHORTCUTS.put("slot.armor.head", 100 + EntityEquipmentSlot.HEAD.getIndex());
      SHORTCUTS.put("slot.armor.chest", 100 + EntityEquipmentSlot.CHEST.getIndex());
      SHORTCUTS.put("slot.armor.legs", 100 + EntityEquipmentSlot.LEGS.getIndex());
      SHORTCUTS.put("slot.armor.feet", 100 + EntityEquipmentSlot.FEET.getIndex());
      SHORTCUTS.put("slot.horse.saddle", 400);
      SHORTCUTS.put("slot.horse.armor", 401);
      SHORTCUTS.put("slot.horse.chest", 499);
   }
}
