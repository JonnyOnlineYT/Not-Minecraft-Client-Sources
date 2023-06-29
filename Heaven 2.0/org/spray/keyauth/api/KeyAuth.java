package org.spray.keyauth.api;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.spray.heaven.protect.Provider;
import org.spray.keyauth.user.UserData;
import org.spray.keyauth.util.Encryption;
import org.spray.keyauth.util.HWID;
import org.spray.keyauth.util.HttpUtils;
import org.spray.keyauth.util.ShutDown;

import by.radioegor146.nativeobfuscator.Native;

@Native
public class KeyAuth {

	private String secret;

	public final String appname;
	public final String ownerid;
	public final String version;
	public final String url;

	protected String sessionid;
	protected boolean initialized;

	protected String status;
	protected Status statusType;

	protected UserData userData;

	private boolean debug = false;

	private String enckey, iv_key;

	private String hash = "";

	public KeyAuth(String appname, String secret, String ownerid, String version, String url) {
		if (appname == null || secret == null || ownerid == null || version == null || url == null)
			throw new NullPointerException();

		this.appname = appname;
		this.secret = secret;
		this.ownerid = ownerid;
		this.version = version;
		this.url = url;
		statusType = Status.INFO;
		status = "";
	}

	public UserData getUserData() {
		return userData;
	}

	public Status getStatusType() {
		return statusType;
	}

	public String getStatus() {
		return status;
	}

	public String getSessionId() {
		return sessionid;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setStatus(String status, Status type) {
		this.status = status;
		this.statusType = type;
	}

	public void init() {
		try {
			enckey = Encryption.sha256(Encryption.generateIv());
			String init_iv = Encryption.sha256(Encryption.generateIv());

			Map<String, String> arguments = new HashMap<>();
			arguments.put("type", "init");
			arguments.put("ver",  version);
			arguments.put("name", appname);
			arguments.put("ownerid", ownerid);
			arguments.put("init_iv", init_iv);

			String responseBody = HttpUtils.post(url, arguments);

			try {
				JSONObject responseJSON = new JSONObject(responseBody);

				if (responseBody.equalsIgnoreCase("KeyAuth_Invalid")) {
					// �alling the method with a disabled connection
					// System.exit(0);
				}

				if (responseJSON.getBoolean("success")) {
					sessionid = responseJSON.getString("sessionid");
					initialized = true;

				} else if (responseJSON.getString("message").equalsIgnoreCase("invalidver")) {

				} else {

					ShutDown.run();
				}

			} catch (Exception e) {
				ShutDown.run();
			}
		} catch (Exception e) {
			ShutDown.run();
		}
	}

	public UserData license(String key) {
		if (!initialized) {
			setStatus("Key Auth API initialization error, please re-enter the client", Status.ERROR);
			ShutDown.run();
			return null;
		}

		try {
			String hwid = HWID.getHWID();

			Map<String, String> arguments = new HashMap<>();
			arguments.put("type", "license");
			arguments.put("key", key);
			arguments.put("hwid", hwid);
			arguments.put("sessionid", sessionid);
			arguments.put("name", appname);
			arguments.put("ownerid", ownerid);

			String responseBody = HttpUtils.post(url, arguments);

			try {
				JSONObject responseJSON = new JSONObject(responseBody);

				if (responseJSON.getBoolean("success")) {

					setStatus("Successfully logged in", Status.SUCCESS);
					userData = new UserData(responseJSON, key, "SUCCESS");

				} else {
					try {
						setStatus(responseJSON.getString("message"), Status.INFO);
					} catch (Exception e) {
						setStatus("License does not exist", Status.INFO);
					}
				}

				Provider.getChecker().setResult(() -> String.valueOf(responseJSON.getBoolean("success")));
			} catch (Exception e) {
				setStatus("JSONObject is empty", Status.ERROR);
			}
		} catch (Exception e) {
			setStatus("Connection Error", Status.ERROR);
		}
		return userData;
	}

	public void setHash(String strHash) {
		this.hash = strHash;
	}

	public enum Status {
		ERROR(0xFFED3D2F), SUCCESS(0xFF46ED2F), INFO(0xFFE2E3E2);

		private int color;

		Status(int color) {
			this.color = color;
		}

		public int getColor() {
			return color;
		}
	}
}
