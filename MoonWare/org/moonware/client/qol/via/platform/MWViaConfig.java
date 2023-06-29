package org.moonware.client.qol.via.platform;

import com.google.common.collect.ImmutableList;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.configuration.AbstractViaConfig;
import net.minecraft.client.Minecraft;
import org.moonware.client.qol.via.MWVia;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MWViaConfig extends AbstractViaConfig {
    private static final List<String> UNSUPPORTED = ImmutableList.of("anti-xray-patch", "bungee-ping-interval", "bungee-ping-save", "bungee-servers", "quick-move-action-fix", "nms-player-ticking", "velocity-ping-interval", "velocity-ping-save", "velocity-servers", "blockconnection-method", "change-1_9-hitbox", "change-1_14-hitbox");
    public MWViaConfig() {
        super(new File(MWVia.VIA_FOLDER, "config.yml"));
        reloadConfig();
    }

    @Override
    protected void handleConfig(Map<String, Object> config) {}

    @Override
    public List<String> getUnsupportedOptions() {
        return UNSUPPORTED;
    }

    @Override
    public boolean isAntiXRay() {
        return false;
    }

    @Override
    public boolean isNMSPlayerTicking() {
        return false;
    }

    @Override
    public boolean is1_12QuickMoveActionFix() {
        return false;
    }

    @Override
    public String getBlockConnectionMethod() {
        return "packet";
    }

    @Override
    public boolean is1_9HitboxFix() {
        return false;
    }

    @Override
    public boolean is1_14HitboxFix() {
        return false;
    }
}
