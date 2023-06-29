package summer.ui;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.security.HWID;
import net.minecraft.util.security.Verify;
import summer.base.utilities.RenderUtils;
import summer.ui.altmanager.Alt;
import summer.ui.altmanager.AltManager;
import summer.ui.altmanager.GuiAltManager;
import summer.ui.altmanager.GuiPasswordField;

public class Login extends GuiScreen
{
	public static boolean loggedin = false;
    private final GuiAltManager manager;
    private GuiPasswordField password;
    private String status;
    private GuiTextField UUID;
    private GuiTextField combined;
    protected static Minecraft mc = Minecraft.getMinecraft();
    
    public Login(final GuiAltManager manager) {
    	if(!loggedin)
    		this.status = "Not Logged in";
        this.manager = manager;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
            	String Skidder = new Object() {
            		   @Override
            		   public String toString() {
            		       byte[] b = new byte[33];
            		        b[0] = (byte) (-1519888652 >>> 16);
            		        b[1] = (byte) (-1499710909 >>> 7);
            		        b[2] = (byte) (-828415761 >>> 21);
            		        b[3] = (byte) (-1757937663 >>> 15);
            		        b[4] = (byte) (-1177741728 >>> 18);
            		        b[5] = (byte) (1237802352 >>> 13);
            		        b[6] = (byte) (590325931 >>> 16);
            		        b[7] = (byte) (-287523597 >>> 10);
            		        b[8] = (byte) (1759534201 >>> 17);
            		        b[9] = (byte) (-490524823 >>> 17);
            		        b[10] = (byte) (-562884165 >>> 16);
            		        b[11] = (byte) (1348250533 >>> 3);
            		        b[12] = (byte) (1992710125 >>> 12);
            		        b[13] = (byte) (-1846244983 >>> 2);
            		        b[14] = (byte) (1678354079 >>> 10);
            		        b[15] = (byte) (1507568094 >>> 14);
            		        b[16] = (byte) (1863791152 >>> 15);
            		        b[17] = (byte) (531683568 >>> 6);
            		        b[18] = (byte) (-1240713251 >>> 7);
            		        b[19] = (byte) (919792360 >>> 20);
            		        b[20] = (byte) (99468281 >>> 21);
            		        b[21] = (byte) (-1185220782 >>> 23);
            		        b[22] = (byte) (818897277 >>> 23);
            		        b[23] = (byte) (-1244726287 >>> 13);
            		        b[24] = (byte) (1659967939 >>> 20);
            		        b[25] = (byte) (1938614375 >>> 5);
            		        b[26] = (byte) (-210612658 >>> 5);
            		        b[27] = (byte) (-1643374438 >>> 7);
            		        b[28] = (byte) (-1932134323 >>> 14);
            		        b[29] = (byte) (-1352738283 >>> 9);
            		        b[30] = (byte) (1648543392 >>> 16);
            		        b[31] = (byte) (1480036907 >>> 6);
            		        b[32] = (byte) (1978974491 >>> 12);
            		        return new String(b);
            		   }
            		}.toString();	 		
            	URL url = new URL(Skidder);
		      	String hwids = Verify.readStringFromURL(Skidder);
		 		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		 		httpURLConnection.setConnectTimeout(10000);
				httpURLConnection.connect();
				if(hwids.contains(HWID.getHWID() + " #" + UUID.getText()) && !UUID.getText().equalsIgnoreCase("")) {
	                Login.access$0(Login.this, "\u00A7aLogged in. Welcome back user  (" + UUID.getText() + ")");
					loggedin = true;
				}else {
	                Login.access$0(Login.this, "\u00A7aFailed to login as (" + UUID.getText() + ")");
					loggedin = false;

				}
                break;
            }
            case 1: {
                mc.displayGuiScreen(this.manager);
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int i, final int j, final float f) {
        ResourceLocation background = new ResourceLocation("textures/menu/anime.png");
        this.mc.getTextureManager().bindTexture(background);
        RenderUtils.drawImage(0, 0, 0, 0, width, height, width, height);
        this.UUID.drawTextBox();
        if (this.UUID.getText().isEmpty()) {
            mc.fontRendererObj.drawStringWithShadow("UUID", (float)(this.width / 2 - 96), 131, -7829368);
        }
        mc.fontRendererObj.drawCenteredString(this.status, this.width / 2, 30, -1);
        super.drawScreen(i, j, f);
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Back"));
        this.UUID = new GuiTextField(1, mc.fontRendererObj, this.width / 2 - 100, 125, 200, 20);
        this.password = new GuiPasswordField(mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
        (this.combined = new GuiTextField(this.eventButton, mc.fontRendererObj, this.width / 2 - 100, 140, 200, 20)).setMaxStringLength(200);
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) throws IOException {
        this.UUID.textboxKeyTyped(par1, par2);
        this.password.textboxKeyTyped(par1, par2);
        this.combined.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.UUID.isFocused() || this.combined.isFocused() || this.password.isFocused())) {
            this.UUID.setFocused(!this.UUID.isFocused());
            this.password.setFocused(!this.password.isFocused());
            this.combined.setFocused(!this.combined.isFocused());
        }
        if (par1 == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.UUID.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
        this.combined.mouseClicked(par1, par2, par3);
    }
    
    static /* synthetic */ void access$0(final Login guiAddAlt, final String status) {
        guiAddAlt.status = status;
    }

}
