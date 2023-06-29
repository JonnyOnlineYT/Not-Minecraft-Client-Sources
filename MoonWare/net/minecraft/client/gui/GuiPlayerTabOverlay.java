package net.minecraft.client.gui;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.hud.IngameHud;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.Formatting;
import net.minecraft.world.GameType;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.misc.Disabler;
import org.moonware.client.feature.impl.misc.StreamerMode;
import org.moonware.client.utils.Interpolator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GuiPlayerTabOverlay extends Gui
{
    public static final Ordering<NetworkPlayerInfo> ENTRY_ORDERING = Ordering.from(new GuiPlayerTabOverlay.PlayerComparator());
    private final Minecraft mc;
    private final IngameHud guiIngame;
    private Component footer;
    private Component header;

    /**
     * The last time the playerlist was opened (went from not being renderd, to being rendered)
     */
    private long lastTimeOpened;

    public static float animTicks;
    public static float addition;
    public static float lastTime;

    /** Weither or not the playerlist is currently being rendered */
    private boolean isBeingRendered;

    public GuiPlayerTabOverlay(Minecraft mcIn, IngameHud guiIngameIn)
    {
        mc = mcIn;
        guiIngame = guiIngameIn;
    }

    /**
     * Returns the name that should be renderd for the player supplied
     */
    public String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn)
    {
        return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().asFormattedString() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
    }

    /**
     * Called by GuiIngame to update the information stored in the playerlist, does not actually render the list,
     * however.
     */
    public void updatePlayerList(boolean willBeRendered)
    {
        if (willBeRendered && !isBeingRendered)
        {
            animTicks = -50F;
            addition = 0F;
            lastTime = 0F;
            lastTimeOpened = Minecraft.getSystemTime();
        }

        isBeingRendered = willBeRendered;
    }

    /**
     * Renders the playerlist, its background, headers and footers.
     */
    public void renderPlayerlist(int width, Scoreboard scoreboardIn, @Nullable ScoreObjective scoreObjectiveIn) {
        addition = lastTime < Minecraft.timer.renderPartialTicks ? Minecraft.timer.renderPartialTicks - lastTime : (lastTime != Minecraft.timer.renderPartialTicks ? 1 - lastTime + Minecraft.timer.renderPartialTicks : 0);
        animTicks = (float) Interpolator.linear(animTicks, 3,2f/10);
        NetHandlerPlayClient nethandlerplayclient = Minecraft.player.connection;
        List<NetworkPlayerInfo> list = ENTRY_ORDERING.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
        int i = 0;
        int j = 0;

        for (NetworkPlayerInfo networkplayerinfo : list)
        {
            int k = Minecraft.font.getStringWidth(getPlayerName(networkplayerinfo));
            i = Math.max(i, k);

            if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreCriteria.EnumRenderType.HEARTS)
            {
                k = Minecraft.font.getStringWidth(" " + scoreboardIn.getOrCreateScore(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
                j = Math.max(j, k);
            }
        }

        list = list.subList(0, Math.min(list.size(), 80));
        int l3 = list.size();
        int i4 = l3;
        int j4;

        for (j4 = 1; i4 > 20; i4 = (l3 + j4 - 1) / j4)
        {
            ++j4;
        }

        boolean flag = true;
        int l;

        if (scoreObjectiveIn != null)
        {
            if (scoreObjectiveIn.getRenderType() == IScoreCriteria.EnumRenderType.HEARTS)
            {
                l = 90;
            }
            else
            {
                l = j;
            }
        }
        else
        {
            l = 0;
        }

        int i1 = Math.min(j4 * ((flag ? 9 : 0) + i + l + 13), width - 50) / j4;
        int j1 = width / 2 - (i1 * j4 + (j4 - 1) * 5) / 2;
        int k1 = 10;
        int l1 = i1 * j4 + (j4 - 1) * 5;
        List<String> list1 = null;

        GlStateManager.pushMatrix();
        GlStateManager.translate(width / 2F, k1 / 2F, 1F);
        //GlStateManager.scale(animTicks * 0.05F, animTicks * 0.05F, animTicks * 0.05F);
        GlStateManager.translate(-width / 2F, -k1 / 2F * -animTicks, 1);
        lastTime = Minecraft.timer.renderPartialTicks;

        if (header != null)
        {
            list1 = Minecraft.font.split(header.asFormattedString(), width - 50);

            for (String s : list1)
            {
                l1 = Math.max(l1, Minecraft.font.getStringWidth(s));
            }
        }

        List<String> list2 = null;

        if (footer != null)
        {
            list2 = Minecraft.font.split(footer.asFormattedString(), width - 50);

            for (String s1 : list2)
            {
                l1 = Math.max(l1, Minecraft.font.getStringWidth(s1));
            }
        }

        if (list1 != null)
        {
            Gui.drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list1.size() * Minecraft.font.height, Integer.MIN_VALUE);

            for (String s2 : list1)
            {
                int i2 = Minecraft.font.getStringWidth(s2);
                Minecraft.font.drawStringWithShadow(s2, (float)(width / 2 - i2 / 2), (float)k1, -1);
                k1 += Minecraft.font.height;
            }

            ++k1;
        }

        Gui.drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + i4 * 9, Integer.MIN_VALUE);

        for (int k4 = 0; k4 < l3; ++k4)
        {
            int l4 = k4 / i4;
            int i5 = k4 % i4;
            int j2 = j1 + l4 * i1 + l4 * 5;
            int k2 = k1 + i5 * 9;
            Gui.drawRect(j2, k2, j2 + i1, k2 + 8, 553648127);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            if (k4 < list.size())
            {
                NetworkPlayerInfo networkplayerinfo1 = list.get(k4);
                GameProfile gameprofile = networkplayerinfo1.getGameProfile();

                if (flag)
                {
                    EntityPlayer entityplayer = Minecraft.world.getPlayerEntityByUUID(gameprofile.getId());
                    boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && ("Dinnerbone".equals(gameprofile.getName()) || "Grumm".equals(gameprofile.getName()));
                    Minecraft.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
                    int l2 = 8 + (flag1 ? 8 : 0);
                    int i3 = 8 * (flag1 ? -1 : 1);
                    Gui.drawScaledCustomSizeModalRect(j2, k2, 8.0F, (float)l2, 8, i3, 8, 8, 64.0F, 64.0F);

                    if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT))
                    {
                        int j3 = 8 + (flag1 ? 8 : 0);
                        int k3 = 8 * (flag1 ? -1 : 1);
                        Gui.drawScaledCustomSizeModalRect(j2, k2, 40.0F, (float)j3, 8, k3, 8, 8, 64.0F, 64.0F);
                    }

                    j2 += 9;
                }

                String s4 = getPlayerName(networkplayerinfo1);

                if (MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState()) {
                    if (StreamerMode.tabSpoof.getBoolValue()) {
                        s4 = "\247aProtected";
                    }
                    if (StreamerMode.ownName.getBoolValue()) {
                        s4 = s4.replace(Minecraft.player.getName(), Formatting.GREEN + "Protected");
                    }
                }else {
                    updatePlayerList(true);
                }

                if (networkplayerinfo1.getGameType() == GameType.SPECTATOR)
                {
                    Minecraft.font.drawStringWithShadow(Formatting.ITALIC + s4, (float)j2, (float)k2, -1862270977);
                }
                else
                {
                    Minecraft.font.drawStringWithShadow(s4, (float)j2, (float)k2, -1);
                }

                if (scoreObjectiveIn != null && networkplayerinfo1.getGameType() != GameType.SPECTATOR)
                {
                    int k5 = j2 + i + 1;
                    int l5 = k5 + l;

                    if (l5 - k5 > 5)
                    {
                        drawScoreboardValues(scoreObjectiveIn, k2, gameprofile.getName(), k5, l5, networkplayerinfo1);
                    }
                }

                drawPing(i1, j2 - (flag ? 9 : 0), k2, networkplayerinfo1);
            }
        }

        if (list2 != null)
        {
            k1 = k1 + i4 * 9 + 1;
            Gui.drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list2.size() * Minecraft.font.height, Integer.MIN_VALUE);

            for (String s3 : list2)
            {
                int j5 = Minecraft.font.getStringWidth(s3);
                Minecraft.font.drawStringWithShadow(s3, (float)(width / 2 - j5 / 2), (float)k1, -1);
                k1 += Minecraft.font.height;
            }
        }
        GlStateManager.popMatrix();
    }
    public static List<EntityPlayer> getPlayers() {
        List<NetworkPlayerInfo> list = ENTRY_ORDERING.sortedCopy(Minecraft.player.connection.getPlayerInfoMap());
        ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
        for (NetworkPlayerInfo player : list) {
            if (player == null) continue;
            players.add(Minecraft.world.getPlayerEntityByName(player.getGameProfile().getName()));
        }
        return players;
    }
    protected void drawPing(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo networkPlayerInfoIn)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(Gui.ICONS);
        int j = 0;

        if(MoonWare.featureManager.getFeatureByClass(Disabler.class).getState()) {
            if(networkPlayerInfoIn.getGameProfile() == Minecraft.player.getGameProfile()) {
                j = 5;
            }
        }
        else if (networkPlayerInfoIn.getResponseTime() < 0)
        {
            j = 5;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 150)
        {
            j = 0;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 300)
        {
            j = 1;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 600)
        {
            j = 2;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 1000)
        {
            j = 3;
        }
        else
        {
            j = 4;
        }

        zLevel += 100.0F;
        drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, p_175245_3_, 0, 176 + j * 8, 10, 8);
        zLevel -= 100.0F;
    }

    private void drawScoreboardValues(ScoreObjective objective, int p_175247_2_, String name, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo info)
    {
        int i = objective.getScoreboard().getOrCreateScore(name, objective).getScorePoints();

        if (objective.getRenderType() == IScoreCriteria.EnumRenderType.HEARTS)
        {
            Minecraft.getTextureManager().bindTexture(Gui.ICONS);

            if (lastTimeOpened == info.getRenderVisibilityId())
            {
                if (i < info.getLastHealth())
                {
                    info.setLastHealthTime(Minecraft.getSystemTime());
                    info.setHealthBlinkTime(guiIngame.getUpdateCounter() + 20);
                }
                else if (i > info.getLastHealth())
                {
                    info.setLastHealthTime(Minecraft.getSystemTime());
                    info.setHealthBlinkTime(guiIngame.getUpdateCounter() + 10);
                }
            }

            if (Minecraft.getSystemTime() - info.getLastHealthTime() > 1000L || lastTimeOpened != info.getRenderVisibilityId())
            {
                info.setLastHealth(i);
                info.setDisplayHealth(i);
                info.setLastHealthTime(Minecraft.getSystemTime());
            }

            info.setRenderVisibilityId(lastTimeOpened);
            info.setLastHealth(i);
            int j = MathHelper.ceil((float)Math.max(i, info.getDisplayHealth()) / 2.0F);
            int k = Math.max(MathHelper.ceil((float)(i / 2)), Math.max(MathHelper.ceil((float)(info.getDisplayHealth() / 2)), 10));
            boolean flag = info.getHealthBlinkTime() > (long) guiIngame.getUpdateCounter() && (info.getHealthBlinkTime() - (long) guiIngame.getUpdateCounter()) / 3L % 2L == 1L;

            if (j > 0)
            {
                float f = Math.min((float)(p_175247_5_ - p_175247_4_ - 4) / (float)k, 9.0F);

                if (f > 3.0F)
                {
                    for (int l = j; l < k; ++l)
                    {
                        drawTexturedModalRect((float)p_175247_4_ + (float)l * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                    }

                    for (int j1 = 0; j1 < j; ++j1)
                    {
                        drawTexturedModalRect((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);

                        if (flag)
                        {
                            if (j1 * 2 + 1 < info.getDisplayHealth())
                            {
                                drawTexturedModalRect((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, 70, 0, 9, 9);
                            }

                            if (j1 * 2 + 1 == info.getDisplayHealth())
                            {
                                drawTexturedModalRect((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, 79, 0, 9, 9);
                            }
                        }

                        if (j1 * 2 + 1 < i)
                        {
                            drawTexturedModalRect((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, j1 >= 10 ? 160 : 52, 0, 9, 9);
                        }

                        if (j1 * 2 + 1 == i)
                        {
                            drawTexturedModalRect((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, j1 >= 10 ? 169 : 61, 0, 9, 9);
                        }
                    }
                }
                else
                {
                    float f1 = MathHelper.clamp((float)i / 20.0F, 0.0F, 1.0F);
                    int i1 = (int)((1.0F - f1) * 255.0F) << 16 | (int)(f1 * 255.0F) << 8;
                    String s = "" + (float)i / 2.0F;

                    if (p_175247_5_ - Minecraft.font.getStringWidth(s + "hp") >= p_175247_4_)
                    {
                        s = s + "hp";
                    }

                    Minecraft.font.drawStringWithShadow(s, (float)((p_175247_5_ + p_175247_4_) / 2 - Minecraft.font.getStringWidth(s) / 2), (float)p_175247_2_, i1);
                }
            }
        }
        else
        {
            String s1 = Formatting.YELLOW + "" + i;
            Minecraft.font.drawStringWithShadow(s1, (float)(p_175247_5_ - Minecraft.font.getStringWidth(s1)), (float)p_175247_2_, 16777215);
        }
    }



    public void setFooter(@Nullable Component footerIn)
    {
        footer = footerIn;
    }

    public void setHeader(@Nullable Component headerIn)
    {
        header = headerIn;
    }

    public void cleanup()
    {
        header = null;
        footer = null;
    }

    static class PlayerComparator implements Comparator<NetworkPlayerInfo>
    {
        private PlayerComparator()
        {
        }

        public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_)
        {
            ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
            ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != GameType.SPECTATOR, p_compare_2_.getGameType() != GameType.SPECTATOR).compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "", scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "").compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
        }
    }
}
