package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandTestFor extends CommandBase {
   public String getCommandName() {
      return "testfor";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.testfor.usage";
   }

   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if (args.length < 1) {
         throw new WrongUsageException("commands.testfor.usage", new Object[0]);
      } else {
         Entity entity = getEntity(server, sender, args[0]);
         NBTTagCompound nbttagcompound = null;
         if (args.length >= 2) {
            try {
               nbttagcompound = JsonToNBT.getTagFromJson(buildString(args, 1));
            } catch (NBTException var7) {
               throw new CommandException("commands.testfor.tagError", new Object[]{var7.getMessage()});
            }
         }

         if (nbttagcompound != null) {
            NBTTagCompound nbttagcompound1 = entityToNBT(entity);
            if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1, true)) {
               throw new CommandException("commands.testfor.failure", new Object[]{entity.getName()});
            }
         }

         notifyCommandListener(sender, this, "commands.testfor.success", new Object[]{entity.getName()});
      }
   }

   public boolean isUsernameIndex(String[] args, int index) {
      return index == 0;
   }

   public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
      return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
   }
}
