/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.helpers.hwid;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Base64;
import java.util.Enumeration;
import ru.fluger.client.helpers.encrypt.SHAUtil;

public class HwidUtil {
    public String getHwid() {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(System.getProperty("os.name"));
            builder.append(new String(Base64.getEncoder().encode(this.getLocalNetWork().getHardwareAddress())));
            return SHAUtil.SHA1(builder.toString());
        }
        catch (Exception e) {
            return null;
        }
    }

    private NetworkInterface getLocalNetWork() throws Exception {
        NetworkInterface ntwinterface = null;
        Enumeration<NetworkInterface> NETWORK = NetworkInterface.getNetworkInterfaces();
        while (NETWORK.hasMoreElements()) {
            NetworkInterface element = NETWORK.nextElement();
            Enumeration<InetAddress> addresses = element.getInetAddresses();
            while (addresses.hasMoreElements() && element.getHardwareAddress() != null && !this.isVMMac(element.getHardwareAddress())) {
                InetAddress ip = addresses.nextElement();
                if (!(ip instanceof Inet4Address) || !ip.isSiteLocalAddress()) continue;
                ntwinterface = NetworkInterface.getByInetAddress(ip);
            }
        }
        return ntwinterface;
    }

    private boolean isVMMac(byte[] mac) {
        byte[][] invalidMacs;
        if (null == mac) {
            return false;
        }
        for (byte[] invalid : invalidMacs = new byte[][]{{0, 5, 105}, {0, 28, 20}, {0, 12, 41}, {0, 80, 86}, {8, 0, 39}, {10, 0, 39}, {0, 3, -1}, {0, 21, 93}}) {
            if (invalid[0] != mac[0] || invalid[1] != mac[1] || invalid[2] != mac[2]) continue;
            return true;
        }
        return false;
    }
}

