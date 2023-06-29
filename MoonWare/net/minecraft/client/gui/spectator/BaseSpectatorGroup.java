package net.minecraft.client.gui.spectator;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.gui.spectator.categories.TeleportToPlayer;
import net.minecraft.client.gui.spectator.categories.TeleportToTeam;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;

public class BaseSpectatorGroup implements ISpectatorMenuView
{
    private final List<ISpectatorMenuObject> items = Lists.newArrayList();

    public BaseSpectatorGroup()
    {
        items.add(new TeleportToPlayer());
        items.add(new TeleportToTeam());
    }

    public List<ISpectatorMenuObject> getItems()
    {
        return items;
    }

    public Component getPrompt()
    {
        return new TranslatableComponent("spectatorMenu.root.prompt");
    }
}
