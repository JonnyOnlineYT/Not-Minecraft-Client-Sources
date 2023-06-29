package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Runnables;

import javafx.animation.Interpolator;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import optifine.CustomPanorama;
import optifine.CustomPanoramaProperties;
import optifine.Reflector;
import ru.rockstar.Main;
import ru.rockstar.api.changelogs.ChangeLog;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event2D;
import ru.rockstar.api.utils.other.ParticleEngine;
import ru.rockstar.api.utils.other.SoundHelper;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.glsandbox.animbackground;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.ui.altmanager.GuiAltButton;
import ru.rockstar.client.ui.altmanager.GuiAltManager;
import ru.rockstar.client.ui.settings.button.ImageButton;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.Project;


public class GuiMainMenu extends GuiScreen
{
	public boolean hovered;
	public float currentValueAnimate = 0f;
	public float s1 = 0f;
	public float s2 = 0f;
	protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
    private static final Logger LOGGER;
    private static final Random RANDOM;
    public static ParticleEngine engine;
    private final float updateCounter;
    private String splashText;
    private GuiButton buttonResetDemo;
    private float panoramaTimer;
    private DynamicTexture viewportTexture;
    private final Object threadLock;
    public static final String MORE_INFO_TEXT;
    private int openGLWarning2Width;
    private int openGLWarning1Width;
    private int openGLWarningX1;
    private int openGLWarningY1;
    private int openGLWarningX2;
    private int openGLWarningY2;
    private String openGLWarning1;
    private String openGLWarning2;
    private String openGLWarningLink;
    private static final ResourceLocation SPLASH_TEXTS;
    private static final ResourceLocation MINECRAFT_TITLE_TEXTURES;
    private static final ResourceLocation field_194400_H;
    private static final ResourceLocation[] TITLE_PANORAMA_PATHS;
    private ResourceLocation backgroundTexture;
    private GuiButton realmsButton;
    private boolean hasCheckedForRealmsNotification;
    private GuiScreen realmsNotification;
    private int field_193978_M;
    private int field_193979_N;
    private GuiButton modButton;
    private GuiScreen modUpdateNotification;
    private final long initTime;
    private final animbackground backgroundShader;
    public static float progress;
    public static float anim;
    private long lastMS;
    public static String maintext = "Created by ConeTin";
    public static float bganim;
    public GuiMainMenu() {
    	
        this.imageButtons = new ArrayList<ImageButton>();
        this.threadLock = new Object();
        this.initTime = System.currentTimeMillis();
        this.openGLWarning2 = GuiMainMenu.MORE_INFO_TEXT;
        this.splashText = "missingno";
        IResource iresource = null;
        try {
            this.backgroundShader = new animbackground("/noise.fsh");
        }
        catch (IOException var2) {
            throw new IllegalStateException("Failed to load backgound shader", var2);
        }
        try {
            List<String> list = Lists.<String>newArrayList(); 
            iresource = Minecraft.getMinecraft().getResourceManager().getResource(GuiMainMenu.SPLASH_TEXTS);
            final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8));
            String s;
            while ((s = bufferedreader.readLine()) != null) {
                s = s.trim();
                if (!s.isEmpty()) {
                    list.add(s);
                }
            }
            if (!list.isEmpty()) {
                do {
                    this.splashText = list.get(GuiMainMenu.RANDOM.nextInt(list.size()));
                } while (this.splashText.hashCode() == 125780783);
            }
        }
        catch (IOException ex) {}
        finally {
            IOUtils.closeQuietly((Closeable)iresource);
        }
        this.updateCounter = GuiMainMenu.RANDOM.nextFloat();
        this.openGLWarning1 = "";
    }
    
    private boolean areRealmsNotificationsEnabled() {
        return Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && this.realmsNotification != null;
    }
    
    @Override
    public void updateScreen() {
        if (this.areRealmsNotificationsEnabled()) {
            this.realmsNotification.updateScreen();
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    TimerHelper timerHelper = new TimerHelper();
    
    @Override
    public void initGui() {
			maintext = "Created By ConeTin";
    	bganim = 0;
    	s2 = 320;
    	
    	final ScaledResolution sr = new ScaledResolution(this.mc);
    	this.lastMS = System.currentTimeMillis();
    	
    	this.progress = 0.0f;
    	

    	//SoundHelper.playSound("sea.wav", 0.2f, false);
		 
    	
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("1", this.viewportTexture);
        this.field_193978_M = this.fontRendererObj.getStringWidth("1");
        this.field_193979_N = this.width - this.field_193978_M - 49;
        final int i = 24;
        final int j = this.height / 4 + 48;
        if (this.mc.isDemo()) {
            this.addDemoButtons(j, 24);
        }
        else {
            this.addSingleplayerMultiplayerButtons(j, 24);
        }
        synchronized (this.threadLock) {
            this.openGLWarning1Width = this.fontRendererObj.getStringWidth(this.openGLWarning1);
            this.openGLWarning2Width = this.fontRendererObj.getStringWidth(this.openGLWarning2);
            final int k = Math.max(this.openGLWarning1Width, this.openGLWarning2Width);
            this.openGLWarningX1 = (this.width - k) / 2;
            this.openGLWarningX2 = this.openGLWarningX1 + k;
            this.openGLWarningY2 = this.openGLWarningY1 + 24;
        }
        this.mc.setConnectedToRealms(false);
        if (Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && !this.hasCheckedForRealmsNotification) {
            final RealmsBridge realmsbridge = new RealmsBridge();
            this.realmsNotification = realmsbridge.getNotificationScreen(this);
            this.hasCheckedForRealmsNotification = true;
        }
        if (this.areRealmsNotificationsEnabled()) {
            this.realmsNotification.setGuiSize(this.width, this.height);
            this.realmsNotification.initGui();
        }
        if (Reflector.NotificationModUpdateScreen_init.exists()) {
            this.modUpdateNotification = (GuiScreen)Reflector.call(Reflector.NotificationModUpdateScreen_init, this, this.modButton);
        }
        this.imageButtons.clear();
    }
    
    private void addSingleplayerMultiplayerButtons(final int p_73969_1_, final int p_73969_2_) {
    	final ScaledResolution sr = new ScaledResolution(this.mc);
    	
        if (Reflector.GuiModList_Constructor.exists()) {
            this.realmsButton = this.addButton(new GuiButton(14, this.width / 2, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("menu.online", new Object[0]).replace("Minecraft", "").trim()));
            this.buttonList.add(this.modButton = new GuiButton(6, this.width / 2 - 110, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("fml.menu.mods", new Object[0])));
        }
        else {
        }
    }
    
    private void addDemoButtons(final int p_73972_1_, final int p_73972_2_) {
        this.buttonResetDemo = this.addButton(new GuiButton(12, this.width / 2 - 110, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0])));
        final ISaveFormat isaveformat = this.mc.getSaveLoader();
        final WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
        if (worldinfo == null) {
            this.buttonResetDemo.enabled = false;
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (button.id == 5) {
        }
        if (button.id == 14) {
            this.mc.displayGuiScreen(new GuiAltManager());
        }
        if (button.id == 4) {
            this.mc.shutdown();
        }
        if (button.id == 6 && Reflector.GuiModList_Constructor.exists()) {
            this.mc.displayGuiScreen((GuiScreen)Reflector.newInstance(Reflector.GuiModList_Constructor, this));
        }
        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", WorldServerDemo.DEMO_WORLD_SETTINGS);
        }
        if (button.id == 12) {
            final ISaveFormat isaveformat = this.mc.getSaveLoader();
            final WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
            if (worldinfo != null) {
                this.mc.displayGuiScreen(new GuiYesNo(this, I18n.format("selectWorld.deleteQuestion", new Object[0]), "'" + worldinfo.getWorldName() + "' " + I18n.format("selectWorld.deleteWarning", new Object[0]), I18n.format("selectWorld.deleteButton", new Object[0]), I18n.format("gui.cancel", new Object[0]), 12));
            }
        }
    }
    
    private void switchToRealms() {
        final RealmsBridge realmsbridge = new RealmsBridge();
        realmsbridge.switchToRealms(this);
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (result && id == 12) {
            final ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        }
        else if (id == 12) {
            this.mc.displayGuiScreen(this);
        }
        else if (id == 13) {
            if (result) {
                try {
                    final Class<?> oclass = Class.forName("java.awt.Desktop");
                    final Object object = oclass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI(this.openGLWarningLink));
                }
                catch (Throwable throwable1) {
                    GuiMainMenu.LOGGER.error("Couldn't open link", throwable1);
                }
            }
            this.mc.displayGuiScreen(this);
        }
    }
    
    
    @EventTarget
    public void renderAnim(Event2D render) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
    	bganim = (float) Interpolator.LINEAR.interpolate((double)this.bganim, 255, 0.05);
    	if (this.progress >= 1.0f) {
            this.progress = 1.0f;
        }
        else {
            this.progress = (System.currentTimeMillis() - this.lastMS) / 550.0f;
        }    
    	
    	for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (Mouse.isButtonDown(0)) {
                imageButton.onClick(mouseX, mouseY);
            }
        }
    	
        final ScaledResolution sr = new ScaledResolution(this.mc);
        
        
        DrawHelper.drawRect(0,0,(float)sr.getScaledWidth(),sr.getScaledHeight(), new Color(30,30,30, (int) bganim).getRGB());
        
        
       // DrawHelper.drawGlow(mouseX - 10, mouseY - 100, mouseX + 10, mouseY + 175, ClientHelper.getClientColor().getRGB());
        
        //DrawHelper.drawImage(new ResourceLocation("bg.png"), 0,0,sr.getScaledWidth(),sr.getScaledHeight(),new Color(-1));
        
        
        
        
        
        
        
        
        
        
        
       
        
        
        
        
        
        
        
        
        
        mc.mntsb_25.drawString("Клиент", sr.getScaledWidth() - 228, sr.getScaledHeight() - 175, -1);
        
        DrawHelper.drawRect(sr.getScaledWidth() - 80, sr.getScaledHeight() - 80, sr.getScaledWidth() - 10, sr.getScaledHeight() - 10, new Color(59, 63, 65).getRGB());
       
        DrawHelper.drawRect(sr.getScaledWidth() - 155, sr.getScaledHeight() - 80, sr.getScaledWidth() - 85, sr.getScaledHeight() - 10, new Color(59, 63, 65).getRGB());
        
        DrawHelper.drawRect(sr.getScaledWidth() - 230, sr.getScaledHeight() - 80, sr.getScaledWidth() - 160, sr.getScaledHeight() - 10, new Color(59, 63, 65).getRGB());
        
        
        DrawHelper.drawRect(sr.getScaledWidth() - 80, sr.getScaledHeight() - 155, sr.getScaledWidth() - 10, sr.getScaledHeight() - 85, new Color(59, 63, 65).getRGB());
        
        DrawHelper.drawRect(sr.getScaledWidth() - 155, sr.getScaledHeight() - 155, sr.getScaledWidth() - 85, sr.getScaledHeight() - 85, new Color(59, 63, 65).getRGB());
        
        DrawHelper.drawRect(sr.getScaledWidth() - 230, sr.getScaledHeight() - 155, sr.getScaledWidth() - 160, sr.getScaledHeight() - 85, new Color(59, 63, 65).getRGB());
        
        
        

        
        
        DrawHelper.drawImage(new ResourceLocation("singleplayer.png"), sr.getScaledWidth() - 230, sr.getScaledHeight() - 330 + 175, 70, 70, new Color(-1));

        DrawHelper.drawImage(new ResourceLocation("multyplayer.png"), sr.getScaledWidth() - 155, sr.getScaledHeight() - 330 + 175, 70, 70, new Color(-1));

        DrawHelper.drawImage(new ResourceLocation("altmanager.png"), sr.getScaledWidth() - 80, sr.getScaledHeight() - 330 + 175, 70, 70, new Color(-1));

        DrawHelper.drawImage(new ResourceLocation("settings.png"), sr.getScaledWidth() - 230, sr.getScaledHeight() - 255 + 175, 70, 70, new Color(-1));

        DrawHelper.drawImage(new ResourceLocation("quit.png"),sr.getScaledWidth() - 154, sr.getScaledHeight() - 255 + 175, 70, 70, new Color(-1));

        DrawHelper.drawImage(new ResourceLocation("username.png"),sr.getScaledWidth() - 80, sr.getScaledHeight() - 255 + 175, 70, 70, new Color(-1));

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        mc.mntsb_30.drawString("ROCKSTAR", 40 - s2, 5, -1);
        
        mc.mntsb_20.drawString(maintext, 40 - s2, 20, -1);
        
        
        
        
        
        
        
        super.drawScreen(mouseX, mouseY, partialTicks);
        int y = 40;
        final int x = 3;
        
        
        
        for (final ChangeLog log : Main.instance.changeManager.getChangeLogs()) {
            GlStateManager.pushMatrix();
            if (log != null) {
                DrawHelper.scissorRect(0.0f, 0.0f, (float)(this.width / 2 - 20), this.height - 10);
                this.mc.neverlose500_10.drawString(log.getLogName(), 40 - s2, y - 0.1f, -1);
            }
            y += 5;
            GlStateManager.popMatrix();
        }
        
        drawPanel(mouseX, mouseY);
    }
    
    public void drawPanel(int mouseX, int mouseY) {
    	final ScaledResolution sr = new ScaledResolution(this.mc);
    	
    	
    	if (mouseX < 35) {
    		hovered = true;
    	} else if (mouseX > 120) {
    		hovered = false;
    	}
    	
   	 	this.currentValueAnimate = (float)Interpolator.LINEAR.interpolate((double)this.currentValueAnimate, bganim < 200 ? 0 : this.hovered ? 120 : 35, 0.1);

   	 	this.s2 = (float)Interpolator.LINEAR.interpolate((double)this.s2, bganim > 200 ? this.hovered ? -85 : 0 : 320, 0.2);

   	 	
    	//currentValueAnimate = hovered ? 120 : 35;
    	
    	
    	
   	 	GlStateManager.pushMatrix();
   	 	GlStateManager.enable(GL11.GL_SCISSOR_TEST);
 	
 		DrawHelper.scissorRect(0.0f, 0.0f, (currentValueAnimate), this.height);
 	
 		int s = -1;
 		if (mouseX < currentValueAnimate) {
            if (mouseY > sr.getScaledHeight() - 32*6 &&  mouseY < sr.getScaledHeight() - 32*6 + 30) {
            	s = 6;
            }
            if (mouseY > sr.getScaledHeight() - 32*5 &&  mouseY < sr.getScaledHeight() - 32*5 + 30) {
            	s = 5;
            }
            if (mouseY > sr.getScaledHeight() - 32*4 &&  mouseY < sr.getScaledHeight() - 32*4 + 30) {
            	s = 4;
            }
            if (mouseY > sr.getScaledHeight() - 32*3 &&  mouseY < sr.getScaledHeight() - 32*3 + 30) {
            	s = 3;
            }
            if (mouseY > sr.getScaledHeight() - 32*2 &&  mouseY < sr.getScaledHeight() - 32*2 + 30) {
            	s = 2;
            }
    	}
 		
 		
 		this.s1 = (float)Interpolator.LINEAR.interpolate((double)this.s1, (mouseX < currentValueAnimate && mouseY > sr.getScaledHeight() - 32*6) ? currentValueAnimate : 0, 0.1);

 	
 		
 		
 		
 		
 		
    	DrawHelper.drawRect(0,0, currentValueAnimate, sr.getScaledWidth(), new Color(42, 42, 42).getRGB());
    	
    	DrawHelper.drawGradientRect(0, sr.getScaledHeight() - 32*s - 3, s1, sr.getScaledHeight() - 32*s - 1, new Color(0,0,0,0).getRGB(), new Color(0,0,0, 50).getRGB());
    	DrawHelper.drawRect(0, sr.getScaledHeight() - 32*s - 1, s1, sr.getScaledHeight() - 32*s + 32 - 1, new Color(82, 82, 82).getRGB());
    	DrawHelper.drawGradientRect(0, sr.getScaledHeight() - 32*s - 1 + 32, s1, sr.getScaledHeight() - 32*s + 32 - 1 + 2, new Color(0,0,0,50).getRGB(), new Color(0,0,0,0).getRGB());
    	
    	DrawHelper.drawGradientRect1( currentValueAnimate,0,  currentValueAnimate + 5, sr.getScaledWidth(), new Color(0, 0, 0, 200).getRGB(), new Color(0,0,0,0).getRGB());

    	
    	
    	
    	DrawHelper.drawImage(new ResourceLocation("icon.png"),2, 0, 30, 30, new Color(-1));
    	DrawHelper.drawTribleGradient(3, 30, currentValueAnimate - 7, 0.7f, new Color(255,255,255,0), new Color(255,255,255), new Color(255,255,255,0));
    
    	DrawHelper.drawImage(new ResourceLocation("singleplayer.png"),3, sr.getScaledHeight() - 32*6, 30, 30, new Color(-1));

    	DrawHelper.drawImage(new ResourceLocation("multyplayer.png"),3, sr.getScaledHeight() - 32*5, 30, 30, new Color(-1));

    	DrawHelper.drawImage(new ResourceLocation("altmanager.png"),3, sr.getScaledHeight() - 32*4, 30, 30, new Color(-1));

    	DrawHelper.drawImage(new ResourceLocation("settings.png"),3, sr.getScaledHeight() - 32*3, 30, 30, new Color(-1));

    	DrawHelper.drawImage(new ResourceLocation("quit.png"),3, sr.getScaledHeight() - 32*2, 30, 30, new Color(-1));

    	DrawHelper.drawTribleGradient(3, sr.getScaledHeight() - 32, currentValueAnimate - 7, 0.7f, new Color(255,255,255,0), new Color(255,255,255), new Color(255,255,255,0));
    
    	DrawHelper.drawImage(new ResourceLocation("username.png"),2, sr.getScaledHeight() - 30, 30, 30, new Color(-1));
    
    	
    	
    	mc.mntsb_25.drawString("Rockstar", 37, 10, -1);
    	
    	
    	mc.mntsb_20.drawString("Single", 37, height - 21 - 34 - 34 - 33 - 34 - 31, -1);
    	mc.mntsb_20.drawString("Player", 37, height - 18 - 34 - 34 - 24 - 34 - 31, -1);
    	
    	mc.mntsb_20.drawString("Multi", 37, height - 21 - 34 - 34 - 33 - 34, -1);
    	mc.mntsb_20.drawString("Player", 37, height - 18 - 34 - 34 - 24 - 34, -1);
    	
    	mc.mntsb_20.drawString("Alt", 37, height - 21 - 34 - 34 - 33, -1);
    	mc.mntsb_20.drawString("Manager", 37, height - 18 - 34 - 34 - 24, -1);
    	
    	mc.mntsb_20.drawString("Settings", 37, height - 18 - 32 - 34, -1);
    	
    	mc.mntsb_20.drawString("Quit", 37, height - 18 - 34, -1);
    	
    	mc.mntsb_25.drawString("User", 37, height - 20, -1);
    	
    	GlStateManager.disable(GL11.GL_SCISSOR_TEST);
    	GlStateManager.popMatrix();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        synchronized (this.threadLock) {
            if (!this.openGLWarning1.isEmpty() && !StringUtils.isNullOrEmpty(this.openGLWarningLink) && mouseX >= this.openGLWarningX1 && mouseX <= this.openGLWarningX2 && mouseY >= this.openGLWarningY1 && mouseY <= this.openGLWarningY2) {
                final GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiconfirmopenlink);
            }
        }
        if (this.areRealmsNotificationsEnabled()) {
            this.realmsNotification.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (mouseX > this.field_193979_N && mouseX < this.field_193979_N + this.field_193978_M && mouseY > this.height - 10 && mouseY < this.height) {
            this.mc.displayGuiScreen(new GuiWinGame(false, Runnables.doNothing()));
        }
        
    	final ScaledResolution sr = new ScaledResolution(this.mc);
    	

        
        if (mouseButton == 0 && mouseY > sr.getScaledHeight() - 330 + 175 && mouseX > sr.getScaledWidth() - 230 && mouseY < sr.getScaledHeight() - 330 + 70 + 175 && mouseX < sr.getScaledWidth() - 230 + 70) {
        	this.mc.displayGuiScreen(new GuiWorldSelection(this));
        }
        if (mouseButton == 0 && mouseY > sr.getScaledHeight() - 330 + 175 && mouseX > sr.getScaledWidth() - 155 && mouseY < sr.getScaledHeight() - 330 + 70 + 175 && mouseX < sr.getScaledWidth() - 155 + 70) {
        	this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (mouseButton == 0 && mouseX > sr.getScaledWidth() - 80 && mouseY > sr.getScaledHeight() - 330 + 175 && mouseX < sr.getScaledWidth() - 80 + 70 && mouseY < sr.getScaledHeight() - 330 + 70 + 175) {
        	 Minecraft.getMinecraft().displayGuiScreen(new GuiAltManager());
        }
        if (mouseButton == 0 && mouseX > sr.getScaledWidth() - 230 && mouseY > sr.getScaledHeight() - 255 + 175 && mouseX < sr.getScaledWidth() - 230 + 70 && mouseY < sr.getScaledHeight() - 255 + 70 + 175) {
        	this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (mouseButton == 0 && mouseX > sr.getScaledWidth() - 154 && mouseY > sr.getScaledHeight() - 255 + 175 && mouseX < sr.getScaledWidth() - 154 + 70 && mouseY < sr.getScaledHeight() - 255 + 70 + 175) {
        	mc.shutdown();
        }
        if (mouseButton == 0 && mouseX > sr.getScaledWidth() - 80 && mouseY > sr.getScaledHeight() - 255 + 175 && mouseX < sr.getScaledWidth() - 80 + 70 && mouseY < sr.getScaledHeight() - 255 + 70 + 175) {
        }
        
    	
    	if (mouseX < currentValueAnimate) {
    		if (mouseButton == 0 && mouseY < 30) {
            	Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
            }
            
            if (mouseButton == 0 && mouseY > sr.getScaledHeight() - 32*6 &&  mouseY < sr.getScaledHeight() - 32*6 + 30) {
            	this.mc.displayGuiScreen(new GuiWorldSelection(this));
            }
            if (mouseButton == 0 && mouseY > sr.getScaledHeight() - 32*5 &&  mouseY < sr.getScaledHeight() - 32*5 + 30) {
            	this.mc.displayGuiScreen(new GuiMultiplayer(this));
            }
            if (mouseButton == 0 && mouseY > sr.getScaledHeight() - 32*4 &&  mouseY < sr.getScaledHeight() - 32*4 + 30) {
            	 Minecraft.getMinecraft().displayGuiScreen(new GuiAltManager());
            }
            if (mouseButton == 0 && mouseY > sr.getScaledHeight() - 32*3 &&  mouseY < sr.getScaledHeight() - 32*3 + 30) {
            	this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
            }
            if (mouseButton == 0 && mouseY > sr.getScaledHeight() - 32*2 &&  mouseY < sr.getScaledHeight() - 32*2 + 30) {
            	mc.shutdown();
            }
            if (mouseButton == 0 && mouseY > sr.getScaledHeight() - 30) {
            }
    	}
    }
    
    @Override
    public void onGuiClosed() {
    	this.progress = 0.0f;
    	  SoundHelper.playSound("sea.wav", 0.2f, true);
        if (this.realmsNotification != null) {
            this.realmsNotification.onGuiClosed();
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        RANDOM = new Random();
        GuiMainMenu.engine = new ParticleEngine();
        MORE_INFO_TEXT = "Please click " + TextFormatting.UNDERLINE + "here" + TextFormatting.RESET + " for more information.";
        SPLASH_TEXTS = new ResourceLocation("texts/splashes.txt");
        MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");
        field_194400_H = new ResourceLocation("textures/gui/title/edition.png");
        TITLE_PANORAMA_PATHS = new ResourceLocation[] { new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png") };
    }
}
