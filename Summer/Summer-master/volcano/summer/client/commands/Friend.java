package volcano.summer.client.commands;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;
import volcano.summer.base.manager.friend.FriendManager;
import volcano.summer.client.events.EventChatSent;

public class Friend extends Command {

	private boolean realnameBoolean;

	public Friend() {
		super("Friend", "<add | remove | realname | clear>");
	}

	@Override
	public void run(String message) {
		try {
			final String cmd = EventChatSent.getMessage().split(" ")[1];
			if (cmd.equalsIgnoreCase("add")) {
				try {
					final String name = EventChatSent.getMessage().split(" ")[2];
					String alias = EventChatSent.getMessage().split(" ")[3];
					if (alias == null) {
						alias = name;
					}
					if (cmd.equalsIgnoreCase("add") && !name.equalsIgnoreCase(" ") && !alias.equalsIgnoreCase(" ")) {
						if (!FriendManager.isFriend(name)) {
							FriendManager.addFriend(name, alias);
							Summer.tellPlayer(
									"You have added §e" + name.replaceFirst(name.substring(1), "§e" + name.substring(1))
											+ " §7to your friend list as §e" + alias);
						} else {
							Summer.tellPlayer("That player is already a friend or isn't online!");
						}
					}
				} catch (Exception e) {
					Summer.tellPlayer("Incorrect Syntax! friend add <name> <alias>");
				}
			} else if (cmd.equalsIgnoreCase("remove")) {
				try {
					final String name = EventChatSent.getMessage().split(" ")[2];
					if (cmd.equalsIgnoreCase("remove")) {
						if (FriendManager.isFriend(name)) {
							FriendManager.deleteFriend(name);
							Summer.tellPlayer("You have removed §e"
									+ name.replaceFirst(name.substring(1), "§e" + name.substring(1))
									+ "§7 from your friends list.");
						} else {
							Summer.tellPlayer(String.valueOf(String.valueOf(name)) + " is not a friend!");
						}
					}
				} catch (Exception e) {
					Summer.tellPlayer("Incorrect Syntax! friend remove <name>");
				}
			} else if (!cmd.equalsIgnoreCase("realname") && !cmd.equalsIgnoreCase("name")) {
				if (cmd.equalsIgnoreCase("clear")) {
					try {
						Summer.tellPlayer("Cleared Friends!");
						FriendManager.friends.clear();
					} catch (Exception ex) {
					}
				} else if (!cmd.equalsIgnoreCase("clear") || !cmd.equalsIgnoreCase("add")
						|| !cmd.equalsIgnoreCase("remove")) {
					Summer.tellPlayer("Incorrect Syntax! Options: " + this.getArguments());
				}
			} else {
				try {
					final String alias2 = EventChatSent.getMessage().split(" ")[2];
					String name2 = "";
					for (final volcano.summer.base.manager.friend.Friend theFriend : FriendManager.friends) {
						if (theFriend.getAlias().equalsIgnoreCase(alias2)) {
							this.realnameBoolean = true;
							name2 = theFriend.getName();
							Summer.tellPlayer("§e" + alias2 + "§e's§7 " + "realname is: §e"
									+ name2.replaceFirst(name2.substring(1), "§e" + name2.substring(1)));
						} else {
							if (this.realnameBoolean) {
								continue;
							}
							Summer.tellPlayer(String.valueOf(String.valueOf(alias2)) + " is not a valid alias!");
						}
					}
				} catch (Exception e) {
					Summer.tellPlayer("Incorrect Syntax! friend realname <alias>");
				}
			}
		} catch (Exception e2) {
			Summer.tellPlayer("Incorrect Syntax! Options: " + this.getArguments());
		}
		Summer.fileManager.saveFiles();
	}
}