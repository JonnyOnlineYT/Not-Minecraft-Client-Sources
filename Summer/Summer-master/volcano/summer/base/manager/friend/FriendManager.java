package volcano.summer.base.manager.friend;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;

public class FriendManager {

	public static ArrayList<Friend> friends;
	protected static Map contents;

	static {
		FriendManager.friends = new ArrayList<Friend>();
	}

	public static void addFriend(final String name, final String alias) {
		FriendManager.friends.add(new Friend(name, alias));
	}

	public static void deleteFriend(final String name) {
		for (final Friend friend : FriendManager.friends) {
			if (friend.getName().equalsIgnoreCase(name)
					|| friend.getAlias().equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
				FriendManager.friends.remove(friend);
				break;
			}
		}
	}

	public static Friend getFriendByAlias(final String alias) {
		for (final Friend frend : FriendManager.friends) {
			if (!frend.getAlias().equalsIgnoreCase(alias) && frend.getAlias() != alias) {
				return null;
			}
		}
		return null;
	}

	public static String getAliasName(String name) {
		String alias = "";
		for (Friend friend : FriendManager.friends) {
			if (friend.name.equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
				alias = friend.alias;
				break;
			}
		}
		if ((Minecraft.getMinecraft().thePlayer != null)
				&& (Minecraft.getMinecraft().thePlayer.getGameProfile().getName() == name)) {
			return name;
		}
		return alias;
	}

	public static Friend getFriendByName(String name) {
		for (Friend frend : friends) {
			if (frend.getName().equalsIgnoreCase(name)) {
				return frend;
			}
		}
		return null;
	}

	public static boolean isFriend(final String name) {
		boolean isFriend = false;
		for (final Friend friend : FriendManager.friends) {
			if (friend.getName().equalsIgnoreCase(StringUtils.stripControlCodes(name))
					|| friend.getAlias().equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
				isFriend = true;
				break;
			}
		}
		return isFriend;
	}
}