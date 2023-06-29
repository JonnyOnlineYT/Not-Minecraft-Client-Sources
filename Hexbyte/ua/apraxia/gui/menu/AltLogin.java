package ua.apraxia.gui.menu;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Session;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import ua.apraxia.Hexbyte;
import ua.apraxia.utility.font.Fonts;
import ua.apraxia.utility.other.HoverUtility;
import ua.apraxia.utility.render.GLSL;
import ua.apraxia.utility.render.RenderUtility;
import ua.apraxia.utility.render.RoundedUtility;

public class AltLogin extends GuiScreen {
    GuiClientTextField inputField;
    private GLSL backgroundShader;
    private final long initTime = System.currentTimeMillis();

    public AltLogin() {
    }

    public void initGui() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        try {
            this.backgroundShader = new GLSL("/assets/noise.fsh");
        }
        catch (IOException var2) {
            throw new IllegalStateException("Failed to load backgound shader", var2);
        }
        int i = sr.getScaledHeight() / 4 + 48;
        this.buttonList.clear();
        this.inputField = new GuiClientTextField(1, this.fontRenderer, sr.getScaledWidth() / 2 - 100, i + 72 - 12, 200, 20);
        this.inputField.setText("");
    }

    public static void changeName(String name) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        auth.logOut();
        Session session = new Session(name, name, "0", "legacy");

        try {
            setSession(session);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            changeName(this.inputField.getText());
        }

    }

    public void updateScreen() {
        this.inputField.updateCursorCounter();
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        switch (keyCode) {
            case 1:
                this.mc.displayGuiScreen(new GuiMainMenu());
                break;
            default:
                this.inputField.textboxKeyTyped(typedChar, keyCode);
        }

    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(this.mc);
        if (HoverUtility.isHovered(mouseX, mouseY, (double)(sr.getScaledWidth() / 2 - 100), (double)(sr.getScaledHeight() / 4 + 48 + 72 + 12), 200.0, 20.0)) {
            changeName(this.inputField.getText());
        }

        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.backgroundShader.useShader(sr.getScaledWidth(), sr.getScaledHeight(), mouseX, mouseY, (float)(System.currentTimeMillis() - this.initTime) / 1500.0f);
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)-1.0f, (float)-1.0f);
        GL11.glVertex2f((float)-1.0f, (float)1.0f);
        GL11.glVertex2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)1.0f, (float)-1.0f);
        GL11.glEnd();
        GL20.glUseProgram((int)0);
        GlStateManager.disableCull();
        GlStateManager.pushMatrix();
        RoundedUtility.drawRound((float)(sr.getScaledWidth() / 2 - 100), (float)(sr.getScaledHeight() / 4 + 48 + 72 + 12), 200.0F, 20.0F, 3.0F,  HoverUtility.isHovered(mouseX, mouseY, (double)(sr.getScaledWidth() / 2 - 100), (double)(sr.getScaledHeight() / 4 + 48 + 72 + 12), 200.0, 20.0) ? new Color(255, 255, 255, 18) :  new Color(255, 255, 255, 16));
        RenderUtility.drawBlur(() -> RoundedUtility.drawRound((float)(sr.getScaledWidth() / 2 - 100), (float)(sr.getScaledHeight() / 4 + 48 + 72 + 12), 200.0F, 20.0F, 3.0F,  HoverUtility.isHovered(mouseX, mouseY, (double)(sr.getScaledWidth() / 2 - 100), (double)(sr.getScaledHeight() / 4 + 48 + 72 + 12), 200.0, 20.0) ? new Color(255, 255, 255, 18) :  new Color(255, 255, 255, 16)), 15);
        Fonts.medium16.drawString("Войти", (float)(sr.getScaledWidth() / 2 - 10), (float)(sr.getScaledHeight() / 4 + 48 + 72 + 20), -1);

        for(int i = 0; i < this.buttonList.size(); ++i) {
            ((GuiButton)this.buttonList.get(i)).drawButton(this.mc, mouseX, mouseY, partialTicks);
        }

        this.inputField.drawTextBox();
        Fonts.medium16.drawString("Твой никнейм: " + this.mc.getSession().getUsername(), (float)(sr.getScaledWidth() / 2 - 100), (float)(sr.getScaledHeight() / 4 + 48 + 110), Color.WHITE.getRGB());
        GlStateManager.popMatrix();
    }






    public static void setSession(Session s) {
        Class<? extends Minecraft> mc = Minecraft.getMinecraft().getClass();

        try {
            Field session = null;
            Field[] var3 = mc.getDeclaredFields();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Field f = var3[var5];
                if (f.getType().isInstance(s)) {
                    session = f;
                }
            }

            if (session == null) {
                throw new IllegalStateException("Session Null");
            }

            session.setAccessible(true);
            session.set(Minecraft.getMinecraft(), s);
            session.setAccessible(false);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }
}
