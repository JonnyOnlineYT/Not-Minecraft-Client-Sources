package splash.utilities.hastebin;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Author: Ice
 * Created: 22:31, 09-Jun-20
 * Project: Client
 */
public class Hastebin {

    public static String post(String text, boolean raw) throws IOException {
        byte[] postData = text.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        String requestURL = "https://hastebin.com/documents";
        URL url = new URL(requestURL);
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", "JavaAPI");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);
        String response = null;
        try {
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.contains("\"key\"")) {
            response = response.substring(response.indexOf(":") + 2, response.length() - 2);
            String postURL = raw ? "https://hastebin.com/raw/" : "https://hastebin.com/";
            response = String.valueOf(postURL) + response;
        }
        return response;
    }

    public static String postWithCode(String input, boolean raw) {
        String url = "";
        try {
            url = post(input, raw);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url.replace("https://hastebin.com/raw/", "");
    }
}
