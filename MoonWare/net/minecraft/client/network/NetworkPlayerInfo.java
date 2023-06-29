package net.minecraft.client.network;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Component;
import net.minecraft.world.GameType;

import javax.annotation.Nullable;
import java.util.Map;

public class NetworkPlayerInfo
{
    /**
     * The GameProfile for the player represented by this NetworkPlayerInfo instance
     */
    private final GameProfile gameProfile;
    Map<MinecraftProfileTexture.Type, Namespaced> playerTextures = Maps.newEnumMap(MinecraftProfileTexture.Type.class);
    private GameType gameType;

    /** Player response time to server in milliseconds */
    private int responseTime;
    private boolean playerTexturesLoaded;
    private String skinType;

    /**
     * When this is non-null, it is displayed instead of the player's real name
     */
    private Component displayName;
    private int lastHealth;
    private int displayHealth;
    private long lastHealthTime;
    private long healthBlinkTime;
    private long renderVisibilityId;

    public NetworkPlayerInfo(GameProfile profile)
    {
        gameProfile = profile;
    }

    public NetworkPlayerInfo(SPacketPlayerListItem.AddPlayerData entry)
    {
        gameProfile = entry.getProfile();
        gameType = entry.getGameMode();
        responseTime = entry.getPing();
        displayName = entry.getDisplayName();
    }

    /**
     * Returns the GameProfile for the player represented by this NetworkPlayerInfo instance
     */
    public GameProfile getGameProfile()
    {
        return gameProfile;
    }

    public GameType getGameType()
    {
        return gameType;
    }

    protected void setGameType(GameType gameMode)
    {
        gameType = gameMode;
    }

    public int getResponseTime()
    {
        return responseTime;
    }

    public void setResponseTime(int latency)
    {
        responseTime = latency;
    }

    public boolean hasLocationSkin()
    {
        return getLocationSkin() != null;
    }

    public String getSkinType()
    {
        return skinType == null ? DefaultPlayerSkin.getSkinType(gameProfile.getId()) : skinType;
    }

    public Namespaced getLocationSkin()
    {
        loadPlayerTextures();
        return MoreObjects.firstNonNull(playerTextures.get(MinecraftProfileTexture.Type.SKIN), DefaultPlayerSkin.getDefaultSkin(gameProfile.getId()));
    }

    @Nullable
    public Namespaced getLocationCape()
    {
        loadPlayerTextures();
        return playerTextures.get(MinecraftProfileTexture.Type.CAPE);
    }

    @Nullable

    /**
     * Gets the special Elytra texture for the player.
     */
    public Namespaced getLocationElytra()
    {
        loadPlayerTextures();
        return playerTextures.get(MinecraftProfileTexture.Type.ELYTRA);
    }

    @Nullable
    public ScorePlayerTeam getPlayerTeam()
    {
        return Minecraft.world.getScoreboard().getPlayersTeam(getGameProfile().getName());
    }

    protected void loadPlayerTextures() {
        synchronized (this) {
            if (!playerTexturesLoaded) {
                playerTexturesLoaded = true;
                Minecraft.getSkinManager().loadProfileTextures(gameProfile, new SkinManager.SkinAvailableCallback() {
                    public void skinAvailable(MinecraftProfileTexture.Type typeIn, Namespaced location, MinecraftProfileTexture profileTexture) {
                        switch (typeIn) {
                            case SKIN:
                                playerTextures.put(MinecraftProfileTexture.Type.SKIN, location);
                                skinType = profileTexture.getMetadata("model");

                                if (skinType == null) {
                                    skinType = "default";
                                }

                                break;

                            case CAPE:
                                playerTextures.put(MinecraftProfileTexture.Type.CAPE, location);
                                break;

                            case ELYTRA:
                                playerTextures.put(MinecraftProfileTexture.Type.ELYTRA, location);
                        }
                    }
                }, true);
            }
        }
    }

    public void setDisplayName(@Nullable Component displayNameIn)
    {
        displayName = displayNameIn;
    }

    @Nullable
    public Component getDisplayName()
    {
        return displayName;
    }

    public int getLastHealth()
    {
        return lastHealth;
    }

    public void setLastHealth(int p_178836_1_)
    {
        lastHealth = p_178836_1_;
    }

    public int getDisplayHealth()
    {
        return displayHealth;
    }

    public void setDisplayHealth(int p_178857_1_)
    {
        displayHealth = p_178857_1_;
    }

    public long getLastHealthTime()
    {
        return lastHealthTime;
    }

    public void setLastHealthTime(long p_178846_1_)
    {
        lastHealthTime = p_178846_1_;
    }

    public long getHealthBlinkTime()
    {
        return healthBlinkTime;
    }

    public void setHealthBlinkTime(long p_178844_1_)
    {
        healthBlinkTime = p_178844_1_;
    }

    public long getRenderVisibilityId()
    {
        return renderVisibilityId;
    }

    public void setRenderVisibilityId(long p_178843_1_)
    {
        renderVisibilityId = p_178843_1_;
    }
}
