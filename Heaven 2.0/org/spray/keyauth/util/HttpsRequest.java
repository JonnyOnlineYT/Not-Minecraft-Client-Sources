package org.spray.keyauth.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsRequest {

	private final static String USER_AGENT = "Mozilla/5.0";
	// File to save response to
	private final static String RESPONSE_FILE_LOCATION = "D:\\file.html";

	static {
		// this part is needed cause Lebocoin has invalid SSL certificate, that cannot
		// be normally processed by Java
		TrustManager[] trustAllCertificates = new TrustManager[] { new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null; // Not relevant.
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
				// Do nothing. Just allow them all.
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
				// Do nothing. Just allow them all.
			}
		} };

		HostnameVerifier trustAllHostnames = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true; // Just allow them all.
			}
		};

		try {
			System.setProperty("jsse.enableSNIExtension", "false");
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCertificates, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames);
		} catch (GeneralSecurityException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * Make post request for given URL with given parameters and save response into
	 * RESPONSE_FILE_LOCATION
	 *
	 * @param url        HTTPS link to send POST request
	 * @param parameters POST request parameters. currently expecting following
	 *                   parameters: name, email, phone, body, send
	 */
	public static void post(String url, Map<String, String> parameters) {
		try {
			ensureAllParametersArePresent(parameters);
			String initialCookies = getUrlConnection(url, "").getHeaderField("Set-Cookie");
			HttpsURLConnection con = getUrlConnection(url, initialCookies);
			String urlParameters = processRequestParameters(parameters);

			sendPostParameters(con, urlParameters);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			System.out.println(con.getResponseMessage());
		} catch (Exception e) {
			throw new RuntimeException(e);
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

	private static void ensureAllParametersArePresent(Map<String, String> parameters) {
		if (parameters.get("send") == null) {
			parameters.put("send", "Envoyer votre message");
		}
		if (parameters.get("phone") == null) {
			parameters.put("phone", "");
		}
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
			throw new RuntimeException(e);
		}
	}
}
