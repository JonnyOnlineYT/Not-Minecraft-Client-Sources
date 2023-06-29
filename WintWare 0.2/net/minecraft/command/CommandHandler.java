package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class CommandHandler implements ICommandManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Map<String, ICommand> commandMap = Maps.newHashMap();
   private final Set<ICommand> commandSet = Sets.newHashSet();

   public int executeCommand(ICommandSender sender, String rawCommand) {
      rawCommand = rawCommand.trim();
      if (rawCommand.startsWith("/")) {
         rawCommand = rawCommand.substring(1);
      }

      String[] astring = rawCommand.split(" ");
      String s = astring[0];
      astring = dropFirstString(astring);
      ICommand icommand = (ICommand)this.commandMap.get(s);
      int i = 0;

      TextComponentTranslation textcomponenttranslation2;
      try {
         int j = this.getUsernameIndex(icommand, astring);
         if (icommand == null) {
            textcomponenttranslation2 = new TextComponentTranslation("commands.generic.notFound", new Object[0]);
            textcomponenttranslation2.getStyle().setColor(TextFormatting.RED);
            sender.addChatMessage(textcomponenttranslation2);
         } else if (icommand.checkPermission(this.getServer(), sender)) {
            if (j > -1) {
               List<Entity> list = EntitySelector.matchEntities(sender, astring[j], Entity.class);
               String s1 = astring[j];
               sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
               if (list.isEmpty()) {
                  throw new PlayerNotFoundException("commands.generic.selector.notFound", new Object[]{astring[j]});
               }

               Iterator var10 = list.iterator();

               while(var10.hasNext()) {
                  Entity entity = (Entity)var10.next();
                  astring[j] = entity.getCachedUniqueIdString();
                  if (this.tryExecute(sender, astring, icommand, rawCommand)) {
                     ++i;
                  }
               }

               astring[j] = s1;
            } else {
               sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
               if (this.tryExecute(sender, astring, icommand, rawCommand)) {
                  ++i;
               }
            }
         } else {
            textcomponenttranslation2 = new TextComponentTranslation("commands.generic.permission", new Object[0]);
            textcomponenttranslation2.getStyle().setColor(TextFormatting.RED);
            sender.addChatMessage(textcomponenttranslation2);
         }
      } catch (CommandException var12) {
         textcomponenttranslation2 = new TextComponentTranslation(var12.getMessage(), var12.getErrorObjects());
         textcomponenttranslation2.getStyle().setColor(TextFormatting.RED);
         sender.addChatMessage(textcomponenttranslation2);
      }

      sender.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, i);
      return i;
   }

   protected boolean tryExecute(ICommandSender sender, String[] args, ICommand command, String input) {
      TextComponentTranslation textcomponenttranslation;
      try {
         command.execute(this.getServer(), sender, args);
         return true;
      } catch (WrongUsageException var7) {
         textcomponenttranslation = new TextComponentTranslation("commands.generic.usage", new Object[]{new TextComponentTranslation(var7.getMessage(), var7.getErrorObjects())});
         textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
         sender.addChatMessage(textcomponenttranslation);
      } catch (CommandException var8) {
         textcomponenttranslation = new TextComponentTranslation(var8.getMessage(), var8.getErrorObjects());
         textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
         sender.addChatMessage(textcomponenttranslation);
      } catch (Throwable var9) {
         textcomponenttranslation = new TextComponentTranslation("commands.generic.exception", new Object[0]);
         textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
         sender.addChatMessage(textcomponenttranslation);
         LOGGER.warn("Couldn't process command: " + input, var9);
      }

      return false;
   }

   protected abstract MinecraftServer getServer();

   public ICommand registerCommand(ICommand command) {
      this.commandMap.put(command.getCommandName(), command);
      this.commandSet.add(command);
      Iterator var2 = command.getCommandAliases().iterator();

      while(true) {
         String s;
         ICommand icommand;
         do {
            if (!var2.hasNext()) {
               return command;
            }

            s = (String)var2.next();
            icommand = (ICommand)this.commandMap.get(s);
         } while(icommand != null && icommand.getCommandName().equals(s));

         this.commandMap.put(s, command);
      }
   }

   private static String[] dropFirstString(String[] input) {
      String[] astring = new String[input.length - 1];
      System.arraycopy(input, 1, astring, 0, input.length - 1);
      return astring;
   }

   public List<String> getTabCompletionOptions(ICommandSender sender, String input, @Nullable BlockPos pos) {
      String[] astring = input.split(" ", -1);
      String s = astring[0];
      if (astring.length == 1) {
         List<String> list = Lists.newArrayList();
         Iterator var7 = this.commandMap.entrySet().iterator();

         while(var7.hasNext()) {
            Entry<String, ICommand> entry = (Entry)var7.next();
            if (CommandBase.doesStringStartWith(s, (String)entry.getKey()) && ((ICommand)entry.getValue()).checkPermission(this.getServer(), sender)) {
               list.add(entry.getKey());
            }
         }

         return list;
      } else {
         if (astring.length > 1) {
            ICommand icommand = (ICommand)this.commandMap.get(s);
            if (icommand != null && icommand.checkPermission(this.getServer(), sender)) {
               return icommand.getTabCompletionOptions(this.getServer(), sender, dropFirstString(astring), pos);
            }
         }

         return Collections.emptyList();
      }
   }

   public List<ICommand> getPossibleCommands(ICommandSender sender) {
      List<ICommand> list = Lists.newArrayList();
      Iterator var3 = this.commandSet.iterator();

      while(var3.hasNext()) {
         ICommand icommand = (ICommand)var3.next();
         if (icommand.checkPermission(this.getServer(), sender)) {
            list.add(icommand);
         }
      }

      return list;
   }

   public Map<String, ICommand> getCommands() {
      return this.commandMap;
   }

   private int getUsernameIndex(ICommand command, String[] args) throws CommandException {
      if (command == null) {
         return -1;
      } else {
         for(int i = 0; i < args.length; ++i) {
            if (command.isUsernameIndex(args, i) && EntitySelector.matchesMultiplePlayers(args[i])) {
               return i;
            }
         }

         return -1;
      }
   }
}
