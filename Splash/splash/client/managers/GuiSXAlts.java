package splash.client.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.lwjgl.input.Keyboard;

import com.google.gson.JsonObject;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.auth.service.AlteningServiceType;
import com.thealtening.auth.service.ServiceSwitcher;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import splash.Splash;

public class GuiSXAlts extends GuiScreen {

    private GuiTextField passsword;
    private final GuiScreen previousScreen;
    private GuiTextField username;
    
    private static String status = "";
    
    protected Minecraft mc = Minecraft.getMinecraft();
    
    public GuiSXAlts(final GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
        status = EnumChatFormatting.YELLOW + "Waiting...";
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 1: {
                if (Splash.sxAlts.equals("")) {
                	this.status = EnumChatFormatting.RED + "Please log in.";
                } else {
                	Thread generate = new Thread(new Runnable() {

						@Override
						public void run() {
		                	status = generate();
						}
                		
                	});
                	generate.start();
                }
                break;
            }
            case 0: {
            	if (!this.username.getText().isEmpty() && !this.passsword.getText().isEmpty()) {
            		this.status = EnumChatFormatting.YELLOW + "Logging in...";
            		
            		JsonObject json = new JsonObject();
            		json.addProperty("username", this.username.getText());
            		json.addProperty("password", this.passsword.getText());

            		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            		try {
            		    HttpPost request = new HttpPost("https://sx-alts-server.herokuapp.com/api/login");
            		    StringEntity params = new StringEntity(json.toString());
            		    request.addHeader("content-type", "application/json");
            		    request.setEntity(params);
            		    CloseableHttpResponse c = httpClient.execute(request);
            		    BufferedReader br = new BufferedReader(new InputStreamReader(c.getEntity().getContent()));
            		    
            		    String line = br.readLine();
            		    
            		    if (line != null) {
            		    	boolean success = Boolean.parseBoolean(line.split(":")[1].split(",")[0]);
            		    	if (success) {
            		    		this.status = EnumChatFormatting.GREEN + "Logged in successfully";
            		    		Splash.sxAlts = line.split(":")[2].substring(1, line.split(":")[2].length() - 2);
            		    	} else {
            		    		this.status = EnumChatFormatting.RED + line.split(":")[2].substring(1, line.split(":")[2].length() - 2);
            		    	}
            		    } else {
            		    	this.status = EnumChatFormatting.RED + "There was a error logging in.";
            		    }
            		} catch (Exception ex) {
            		    ex.printStackTrace();
            		} finally {
            		    httpClient.close();
            		}
            	} else {
            		this.status = EnumChatFormatting.RED + "Please enter a correct a username & a password";
            	}
                break;
            }
            case 2: {
            	Minecraft.getMinecraft().displayGuiScreen(this.previousScreen);
                break;
            }
        }
    }
    
    public static String generate() {
    	if (Splash.sxAlts.equals("")) {
    		return EnumChatFormatting.RED + "Please log in with a S-X account";
    	}
    	
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("https://sx-alts-server.herokuapp.com/api/alt");
        
        request.addHeader("token", Splash.sxAlts);

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();

            if (entity != null) {
            	String status = EnumChatFormatting.RED + "There was a error generating";
                String result = EntityUtils.toString(entity);
                System.out.println(result);
                boolean success = Boolean.parseBoolean(result.split(":")[1].split(",")[0]);
		    	if (success) {
		    		status = EnumChatFormatting.YELLOW + "Logging in...";
		    		String alt = result.substring(23, result.length() - 2);
		    		if (alt.contains(":")) {
		    			System.out.println("Contains");
		    			login(alt.split(":")[0], alt.split(":")[1]);
		    		}
		    	} else {
		    		status = EnumChatFormatting.RED + result.split(":")[2].substring(1, result.split(":")[2].length() - 2);
		    	}
                return status;
            }
        } catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return EnumChatFormatting.RED + "There was a error generating";
    }
    
	public static void login(String user, String pass) {
		Thread login = new Thread(new Runnable() {

			@Override
			public void run() {
				new ServiceSwitcher().switchToService(AlteningServiceType.MOJANG);
				status = EnumChatFormatting.YELLOW + "Logging in...";
		        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
		        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
		        auth.setUsername(user);
		        auth.setPassword(pass);
		        try {
		            auth.logIn();
		            Minecraft.getMinecraft().session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
		            status = EnumChatFormatting.GREEN + "Logged in as " + auth.getSelectedProfile().getName() + ".";
		        }
		        catch (AuthenticationException localAuthenticationException) {
		            localAuthenticationException.printStackTrace();
		            status = EnumChatFormatting.RED + "Login failed.";
		        }
			}
			
		});
		login.start();
	}
    
    @Override
    public void drawScreen(final int x, final int y, final float z) {
    	this.drawDefaultBackground();
        this.passsword.drawTextBox();
        this.username.drawTextBox();
        mc.fontRendererObj.drawCenteredString("Alt Login", this.width / 2, 20, -1);
        mc.fontRendererObj.drawStringWithShadow((Splash.sxAlts.equals("") ? EnumChatFormatting.RED : EnumChatFormatting.GREEN) + "Logging in: " + !Splash.sxAlts.equals(""), 20, 20, -1);
        mc.fontRendererObj.drawCenteredString(status, this.width / 2, 29, -1);
        if (this.passsword.getText().isEmpty()) {
            mc.fontRendererObj.drawStringWithShadow("Password", (float)(this.width / 2 - 94), this.passsword.isFocused() ? 129.0f : 131.0f, -7829368);
        }
        if (this.username.getText().isEmpty()) {
            mc.fontRendererObj.drawStringWithShadow("Username", (float)(this.width / 2 - 94), this.username.isFocused() ? 104.0f : 106.0f, -7829368);
        }
        super.drawScreen(x, y, z);
    }
    
    @Override
    public void initGui() {
        final int var3 = this.height / 4 + 24;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 175, "Generate"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 150, "Login"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 198, "Back"));
        this.passsword = new GuiTextField(var3, this.fontRendererObj, this.width / 2 - 98, 125, 195, 20);
        this.username = new GuiTextField(var3, this.fontRendererObj, this.width / 2 - 98, 100, 195, 20);
        this.passsword.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    protected void keyTyped(final char character, final int key) throws IOException {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t' && (this.passsword.isFocused() || this.username.isFocused())) {
            this.passsword.setFocused(!this.passsword.isFocused());
            this.username.setFocused(!this.username.isFocused());
        }
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
        this.passsword.textboxKeyTyped(character, key);
        this.username.textboxKeyTyped(character, key);
    }
    
    @Override
    protected void mouseClicked(final int x, final int y, final int button) {
        try {
            super.mouseClicked(x, y, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.passsword.mouseClicked(x, y, button);
        this.passsword.mouseClicked(x, y, button);
        this.username.mouseClicked(x, y, button);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void updateScreen() {
        this.passsword.updateCursorCounter();
        this.passsword.updateCursorCounter();
        this.username.updateCursorCounter();
    }
    
    
}
