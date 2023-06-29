import net.minecraft.client.main.Main;

public class Start {

    public static void main(String[] args) {
        String[] option = {"--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.12", "--userProperties", "{}"};
        Main.main(option);
    }
}
