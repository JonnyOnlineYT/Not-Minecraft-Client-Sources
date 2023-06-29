package org.spray.infinity.ui.account.main;

import java.util.ArrayList;

import org.spray.infinity.utils.file.AccountsFile;

public class AccountManager extends AccountsFile {
                  
	public static Account lastAlt;
	public static ArrayList<Account> registry;

	public ArrayList<Account> getRegistry() {
		return registry;
	}
	
	public static void add(Account account) {
		if (!registry.contains(account)) {
			registry.add(account);
		}
	}

	public void setLastAlt(final Account alt) {
		lastAlt = alt;
	}
	
	public void load() {
		loadAccounts();
	}
	
	public void save() {
		saveAccounts();
	}

	static {
		registry = new ArrayList<Account>();
	}
}
