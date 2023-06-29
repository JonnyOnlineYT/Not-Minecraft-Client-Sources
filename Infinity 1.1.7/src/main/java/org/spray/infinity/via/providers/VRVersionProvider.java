package org.spray.infinity.via.providers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.spray.infinity.via.ViaFabric;
import org.spray.infinity.via.util.ProtocolUtils;

import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.exception.CancelException;
import com.viaversion.viaversion.protocols.base.BaseProtocol1_16;
import com.viaversion.viaversion.protocols.base.BaseProtocol1_7;
import com.viaversion.viaversion.protocols.base.BaseVersionProvider;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.ClientConnection;

public class VRVersionProvider extends BaseVersionProvider {
	private int[] multiconnectSupportedVersions = null;

	{
		try {
			if (FabricLoader.getInstance().isModLoaded("multiconnect")) {
				Class<?> mcApiClass = Class.forName("net.earthcomputer.multiconnect.api.MultiConnectAPI");
				Class<?> iProtocolClass = Class.forName("net.earthcomputer.multiconnect.api.IProtocol");
				Object mcApiInstance = mcApiClass.getMethod("instance").invoke(null);
				List<?> protocols = (List<?>) mcApiClass.getMethod("getSupportedProtocols").invoke(mcApiInstance);
				Method getValue = iProtocolClass.getMethod("getValue");
				Method isMulticonnectBeta;
				try {
					isMulticonnectBeta = iProtocolClass.getMethod("isMulticonnectBeta");
				} catch (NoSuchMethodException e) {
					isMulticonnectBeta = null;
				}
				Set<Integer> vers = new TreeSet<>();
				for (Object protocol : protocols) {
					// Do not use versions with beta multiconnect support, which may have stability
					// issues
					if (isMulticonnectBeta == null || !(Boolean) isMulticonnectBeta.invoke(protocol)) {
						vers.add((Integer) getValue.invoke(protocol));
					}
				}
				multiconnectSupportedVersions = vers.stream().mapToInt(Integer::intValue).toArray();
				ViaFabric.JLOGGER.info("ViaFabric will integrate with multiconnect");
			}
		} catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException
				| ClassCastException ignored) {
		}
	}

	@Override
	public int getClosestServerProtocol(UserConnection connection) throws Exception {
		if (connection.isClientSide()) {
			return ViaFabric.INSTANCE.getVersion();
		}
		return super.getClosestServerProtocol(connection);
	}

	private void handleMulticonnectPing(UserConnection connection, ProtocolInfo info, boolean blocked, int serverVer)
			throws Exception {
		if (info.getState() == State.STATUS && info.getProtocolVersion() == -1
				&& connection.getChannel().pipeline().get(ClientConnection.class).getPacketListener().getClass()
						.getName().startsWith("net.earthcomputer.multiconnect")
				&& (blocked || ProtocolUtils.isSupported(serverVer, getVersionForMulticonnect(serverVer)))) { // Intercept
																												// the
																												// connection
			int multiconnectSuggestion = blocked ? -1 : getVersionForMulticonnect(serverVer);
			ViaFabric.JLOGGER.info("Sending " + ProtocolVersion.getProtocol(multiconnectSuggestion)
					+ " for multiconnect version detector");
			PacketWrapper newAnswer = PacketWrapper.create(0x00, null, connection);
			newAnswer.write(Type.STRING,
					"{\"version\":{\"name\":\"viafabric integration\",\"protocol\":" + multiconnectSuggestion + "}}");
			newAnswer.send(info.getPipeline().contains(BaseProtocol1_16.class) ? BaseProtocol1_16.class
					: BaseProtocol1_7.class);
			throw CancelException.generate();
		}
	}

	private int getVersionForMulticonnect(int clientSideVersion) {
		// https://github.com/ViaVersion/ViaVersion/blob/master/velocity/src/main/java/us/myles/ViaVersion/velocity/providers/VelocityVersionProvider.java
		int[] compatibleProtocols = multiconnectSupportedVersions;

		if (Arrays.binarySearch(compatibleProtocols, clientSideVersion) >= 0) {
			return clientSideVersion;
		}

		if (clientSideVersion < compatibleProtocols[0]) {
			return compatibleProtocols[0];
		}

		// TODO: This needs a better fix, i.e checking ProtocolRegistry to see if it
		// would work.
		for (int i = compatibleProtocols.length - 1; i >= 0; i--) {
			int protocol = compatibleProtocols[i];
			if (clientSideVersion > protocol && ProtocolVersion.isRegistered(protocol)) {
				return protocol;
			}
		}

		ViaFabric.JLOGGER.severe("multiconnect integration: Panic, no protocol id found for " + clientSideVersion);
		return clientSideVersion;
	}

}
