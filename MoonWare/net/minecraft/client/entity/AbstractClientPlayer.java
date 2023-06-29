package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.Namespaced;
import net.minecraft.util.StringUtils;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import optifine.Config;
import optifine.PlayerConfigurations;
import optifine.Reflector;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.misc.StreamerMode;

import javax.annotation.Nullable;
import java.io.File;

public abstract class AbstractClientPlayer extends EntityPlayer {
    private NetworkPlayerInfo playerInfo;
    public float rotateElytraX;
    public float rotateElytraY;
    public float rotateElytraZ;
    private Namespaced locationOfCape;
    private String nameClear;
    private static final Namespaced TEXTURE_ELYTRA = new Namespaced("textures/entity/elytra.png");

    public AbstractClientPlayer(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
        nameClear = playerProfile.getName();

        if (nameClear != null && !nameClear.isEmpty()) {
            nameClear = StringUtils.stripControlCodes(nameClear);
        }
        PlayerConfigurations.getPlayerConfiguration(this);
    }

    /**
     * Returns true if the player is in spectator mode.
     */
    public boolean isSpectator() {
        NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(getGameProfile().getId());
        return networkplayerinfo != null && networkplayerinfo.getGameType() == GameType.SPECTATOR;
    }

    public boolean isCreative() {
        NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(getGameProfile().getId());
        return networkplayerinfo != null && networkplayerinfo.getGameType() == GameType.CREATIVE;
    }

    /**
     * Checks if this instance of AbstractClientPlayer has any associated player data.
     */
    public boolean hasPlayerInfo() {
        return getPlayerInfo() != null;
    }

    @Nullable
    protected NetworkPlayerInfo getPlayerInfo() {
        if (playerInfo == null) {
            playerInfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(getUniqueID());
        }

        return playerInfo;
    }

    /**
     * Returns true if the player has an associated skin.
     */
    public boolean hasSkin() {
        NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
        return networkplayerinfo != null && networkplayerinfo.hasLocationSkin();
    }

    /**
     * Returns true if the player instance has an associated skin.
     */
    public Namespaced getLocationSkin() {
        NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
        if (networkplayerinfo == null || MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.skinSpoof.getBoolValue())
            return DefaultPlayerSkin.getDefaultSkin(getUniqueID());
        return networkplayerinfo.getLocationSkin();
    }

    @Nullable
    public Namespaced getLocationCape() {
        if (!Config.isShowCapes()) {
            return null;
        } else if (locationOfCape != null) {
            return locationOfCape;
        } else {
            NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
            return networkplayerinfo == null ? null : networkplayerinfo.getLocationCape();
        }
    }

    public boolean isPlayerInfoSet() {
        return getPlayerInfo() != null;
    }

    @Nullable

    /**
     * Gets the special Elytra texture for the player.
     */
    public Namespaced getLocationElytra() {
        NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
        return networkplayerinfo == null ? null : networkplayerinfo.getLocationElytra();
    }

    public static ThreadDownloadImageData getDownloadImageSkin(Namespaced namespacedIn, String username) {
        TextureManager texturemanager = Minecraft.getTextureManager();
        ITextureObject itextureobject = texturemanager.getTexture(namespacedIn);

        if (itextureobject == null) {
            itextureobject = new ThreadDownloadImageData(null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(EntityPlayer.getOfflineUUID(username)), new ImageBufferDownload());
            texturemanager.loadTexture(namespacedIn, itextureobject);
        }

        return (ThreadDownloadImageData) itextureobject;
    }

    /**
     * Returns true if the username has an associated skin.
     */
    public static Namespaced getLocationSkin(String username) {
        return new Namespaced("skins/" + StringUtils.stripControlCodes(username));
    }

    public String getSkinType() {
        NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
        return networkplayerinfo == null ? DefaultPlayerSkin.getSkinType(getUniqueID()) : networkplayerinfo.getSkinType();
    }

    public float getFovModifier() {
        float f = 1.0F;

        if (capabilities.isFlying) {
            f *= 1.1F;
        }

        IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        f = (float) ((double) f * ((iattributeinstance.getAttributeValue() / (double) capabilities.getWalkSpeed() + 1.0D) / 2.0D));

        if (capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
            f = 1.0F;
        }

        if (isHandActive() && getActiveItemStack().getItem() == Items.BOW) {
            int i = getItemInUseMaxCount();
            float f1 = (float) i / 20.0F;

            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 = f1 * f1;
            }

            f *= 1.0F - f1 * 0.15F;
        }

        return Reflector.ForgeHooksClient_getOffsetFOV.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getOffsetFOV, this, f) : f;
    }

    public String getNameClear() {
        return nameClear;
    }

    public Namespaced getLocationOfCape() {
        return locationOfCape;
    }

    public void setLocationOfCape(Namespaced p_setLocationOfCape_1_) {
        locationOfCape = p_setLocationOfCape_1_;
    }

    public boolean hasElytraCape() {
        Namespaced resourcelocation = getLocationCape();

        if (resourcelocation == null) {
            return false;
        } else {
            return resourcelocation != locationOfCape;
        }
    }
}
