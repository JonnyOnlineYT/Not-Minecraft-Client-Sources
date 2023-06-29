package ru.wendoxd.ui.altmanager;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import ru.wendoxd.ui.altmanager.altManager.Alt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AltManager {
	private final List<Alt> accountList = new ArrayList<>();
	private final File accountFile = new File(System.getenv("appdata") + "\\.antiautistleak\\game\\accounts.wex");

	public void init() throws IOException {
		if (!accountFile.exists()) {
			accountFile.createNewFile();
		} else {
			readAlts();
		}

	}

	public List<Alt> getAccounts() {
		return accountList;
	}

	public Session login(String name, String password) {
		YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
		UserAuthentication authentication = new YggdrasilUserAuthentication(service, Agent.MINECRAFT);
		YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) authentication;
		auth.setUsername(name);
		auth.setPassword(password);

		try {
			auth.logIn();
			return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(),
					auth.getAuthenticatedToken(), "mojang");
		} catch (AuthenticationException var8) {
			Minecraft.LOGGER.error(var8.toString());
			return null;
		}
	}

	public void addAccount(Alt alt) {
		accountList.add(alt);
		syncAccounts(this.getAccounts());
	}

	private void syncAccounts(List<Alt> lines) {
		StringBuilder sb = new StringBuilder();
		lines.forEach(alt -> sb.append(alt.getEmail()).append(":").append(alt.getPassword()).append(":")
				.append(alt.getName()).append("\n"));
		try {
			Files.write(accountFile.toPath(), crypt(sb.toString()).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeAccount(Alt alt) {
		accountList.remove(alt);
		syncAccounts(accountList);
	}

	public void clearAccounts() {
		accountList.clear();
		syncAccounts(getAccounts());
	}

	private String crypt(String input) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		for (char chars : input.toCharArray()) {
			baos.write(chars ^ 40);
		}

		return baos.toString();
	}

	private void readAlts() {
		try {
			Arrays.asList(crypt(new String(Files.readAllBytes(accountFile.toPath()))).split("\n")).forEach(line -> {
				try {
					if (!line.isEmpty() && line.split(":").length > 2) {
						accountList.add(new Alt(line.split(":")[0], line.split(":")[1], line.split(":")[2]));
					}
				} catch (Exception ignored) {

				}
			});
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
	}
}
