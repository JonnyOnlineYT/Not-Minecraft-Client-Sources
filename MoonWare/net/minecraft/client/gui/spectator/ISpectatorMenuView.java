package net.minecraft.client.gui.spectator;

import java.util.List;
import net.minecraft.util.text.Component;

public interface ISpectatorMenuView
{
    List<ISpectatorMenuObject> getItems();

    Component getPrompt();
}
