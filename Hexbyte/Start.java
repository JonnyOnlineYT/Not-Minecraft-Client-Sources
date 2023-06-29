import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.main.Main;

import java.net.Proxy;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Start
{

    public static void main(String[] args) throws AuthenticationException {
        Main.main(new String[]{"--version", "mcp", "--accessToken", "0", "--gameDir", "", "--assetsDir", "", "--assetIndex", "1.12", "--userProperties", "{}", "--username", "Penis"});
    }
}
