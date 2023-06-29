package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.border.WorldBorder;

public class CommandWorldBorder extends CommandBase {
   public String getCommandName() {
      return "worldborder";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.worldborder.usage";
   }

   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if (args.length < 1) {
         throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
      } else {
         WorldBorder worldborder = this.getWorldBorder(server);
         double d6;
         double d10;
         long j1;
         if ("set".equals(args[0])) {
            if (args.length != 2 && args.length != 3) {
               throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]);
            }

            d6 = worldborder.getTargetSize();
            d10 = parseDouble(args[1], 1.0D, 6.0E7D);
            j1 = args.length > 2 ? parseLong(args[2], 0L, 9223372036854775L) * 1000L : 0L;
            if (j1 > 0L) {
               worldborder.setTransition(d6, d10, j1);
               if (d6 > d10) {
                  notifyCommandListener(sender, this, "commands.worldborder.setSlowly.shrink.success", new Object[]{String.format("%.1f", d10), String.format("%.1f", d6), Long.toString(j1 / 1000L)});
               } else {
                  notifyCommandListener(sender, this, "commands.worldborder.setSlowly.grow.success", new Object[]{String.format("%.1f", d10), String.format("%.1f", d6), Long.toString(j1 / 1000L)});
               }
            } else {
               worldborder.setTransition(d10);
               notifyCommandListener(sender, this, "commands.worldborder.set.success", new Object[]{String.format("%.1f", d10), String.format("%.1f", d6)});
            }
         } else if ("add".equals(args[0])) {
            if (args.length != 2 && args.length != 3) {
               throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]);
            }

            d6 = worldborder.getDiameter();
            d10 = d6 + parseDouble(args[1], -d6, 6.0E7D - d6);
            j1 = worldborder.getTimeUntilTarget() + (args.length > 2 ? parseLong(args[2], 0L, 9223372036854775L) * 1000L : 0L);
            if (j1 > 0L) {
               worldborder.setTransition(d6, d10, j1);
               if (d6 > d10) {
                  notifyCommandListener(sender, this, "commands.worldborder.setSlowly.shrink.success", new Object[]{String.format("%.1f", d10), String.format("%.1f", d6), Long.toString(j1 / 1000L)});
               } else {
                  notifyCommandListener(sender, this, "commands.worldborder.setSlowly.grow.success", new Object[]{String.format("%.1f", d10), String.format("%.1f", d6), Long.toString(j1 / 1000L)});
               }
            } else {
               worldborder.setTransition(d10);
               notifyCommandListener(sender, this, "commands.worldborder.set.success", new Object[]{String.format("%.1f", d10), String.format("%.1f", d6)});
            }
         } else if ("center".equals(args[0])) {
            if (args.length != 3) {
               throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]);
            }

            BlockPos blockpos = sender.getPosition();
            double d1 = parseDouble((double)blockpos.getX() + 0.5D, args[1], true);
            double d3 = parseDouble((double)blockpos.getZ() + 0.5D, args[2], true);
            worldborder.setCenter(d1, d3);
            notifyCommandListener(sender, this, "commands.worldborder.center.success", new Object[]{d1, d3});
         } else if ("damage".equals(args[0])) {
            if (args.length < 2) {
               throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]);
            }

            if ("buffer".equals(args[1])) {
               if (args.length != 3) {
                  throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]);
               }

               d6 = parseDouble(args[2], 0.0D);
               d10 = worldborder.getDamageBuffer();
               worldborder.setDamageBuffer(d6);
               notifyCommandListener(sender, this, "commands.worldborder.damage.buffer.success", new Object[]{String.format("%.1f", d6), String.format("%.1f", d10)});
            } else if ("amount".equals(args[1])) {
               if (args.length != 3) {
                  throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]);
               }

               d6 = parseDouble(args[2], 0.0D);
               d10 = worldborder.getDamageAmount();
               worldborder.setDamageAmount(d6);
               notifyCommandListener(sender, this, "commands.worldborder.damage.amount.success", new Object[]{String.format("%.2f", d6), String.format("%.2f", d10)});
            }
         } else if ("warning".equals(args[0])) {
            if (args.length < 2) {
               throw new WrongUsageException("commands.worldborder.warning.usage", new Object[0]);
            }

            int i1;
            int k;
            if ("time".equals(args[1])) {
               if (args.length != 3) {
                  throw new WrongUsageException("commands.worldborder.warning.time.usage", new Object[0]);
               }

               k = parseInt(args[2], 0);
               i1 = worldborder.getWarningTime();
               worldborder.setWarningTime(k);
               notifyCommandListener(sender, this, "commands.worldborder.warning.time.success", new Object[]{k, i1});
            } else if ("distance".equals(args[1])) {
               if (args.length != 3) {
                  throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]);
               }

               k = parseInt(args[2], 0);
               i1 = worldborder.getWarningDistance();
               worldborder.setWarningDistance(k);
               notifyCommandListener(sender, this, "commands.worldborder.warning.distance.success", new Object[]{k, i1});
            }
         } else {
            if (!"get".equals(args[0])) {
               throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
            }

            d6 = worldborder.getDiameter();
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor(d6 + 0.5D));
            sender.addChatMessage(new TextComponentTranslation("commands.worldborder.get.success", new Object[]{String.format("%.0f", d6)}));
         }

      }
   }

   protected WorldBorder getWorldBorder(MinecraftServer server) {
      return server.worldServers[0].getWorldBorder();
   }

   public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
      if (args.length == 1) {
         return getListOfStringsMatchingLastWord(args, new String[]{"set", "center", "damage", "warning", "add", "get"});
      } else if (args.length == 2 && "damage".equals(args[0])) {
         return getListOfStringsMatchingLastWord(args, new String[]{"buffer", "amount"});
      } else if (args.length >= 2 && args.length <= 3 && "center".equals(args[0])) {
         return getTabCompletionCoordinateXZ(args, 1, pos);
      } else {
         return args.length == 2 && "warning".equals(args[0]) ? getListOfStringsMatchingLastWord(args, new String[]{"time", "distance"}) : Collections.emptyList();
      }
   }
}
