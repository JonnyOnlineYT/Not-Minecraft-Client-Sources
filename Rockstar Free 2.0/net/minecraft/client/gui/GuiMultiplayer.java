package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import javafx.animation.Interpolator;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.network.LanServerInfo;
import net.minecraft.client.network.ServerPinger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.glsandbox.animbackground;
import ru.rockstar.client.ui.altmanager.GuiAltManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class GuiMultiplayer extends GuiScreen
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final ServerPinger oldServerPinger = new ServerPinger();
    private final GuiScreen parentScreen;
    public float s1 = 0f;
	public float s2 = 0f;
    private ServerSelectionList serverListSelector;
    private ServerList savedServerList;
    private GuiButton btnEditServer;
    private GuiButton btnSelectServer;
    private GuiButton btnDeleteServer;
    private boolean deletingServer;
    private boolean addingServer;
    private boolean editingServer;
    private boolean directConnect;
    public boolean hovered;
	 public float currentValueAnimate = 0f;
    /**
     * The text to be displayed when the player's cursor hovers over a server listing.
     */
    private String hoveringText;
    private ServerData selectedServer;
    private LanServerDetector.LanServerList lanServerList;
    private LanServerDetector.ThreadLanServerFind lanServerDetector;
    private boolean initialized;
    private long lastMS;
    private final animbackground backgroundShader;
    private final long initTime;
    public static float progress;

    public GuiMultiplayer(GuiScreen parentScreen)
    {
    	 this.initTime = System.currentTimeMillis();
  	   try {
             this.backgroundShader = new animbackground("/noise.fsh");
         }
         catch (IOException var2) {
             throw new IllegalStateException("Failed to load backgound shader", var2);
         }
        this.parentScreen = parentScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
    	this.progress = 0.0f;
    	
    	this.lastMS = System.currentTimeMillis();
       	
       	
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();

        if (this.initialized)
        {
            this.serverListSelector.setDimensions(this.width, this.height, 32, this.height - 64);
        }
        else
        {
            this.initialized = true;
            this.savedServerList = new ServerList(this.mc);
            this.savedServerList.loadServerList();
            this.lanServerList = new LanServerDetector.LanServerList();

            try
            {
                this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList);
                this.lanServerDetector.start();
            }
            catch (Exception exception)
            {
                LOGGER.warn("Unable to start LAN server detection: {}", (Object)exception.getMessage());
            }

            this.serverListSelector = new ServerSelectionList(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
            this.serverListSelector.updateOnlineServers(this.savedServerList);
        }

        this.createButtons();
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.serverListSelector.handleMouseInput();
    }

    public void createButtons()
    {
        this.btnEditServer = this.addButton(new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, I18n.format("selectServer.edit")));
        this.btnDeleteServer = this.addButton(new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, I18n.format("selectServer.delete")));
        this.btnSelectServer = this.addButton(new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("selectServer.select")));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("selectServer.direct")));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, I18n.format("selectServer.add")));
        this.buttonList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, I18n.format("selectServer.refresh")));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, I18n.format("gui.cancel")));
        this.selectServer(this.serverListSelector.getSelected());
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();

        if (this.lanServerList.getWasUpdated())
        {
            List<LanServerInfo> list = this.lanServerList.getLanServers();
            this.lanServerList.setWasNotUpdated();
            this.serverListSelector.updateNetworkServers(list);
        }

        this.oldServerPinger.pingPendingNetworks();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);

        if (this.lanServerDetector != null)
        {
            this.lanServerDetector.interrupt();
            this.lanServerDetector = null;
        }

        this.oldServerPinger.clearPendingNetworks();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.getSelected() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());

            if (button.id == 2 && guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                String s4 = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData().serverName;

                if (s4 != null)
                {
                    this.deletingServer = true;
                    String s = I18n.format("selectServer.deleteQuestion");
                    String s1 = "'" + s4 + "' " + I18n.format("selectServer.deleteWarning");
                    String s2 = I18n.format("selectServer.deleteButton");
                    String s3 = I18n.format("gui.cancel");
                    GuiYesNo guiyesno = new GuiYesNo(this, s, s1, s2, s3, this.serverListSelector.getSelected());
                    this.mc.displayGuiScreen(guiyesno);
                }
            }
            else if (button.id == 1)
            {
                this.connectToSelected();
            }
            else if (button.id == 4)
            {
                this.directConnect = true;
                this.selectedServer = new ServerData(I18n.format("selectServer.defaultName"), "", false);
                this.mc.displayGuiScreen(new GuiScreenServerList(this, this.selectedServer));
            }
            else if (button.id == 3)
            {
                this.addingServer = true;
                this.selectedServer = new ServerData(I18n.format("selectServer.defaultName"), "", false);
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            }
            else if (button.id == 7 && guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                this.editingServer = true;
                ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false);
                this.selectedServer.copyFrom(serverdata);
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            }
            else if (button.id == 0)
            {
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else if (button.id == 8)
            {
                this.refreshServerList();
            }
        }
    }

    private void refreshServerList()
    {
        this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
    }

    public void confirmClicked(boolean result, int id)
    {
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.getSelected() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());

        if (this.deletingServer)
        {
            this.deletingServer = false;

            if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                this.savedServerList.removeServerData(this.serverListSelector.getSelected());
                this.savedServerList.saveServerList();
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.updateOnlineServers(this.savedServerList);
            }

            this.mc.displayGuiScreen(this);
        }
        else if (this.directConnect)
        {
            this.directConnect = false;

            if (result)
            {
                this.connectToServer(this.selectedServer);
            }
            else
            {
                this.mc.displayGuiScreen(this);
            }
        }
        else if (this.addingServer)
        {
            this.addingServer = false;

            if (result)
            {
                this.savedServerList.addServerData(this.selectedServer);
                this.savedServerList.saveServerList();
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.updateOnlineServers(this.savedServerList);
            }

            this.mc.displayGuiScreen(this);
        }
        else if (this.editingServer)
        {
            this.editingServer = false;

            if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                serverdata.serverName = this.selectedServer.serverName;
                serverdata.serverIP = this.selectedServer.serverIP;
                serverdata.copyFrom(this.selectedServer);
                this.savedServerList.saveServerList();
                this.serverListSelector.updateOnlineServers(this.savedServerList);
            }

            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        int i = this.serverListSelector.getSelected();
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = i < 0 ? null : this.serverListSelector.getListEntry(i);

        if (keyCode == 63)
        {
            this.refreshServerList();
        }
        else
        {
            if (i >= 0)
            {
                if (keyCode == 200)
                {
                    if (isShiftKeyDown())
                    {
                        if (i > 0 && guilistextended$iguilistentry instanceof ServerListEntryNormal)
                        {
                            this.savedServerList.swapServers(i, i - 1);
                            this.selectServer(this.serverListSelector.getSelected() - 1);
                            this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                            this.serverListSelector.updateOnlineServers(this.savedServerList);
                        }
                    }
                    else if (i > 0)
                    {
                        this.selectServer(this.serverListSelector.getSelected() - 1);
                        this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());

                        if (this.serverListSelector.getListEntry(this.serverListSelector.getSelected()) instanceof ServerListEntryLanScan)
                        {
                            if (this.serverListSelector.getSelected() > 0)
                            {
                                this.selectServer(this.serverListSelector.getSize() - 1);
                                this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                            }
                            else
                            {
                                this.selectServer(-1);
                            }
                        }
                    }
                    else
                    {
                        this.selectServer(-1);
                    }
                }
                else if (keyCode == 208)
                {
                    if (isShiftKeyDown())
                    {
                        if (i < this.savedServerList.countServers() - 1)
                        {
                            this.savedServerList.swapServers(i, i + 1);
                            this.selectServer(i + 1);
                            this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                            this.serverListSelector.updateOnlineServers(this.savedServerList);
                        }
                    }
                    else if (i < this.serverListSelector.getSize())
                    {
                        this.selectServer(this.serverListSelector.getSelected() + 1);
                        this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());

                        if (this.serverListSelector.getListEntry(this.serverListSelector.getSelected()) instanceof ServerListEntryLanScan)
                        {
                            if (this.serverListSelector.getSelected() < this.serverListSelector.getSize() - 1)
                            {
                                this.selectServer(this.serverListSelector.getSize() + 1);
                                this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                            }
                            else
                            {
                                this.selectServer(-1);
                            }
                        }
                    }
                    else
                    {
                        this.selectServer(-1);
                    }
                }
                else if (keyCode != 28 && keyCode != 156)
                {
                    super.keyTyped(typedChar, keyCode);
                }
                else
                {
                    this.actionPerformed(this.buttonList.get(2));
                }
            }
            else
            {
                super.keyTyped(typedChar, keyCode);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
    	
    	if (this.progress >= 1.0f) {
            this.progress = 1.0f;
        }
        else {
            this.progress = (System.currentTimeMillis() - this.lastMS) / 550.0f;
        } 
 	   
    	
        final ScaledResolution sr = new ScaledResolution(this.mc);
         
        Gui.drawRect(0,0,sr.getScaledWidth(), sr.getScaledHeight(), new Color(30,30,30).getRGB());
        drawPanel(mouseX, mouseY);
    	
        this.hoveringText = null;
        this.serverListSelector.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.title"), this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.hoveringText != null)
        {
            this.drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(this.hoveringText)), mouseX, mouseY);
        }
    }
    
    public void drawPanel(int mouseX, int mouseY) {
    	final ScaledResolution sr = new ScaledResolution(this.mc);
    	
    	
    	if (mouseX < 35) {
    		hovered = true;
    	} else if (mouseX > 120) {
    		hovered = false;
    	}
    	
   	 	this.currentValueAnimate = (float)Interpolator.LINEAR.interpolate((double)this.currentValueAnimate, this.hovered ? 120 : 35, 0.1);

   	 	
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

    public void connectToSelected()
    {
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.getSelected() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());

        if (guilistextended$iguilistentry instanceof ServerListEntryNormal)
        {
            this.connectToServer(((ServerListEntryNormal)guilistextended$iguilistentry).getServerData());
        }
        else if (guilistextended$iguilistentry instanceof ServerListEntryLanDetected)
        {
            LanServerInfo lanserverinfo = ((ServerListEntryLanDetected)guilistextended$iguilistentry).getServerData();
            this.connectToServer(new ServerData(lanserverinfo.getServerMotd(), lanserverinfo.getServerIpPort(), true));
        }
    }

    private void connectToServer(ServerData server)
    {
        this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, server));
    }

    public void selectServer(int index)
    {
        this.serverListSelector.setSelectedSlotIndex(index);
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = index < 0 ? null : this.serverListSelector.getListEntry(index);
        this.btnSelectServer.enabled = false;
        this.btnEditServer.enabled = false;
        this.btnDeleteServer.enabled = false;

        if (guilistextended$iguilistentry != null && !(guilistextended$iguilistentry instanceof ServerListEntryLanScan))
        {
            this.btnSelectServer.enabled = true;

            if (guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                this.btnEditServer.enabled = true;
                this.btnDeleteServer.enabled = true;
            }
        }
    }

    public ServerPinger getOldServerPinger()
    {
        return this.oldServerPinger;
    }

    public void setHoveringText(String p_146793_1_)
    {
        this.hoveringText = p_146793_1_;
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.serverListSelector.mouseClicked(mouseX, mouseY, mouseButton);
        
        final ScaledResolution sr = new ScaledResolution(this.mc);
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

    /**
     * Called when a mouse button is released.
     */
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
        this.serverListSelector.mouseReleased(mouseX, mouseY, state);
    }

    public ServerList getServerList()
    {
        return this.savedServerList;
    }

    public boolean canMoveUp(ServerListEntryNormal p_175392_1_, int p_175392_2_)
    {
        return p_175392_2_ > 0;
    }

    public boolean canMoveDown(ServerListEntryNormal p_175394_1_, int p_175394_2_)
    {
        return p_175394_2_ < this.savedServerList.countServers() - 1;
    }

    public void moveServerUp(ServerListEntryNormal p_175391_1_, int p_175391_2_, boolean p_175391_3_)
    {
        int i = p_175391_3_ ? 0 : p_175391_2_ - 1;
        this.savedServerList.swapServers(p_175391_2_, i);

        if (this.serverListSelector.getSelected() == p_175391_2_)
        {
            this.selectServer(i);
        }

        this.serverListSelector.updateOnlineServers(this.savedServerList);
    }

    public void moveServerDown(ServerListEntryNormal p_175393_1_, int p_175393_2_, boolean p_175393_3_)
    {
        int i = p_175393_3_ ? this.savedServerList.countServers() - 1 : p_175393_2_ + 1;
        this.savedServerList.swapServers(p_175393_2_, i);

        if (this.serverListSelector.getSelected() == p_175393_2_)
        {
            this.selectServer(i);
        }

        this.serverListSelector.updateOnlineServers(this.savedServerList);
    }
}
