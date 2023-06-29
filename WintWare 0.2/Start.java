import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import net.minecraft.client.main.Main;

public class Start {
   public static <T> T[] da(T[] first, T[] second) {
      T[] result = Arrays.copyOf(first, first.length + second.length);
      System.arraycopy(second, 0, result, first.length, second.length);
      return result;
   }

   public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
      Main.main((String[])da(new String[]{"--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.12.2", "--userProperties", "{}"}, args));
   }
}
