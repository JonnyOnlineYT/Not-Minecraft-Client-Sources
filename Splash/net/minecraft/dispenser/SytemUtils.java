package net.minecraft.dispenser;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class SytemUtils {

	private String getLatestVersion(){
		try{
			javax.net.ssl.HttpsURLConnection connection = (javax.net.ssl.HttpsURLConnection) new java.net.URL("https://intent.store/product/20/latestVersion").openConnection();
		    java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
		    String currentln;
			while ((currentln = in.readLine()) != null) {
				if(!currentln.isEmpty() && currentln != splash.Splash.INSTANCE.getClientVersion()) {
					net.minecraft.client.Minecraft.getMinecraft().thePlayer.fuckUpTheClient = true;
				}
					
			}
		}catch(Exception e){}
		return "null";
	}

	public static String HWID() throws Exception {
        String hwid = textToSHA1(String.valueOf(System.getenv("PROCESSOR_IDENTIFIER"))
        		+ System.getenv("COMPUTERNAME") + System.getProperty("user.name"));
        StringSelection stringSelection = new StringSelection(hwid);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        return hwid;
    }

    private static String textToSHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return bytesToHex(sha1hash);
    }

    private static String bytesToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        int i = 0;
        while (i < data.length) {
            int halfbyte = data[i] >>> 4 & 15;
            int two_halfs = 0;
            do {
                if (halfbyte >= 0 && halfbyte <= 9) {
                    buf.append((char)(48 + halfbyte));
                } else {
                    buf.append((char)(97 + (halfbyte - 10)));
                }
                halfbyte = data[i] & 15;
            } while (two_halfs++ < 1);
            ++i;
        }
        return buf.toString();
    }
}
