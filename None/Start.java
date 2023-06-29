import java.util.Arrays;
import net.minecraft.client.main.Main;

public class Start {
    public static void main(String[] args) {
        Main.main(Start.concat(new String[]{"--version", "1.8.8", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        String str = "";
        T[] TArray = result;
        int n = result.length;
        int n2 = 0;
        while (n2 < n) {
            T result1 = TArray[n2];
            str = String.valueOf(str) + result1;
            ++n2;
        }
        System.out.println(str);
        return result;
    }
}
