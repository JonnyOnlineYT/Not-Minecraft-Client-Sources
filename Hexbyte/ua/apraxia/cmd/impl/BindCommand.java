package ua.apraxia.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import ua.apraxia.Hexbyte;
import ua.apraxia.cmd.CommandAbstract;
import ua.apraxia.modules.Module;
import ua.apraxia.utility.other.ChatUtility;

public class BindCommand extends CommandAbstract {

    public BindCommand() {
        super("bind", "bind", ".bind add " + "<name> <key> /" +" .bind remove " + "<name> <key>", ".bind add " + "<name> <key> | .bind remove " + "<name> <key> | .bind clear", "bind");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length == 4) {
                String moduleName = arguments[2];
                String bind = arguments[3].toUpperCase();
                Module feature = Hexbyte.getInstance().moduleManagment.getFeature(moduleName);
                if (arguments[0].equals("bind")) {
                    switch (arguments[1]) {
                        case "add":
                            feature.setBind(Keyboard.getKeyIndex(bind));
                            ChatUtility.addChatMessage(ChatFormatting.AQUA + feature.getModuleName() + ChatFormatting.GRAY + " Добавлен бинд на клавишу " + bind);
                            break;
                        case "remove":
                            feature.setBind(0);
                            ChatUtility.addChatMessage(ChatFormatting.AQUA  + feature.getModuleName() + ChatFormatting.GRAY + " Бинд " + bind + " удален");
                            break;
                        case "clear":
                            if (!feature.getModuleName().equals("ClickUI")) {
                                feature.setBind(0);
                            }
                            break;
                        case "list":
                            if (feature.getBind() == 0) {
                                ChatUtility.addChatMessage(ChatFormatting.RED + "Ваш список биндов пуст!");
                                return;
                            }
                            Hexbyte.getInstance().moduleManagment.getAllFeatures().forEach(feature1 -> ChatUtility.addChatMessage(ChatFormatting.GRAY + "Список биндов: " + ChatFormatting.WHITE + "Binds Name: " + ChatFormatting.RED + feature1.getBind()));
                    }
                }
            } else {
                ChatUtility.addChatMessage(this.getUsage());
            }
        } catch (Exception ignored) {

        }
    }
}
