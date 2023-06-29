package org.spray.infinity.features.component.friends;

import java.util.ArrayList;
import java.util.List;

import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.file.FriendsFile;

import net.minecraft.util.Formatting;

public class Friend extends FriendsFile {

	private List<String> friendList = new ArrayList<String>();

	public boolean contains(String name) {
		if (friendList.contains(name)) {
			return true;
		}
		return false;
	}

	public void switchFriend(String name) {
		if (friendList.contains(name)) {
			remove(name);
		} else {
			friendList.add(name);
			Helper.message(Formatting.WHITE + name + Formatting.GRAY + " added to friend list");
		}
		save();
	}

	public void add(String name) {
		if (friendList.contains(name)) {
			error(name);
		} else {
			friendList.add(name);
			Helper.message(Formatting.WHITE + name + Formatting.GRAY + " added to friend list");
		}
		save();
	}

	public void remove(String name) {
		if (friendList.contains(name)) {
			friendList.remove(name);
			Helper.message(name + Formatting.GRAY + " removed from list");
		} else {
			Helper.message(name + Formatting.GRAY + " missing in the list");
		}
		save();
	}

	private void error(String name) {
		Helper.message(
				Formatting.GRAY + "Friend " + Formatting.BLUE + name + Formatting.GRAY + " is already on the list");
	}

	public void save() {
		saveFriends();
	}

	public void load() {
		loadFriends();
	}

	public List<String> getFriendList() {
		return friendList;
	}

	public void setFriendList(List<String> friendList) {
		this.friendList = friendList;
	}

}
