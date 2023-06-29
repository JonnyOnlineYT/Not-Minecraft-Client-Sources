package org.moonware.client.qol.via.platform;

import com.viaversion.viabackwards.api.ViaBackwardsPlatform;
import org.moonware.client.qol.via.MWVia;

import java.io.File;
import java.util.logging.Logger;

public class MWViaBackwardsPlatform implements ViaBackwardsPlatform {
    private final Logger logger = Logger.getLogger("ViaBackwards");

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public void disable() {}

    @Override
    public File getDataFolder() {
        return MWVia.VIA_BACKWARDS_FOLDER;
    }
}
