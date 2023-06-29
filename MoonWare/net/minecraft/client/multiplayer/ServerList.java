package net.minecraft.client.multiplayer;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class ServerList {
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * The Minecraft instance.
     */
    private final Minecraft mc;
    private final List<ServerData> servers = Lists.newArrayList();

    public ServerList(Minecraft mcIn) {
        mc = mcIn;
        loadServerList();
    }

    /**
     * Loads a list of servers from servers.dat, by running ServerData.getServerDataFromNBTCompound on each NBT compound
     * found in the "servers" tag list.
     */
    public void loadServerList() {
        try {
            servers.clear();
            NBTTagCompound nbttagcompound = CompressedStreamTools.read(new File(Minecraft.gameDir, "servers.dat"));

            if (nbttagcompound == null) {
                return;
            }

            NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                servers.add(ServerData.readNBT(nbttaglist.getCompoundTagAt(i)));
            }
        } catch (Exception exception) {
            LOGGER.error("Couldn't load server list", exception);
        }
    }

    /**
     * Runs getNBTCompound on each ServerData instance, puts everything into a "servers" NBT list and writes it to
     * servers.dat.
     */
    public void saveServerList() {
        try {
            NBTTagList nbttaglist = new NBTTagList();

            for (ServerData serverdata : servers) {
                nbttaglist.appendTag(serverdata.writeNBT());
            }

            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("servers", nbttaglist);
            CompressedStreamTools.safeWrite(nbttagcompound, new File(Minecraft.gameDir, "servers.dat"));
        } catch (Exception exception) {
            LOGGER.error("Couldn't save server list", exception);
        }
    }

    /**
     * Gets the ServerData instance stored for the given index in the list.
     */
    public ServerData getServerData(int index) {
        return servers.get(index);
    }

    /**
     * Removes the ServerData instance stored for the given index in the list.
     */
    public void removeServerData(int index) {
        servers.remove(index);
    }

    /**
     * Adds the given ServerData instance to the list.
     */
    public void addServerData(ServerData server) {
        servers.add(server);
    }

    /**
     * Counts the number of ServerData instances in the list.
     */
    public int getSize() {
        return servers.size();
    }

    /**
     * Takes two list indexes, and swaps their order around.
     */
    public void swapServers(int pos1, int pos2) {
        ServerData serverdata = getServerData(pos1);
        servers.set(pos1, getServerData(pos2));
        servers.set(pos2, serverdata);
        saveServerList();
    }

    public void set(int index, ServerData server) {
        servers.set(index, server);
    }

    public static void saveSingleServer(ServerData server) {
        ServerList serverlist = new ServerList(Minecraft.getMinecraft());
        serverlist.loadServerList();

        for (int i = 0; i < serverlist.getSize(); ++i) {
            ServerData serverdata = serverlist.getServerData(i);

            if (serverdata.name.equals(server.name) && serverdata.ip.equals(server.ip)) {
                serverlist.set(i, server);
                break;
            }
        }

        serverlist.saveServerList();
    }
}
