package org.spray.keyauth.user;

import org.json.JSONArray;
import org.json.JSONObject;

import by.radioegor146.nativeobfuscator.Native;

@Native
public class UserData {

	private String username;
	private String subscription;
	private String expiry;
	private String key;
	private UserStatus status;

	public UserData(JSONObject json, String key, String status) {
		JSONObject info = json.getJSONObject("info");

		JSONArray subArray = info.getJSONArray("subscriptions");
		JSONObject subObject = subArray.getJSONObject(0);
		
		this.username = info.getString("username");
		this.subscription = subObject.getString("subscription");
		this.expiry = subObject.getString("expiry");
		this.key = key;
		this.status = UserStatus.valueOf(status);
	}

	public String getUsername() {
		return username;
	}

	public String getSubscription() {
		return subscription;
	}

	public String getExpiry() {
		return expiry;
	}

	public String getKey() {
		return key;
	}
	
	public UserStatus getStatus() {
		return status;
	}
	
	public enum UserStatus {
		SUCCESS, ERROR;
	}

}
