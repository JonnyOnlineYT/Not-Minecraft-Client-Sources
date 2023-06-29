package net.minecraft.client.multiplayer;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;

public class ServerData {
    public String name;
    public String ip;
    @Getter @Setter private ServerData.ServerResourceMode resourceMode = ServerData.ServerResourceMode.PROMPT;
    // Ping Status Data
    public String motd;
    public String onlineMax;
    public String players;
    public String version = "1.12.2";
    public int protocol = 340;
    @Getter @Setter private String favicon;
    // Ping Time Data
    public long pingToServer;
    public boolean pinged;

    public ServerData(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    /**
     * Returns an NBTTagCompound with the server's name, IP and maybe acceptTextures.
     */
    public NBTTagCompound writeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("name", name);
        nbt.setString("ip", ip);
        if (favicon != null) nbt.setString("icon", favicon);
        if (resourceMode == ServerData.ServerResourceMode.ENABLED) {
            nbt.setBoolean("acceptTextures", true);
        } else if (resourceMode == ServerData.ServerResourceMode.DISABLED) {
            nbt.setBoolean("acceptTextures", false);
        }
        return nbt;
    }

    public static ServerData readNBT(NBTTagCompound nbt) {
        ServerData data = new ServerData(nbt.getString("name"), nbt.getString("ip"));
        if (nbt.hasKey("icon", 8)) data.setFavicon(nbt.getString("icon"));
        if (nbt.hasKey("acceptTextures", 1)) {
            if (nbt.getBoolean("acceptTextures")) {
                data.setResourceMode(ServerData.ServerResourceMode.ENABLED);
            } else {
                data.setResourceMode(ServerData.ServerResourceMode.DISABLED);
            }
        }
        return data;
    }

    public void copyFrom(ServerData other) {
        ip = other.ip;
        name = other.name;
        setResourceMode(other.getResourceMode());
        favicon = other.favicon;
    }

    public enum ServerResourceMode {
        ENABLED("enabled"),
        DISABLED("disabled"),
        PROMPT("prompt");
        @Getter private final Component name;

        ServerResourceMode(String name) {
            this.name = new TranslatableComponent("addServer.resourcePack." + name);
        }
    }
}
