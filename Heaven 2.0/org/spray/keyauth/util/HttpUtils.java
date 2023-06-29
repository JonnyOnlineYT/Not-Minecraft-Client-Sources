package org.spray.keyauth.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;

import by.radioegor146.nativeobfuscator.Native;

@Native
public class HttpUtils {

	private final static String USER_AGENT = "Mozilla/5.0";

	public static String post(String url, Map<String, String> parameters) {
		try {
			String initialCookies = getUrlConnection(url, "").getHeaderField("Set-Cookie");
			HttpsURLConnection con = getUrlConnection(url, initialCookies);
			String urlParameters = processRequestParameters(parameters);

			sendPostParameters(con, urlParameters);
			return IOUtils.toString(con.getInputStream(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			return null;
		}
	}

	private static void sendPostParameters(URLConnection con, String urlParameters) throws IOException {
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
	}

	private static HttpsURLConnection getUrlConnection(String url, String cookies) throws IOException {
		HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Cookie", cookies);
		return con;
	}

	private static String processRequestParameters(Map<String, String> parameters) {
		StringBuilder sb = new StringBuilder();
		for (String parameterName : parameters.keySet()) {
			sb.append(parameterName).append('=').append(urlEncode(parameters.get(parameterName))).append('&');
		}
		return sb.substring(0, sb.length() - 1);
	}

	private static String urlEncode(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
}
