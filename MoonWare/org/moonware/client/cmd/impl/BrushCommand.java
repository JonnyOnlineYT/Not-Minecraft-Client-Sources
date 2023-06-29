package org.moonware.client.cmd.impl;

import net.minecraft.client.Minecraft;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.cmd.CommandAbstract;
import org.moonware.client.ui.components.draggable.GuiHudEditor;

public class BrushCommand extends CommandAbstract {

    Minecraft mc = Minecraft.getMinecraft();

    public BrushCommand() {
        super("brush", "brush", "§6.brush open", "brush");
    }

    @Override
    public void execute(String... args) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("brush")) {
                try {
                    if (args[1].equals("open")) {
                        Minecraft.openScreen(new GuiHudEditor());
                        MWUtils.sendChat("Чтобы закрыть меню, нажмите кнопку ESC");
                    }
                } catch (Exception ignored) {
                }
            }
        } else {
            MWUtils.sendChat(getUsage());
        }
    }
}
