package net.minecraft.client.gui.spectator.categories;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;

public class TeleportToTeam implements ISpectatorMenuView, ISpectatorMenuObject
{
    private final List<ISpectatorMenuObject> items = Lists.newArrayList();

    public TeleportToTeam()
    {
        Minecraft minecraft = Minecraft.getMinecraft();

        for (ScorePlayerTeam scoreplayerteam : Minecraft.world.getScoreboard().getTeams())
        {
            items.add(new TeleportToTeam.TeamSelectionObject(scoreplayerteam));
        }
    }

    public List<ISpectatorMenuObject> getItems()
    {
        return items;
    }

    public Component getPrompt()
    {
        return new TranslatableComponent("spectatorMenu.team_teleport.prompt");
    }

    public void selectItem(SpectatorMenu menu)
    {
        menu.selectCategory(this);
    }

    public Component getSpectatorName()
    {
        return new TranslatableComponent("spectatorMenu.team_teleport");
    }

    public void renderIcon(float p_178663_1_, int alpha)
    {
        Minecraft.getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 16.0F, 0.0F, 16, 16, 256.0F, 256.0F);
    }

    public boolean isEnabled()
    {
        for (ISpectatorMenuObject ispectatormenuobject : items)
        {
            if (ispectatormenuobject.isEnabled())
            {
                return true;
            }
        }

        return false;
    }

    class TeamSelectionObject implements ISpectatorMenuObject
    {
        private final ScorePlayerTeam team;
        private final Namespaced location;
        private final List<NetworkPlayerInfo> players;

        public TeamSelectionObject(ScorePlayerTeam p_i45492_2_)
        {
            team = p_i45492_2_;
            players = Lists.newArrayList();

            for (String s : p_i45492_2_.getMembershipCollection())
            {
                NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(s);

                if (networkplayerinfo != null)
                {
                    players.add(networkplayerinfo);
                }
            }

            if (players.isEmpty())
            {
                location = DefaultPlayerSkin.getDefaultSkinLegacy();
            }
            else
            {
                String s1 = players.get((new Random()).nextInt(players.size())).getGameProfile().getName();
                location = AbstractClientPlayer.getLocationSkin(s1);
                AbstractClientPlayer.getDownloadImageSkin(location, s1);
            }
        }

        public void selectItem(SpectatorMenu menu)
        {
            menu.selectCategory(new TeleportToPlayer(players));
        }

        public Component getSpectatorName()
        {
            return new TextComponent(team.getTeamName());
        }

        public void renderIcon(float p_178663_1_, int alpha)
        {
            int i = -1;
            String s = Font.getFormatFromString(team.getColorPrefix());

            if (s.length() >= 2)
            {
                i = Minecraft.font.getColorCode(s.charAt(1));
            }

            if (i >= 0)
            {
                float f = (float)(i >> 16 & 255) / 255.0F;
                float f1 = (float)(i >> 8 & 255) / 255.0F;
                float f2 = (float)(i & 255) / 255.0F;
                Gui.drawRect(1, 1, 15, 15, MathHelper.rgb(f * p_178663_1_, f1 * p_178663_1_, f2 * p_178663_1_) | alpha << 24);
            }

            Minecraft.getTextureManager().bindTexture(location);
            GlStateManager.color(p_178663_1_, p_178663_1_, p_178663_1_, (float)alpha / 255.0F);
            Gui.drawScaledCustomSizeModalRect(2, 2, 8.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
            Gui.drawScaledCustomSizeModalRect(2, 2, 40.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
        }

        public boolean isEnabled()
        {
            return !players.isEmpty();
        }
    }
}
