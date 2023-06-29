package org.moonware.client.qol.via.platform;

import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.libs.gson.JsonObject;

public class MWViaInjector implements ViaInjector {
    @Override
    public void inject() {}
    @Override
    public void uninject() {}

    @Override
    public int getServerProtocolVersion() {
        return 340;
    }

    @Override
    public JsonObject getDump() {
        return new JsonObject();
    }
}
