package volcano.summer.base.manager.file.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import volcano.summer.base.manager.file.FileManager;
import volcano.summer.base.manager.friend.Friend;
import volcano.summer.base.manager.friend.FriendManager;

public class Friends extends FileManager.CustomFile {

	public Friends(String name, boolean Module, boolean loadOnStart) {
		super(name, Module, loadOnStart);
	}

	public void loadFile() throws IOException {
		BufferedReader variable9 = new BufferedReader(new FileReader(getFile()));
		String line;
		while ((line = variable9.readLine()) != null) {
			int i = line.indexOf(":");
			if (i >= 0) {
				String name = line.substring(0, i).trim();
				String alias = line.substring(i + 1).trim();
				FriendManager.addFriend(name, alias);
			}
		}
		variable9.close();
	}

	public void saveFile() throws IOException {
		PrintWriter variable9 = new PrintWriter(new FileWriter(getFile()));
		for (Friend frend : FriendManager.friends) {
			variable9.println(frend.getName() + ":" + frend.getAlias());
		}
		variable9.close();
	}
}

