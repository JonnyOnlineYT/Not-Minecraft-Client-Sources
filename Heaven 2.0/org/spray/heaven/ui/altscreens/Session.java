package org.spray.heaven.ui.altscreens;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;

import java.net.Proxy;

public class Session extends Thread{
    public String altName;
    public boolean failed;
    public String pass;
    public Session(String name, String password) {
        altName = name;
        pass = password;
    }

    private net.minecraft.util.Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service
                .createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new net.minecraft.util.Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(),
                    auth.getAuthenticatedToken(), "mojang");
        } catch (AuthenticationException localAuthenticationException) {
            localAuthenticationException.printStackTrace();
            return null;
        }
    }

    @Override
    public void run() {
        if (this.pass.equals("")) {
            Minecraft.getMinecraft().setSession(new net.minecraft.util.Session(altName, "", "", "mojang"));
            return;
        }
        net.minecraft.util.Session auth = this.createSession(altName, pass);
        if (auth == null) {
            failed = true;
        } else {
            Minecraft.getMinecraft().setSession(auth);
        }
    }


}
