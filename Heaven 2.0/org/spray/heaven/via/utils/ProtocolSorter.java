package org.spray.heaven.via.utils;

import us.myles.ViaVersion.api.protocol.ProtocolVersion;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ProtocolSorter {

    private static final List<ProtocolVersion> protocolVersions = new ArrayList<>();

    private static int count = 0;

    static {
        for (Field f : ProtocolVersion.class.getDeclaredFields()) {
            if (f.getType().equals(ProtocolVersion.class)) {
                count++;
                try {
                    ProtocolVersion ver = (ProtocolVersion) f.get(null);
                    if (count >= 8 && !ver.getName().equals("UNKNOWN"))
                        getProtocolVersions().add(ver);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static List<ProtocolVersion> getProtocolVersions() {
        return protocolVersions;
    }
}
