package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import javafx.animation.Interpolator;

import java.awt.Color;
import java.io.IOException;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.ui.altmanager.GuiAltManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class GuiWorldSelection extends GuiScreen
{
    private static final Logger LOGGER = LogManager.getLogger();
    public float s1 = 0f;
	public float s2 = 0f;
    /** The screen to return to when this closes (always Main Menu). */
    protected GuiScreen prevScreen;
    protected String title = "Select world";
    public boolean hovered;
  	 public float currentValueAnimate = 0f;
    /**
     * Tooltip displayed a world whose version is different from this client's
     */
    private String worldVersTooltip;
    private GuiButton deleteButton;
    private GuiButton selectButton;
    private GuiButton renameButton;
    private GuiButton copyButton;
    private GuiListWorldSelection selectionList;

    public GuiWorldSelection(GuiScreen screenIn)
    {
        this.prevScreen = screenIn;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.title = I18n.format("selectWorld.title");
        this.selectionList = new GuiListWorldSelection(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
        this.postInit();
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.selectionList.handleMouseInput();
    }

    public void postInit()
    {
        this.selectButton = this.addButton(new GuiButton(1, this.width / 2 - 154, this.height - 52, 150, 20, I18n.format("selectWorld.select")));
        this.addButton(new GuiButton(3, this.width / 2 + 4, this.height - 52, 150, 20, I18n.format("selectWorld.create")));
        this.renameButton = this.addButton(new GuiButton(4, this.width / 2 - 154, this.height - 28, 72, 20, I18n.format("selectWorld.edit")));
        this.deleteButton = this.addButton(new GuiButton(2, this.width / 2 - 76, this.height - 28, 72, 20, I18n.format("selectWorld.delete")));
        this.copyButton = this.addButton(new GuiButton(5, this.width / 2 + 4, this.height - 28, 72, 20, I18n.format("selectWorld.recreate")));
        this.addButton(new GuiButton(0, this.width / 2 + 82, this.height - 28, 72, 20, I18n.format("gui.cancel")));
        this.selectButton.enabled = false;
        this.deleteButton.enabled = false;
        this.renameButton.enabled = false;
        this.copyButton.enabled = false;
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            GuiListWorldSelectionEntry guilistworldselectionentry = this.selectionList.getSelectedWorld();

            if (button.id == 2)
            {
                if (guilistworldselectionentry != null)
                {
                    guilistworldselectionentry.deleteWorld();
                }
            }
            else if (button.id == 1)
            {
                if (guilistworldselectionentry != null)
                {
                    guilistworldselectionentry.joinWorld();
                }
            }
            else if (button.id == 3)
            {
                this.mc.displayGuiScreen(new GuiCreateWorld(this));
            }
            else if (button.id == 4)
            {
                if (guilistworldselectionentry != null)
                {
                    guilistworldselectionentry.editWorld();
                }
            }
            else if (button.id == 0)
            {
                this.mc.displayGuiScreen(this.prevScreen);
            }
            else if (button.id == 5 && guilistworldselectionentry != null)
            {
                guilistworldselectionentry.recreateWorld();
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
         
        this.worldVersTooltip = null;
        this.selectionList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.worldVersTooltip != null)
        {
            this.drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(this.worldVersTooltip)), mouseX, mouseY);
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

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.selectionList.mouseClicked(mouseX, mouseY, mouseButton);
        
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
        this.selectionList.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Called back by selectionList when we call its drawScreen method, from ours.
     */
    public void setVersionTooltip(String p_184861_1_)
    {
        this.worldVersTooltip = p_184861_1_;
    }

    public void selectWorld(@Nullable GuiListWorldSelectionEntry entry)
    {
        boolean flag = entry != null;
        this.selectButton.enabled = flag;
        this.deleteButton.enabled = flag;
        this.renameButton.enabled = flag;
        this.copyButton.enabled = flag;
    }
}
