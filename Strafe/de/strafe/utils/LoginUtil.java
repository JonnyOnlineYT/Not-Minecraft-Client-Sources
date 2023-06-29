package de.strafe.utils;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import lombok.experimental.UtilityClass;
import net.minecraft.util.Session;

import java.net.Proxy;

/**
 * @author XButtonn
 * @created 22.02.2022 - 21:52
 */

@UtilityClass
public class LoginUtil implements IMinecraft {

    public boolean login(String email, String password) {
        UserAuthentication auth = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(email);
        auth.setPassword(password);

        if (auth.canLogIn()) {
            try {
                auth.logIn();
            } catch (AuthenticationException e) {
                return false;
            }
        }
        mc.session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        return true;
    }

}
