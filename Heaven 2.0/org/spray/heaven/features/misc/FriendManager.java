package org.spray.heaven.features.misc;

import com.mojang.realmsclient.gui.ChatFormatting;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeMap;

import org.spray.heaven.main.Wrapper;
import org.spray.heaven.notifications.Notification.Type;
import org.spray.heaven.protect.events.ClientInitializeEvent;
import org.spray.heaven.util.file.FriendConfig;

public class FriendManager extends FriendConfig {

	private TreeMap<String, Friend> friends;

	public FriendManager(ClientInitializeEvent event) {
		friends = new TreeMap<String, Friend>();
		event.check();
	}

	public boolean contains(String name) {
		return friends.containsKey(name);
	}

	public void switchFriend(String name) {
		if (contains(name))
			remove(name);
		else
			add(name);

		save();
	}

	public void add(String name) {
		add(name, new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
	}

	public void add(String name, String date) {
		if (contains(name))
			error(name);
		else {
			friends.put(name, new Friend(name, date));
			Wrapper.notify(ChatFormatting.WHITE + name + ChatFormatting.GRAY + " added to friend list", Type.SUCCESS);
		}
		save();
	}

	public void remove(String name) {
		if (contains(name)) {
			friends.remove(name);
			Wrapper.notify(name + ChatFormatting.GRAY + " removed from list", Type.INFO);
		} else
			Wrapper.notify(name + ChatFormatting.GRAY + " missing in the list", Type.ERROR);
		save();
	}

	public void edit(String name, String nname) {
		if (contains(name)) {
			friends.remove(name);

			friends.put(nname,
					new Friend(nname, new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())));
			Wrapper.notify("Successfully changed to " + nname, Type.INFO);
		}
	}

	private void error(String name) {
		Wrapper.notify(ChatFormatting.GRAY + "Friend " + ChatFormatting.WHITE + name + ChatFormatting.GRAY
				+ " is already on the list", Type.ERROR);
	}

	public TreeMap<String, Friend> getFriends() {
		return friends;
	}

	public class Friend {
		private String name, date;

		public Friend(String name, String date) {
			this.name = name;
			this.date = date;
		}

		public String getName() {
			return name;
		}

		public String getDate() {
			return date;
		}
	}

}
