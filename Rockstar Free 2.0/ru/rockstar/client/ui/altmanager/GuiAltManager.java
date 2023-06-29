package ru.rockstar.client.ui.altmanager;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.glsandbox.animbackground;
import ru.rockstar.client.ui.altmanager.alt.Alt;
import ru.rockstar.client.ui.altmanager.alt.AltConfig;
import ru.rockstar.client.ui.altmanager.alt.AltLoginThread;
import ru.rockstar.client.ui.altmanager.alt.AltManager;
import ru.rockstar.client.ui.altmanager.althening.api.AltService;

import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import javafx.animation.Interpolator;

public class GuiAltManager extends GuiScreen {
   public static final AltService altService = new AltService();
   public Alt selectedAlt = null;
   public String status;
   //private GuiAltButton login;
   //private GuiAltButton remove;
   //private GuiAltButton rename;
   private AltLoginThread loginThread;
   private float offset;
   private GuiTextField searchField;
   private ResourceLocation resourceLocation;
   public static float progress;
   private long lastMS;
   private final animbackground backgroundShader;
   private final long initTime;
   public boolean hovered;
	 public float currentValueAnimate = 0f;
	 public float currentValue = 0f;
   public GuiAltManager() {
	   this.initTime = System.currentTimeMillis();
	   try {
           this.backgroundShader = new animbackground("/noise.fsh");
       }
       catch (IOException var2) {
           throw new IllegalStateException("Failed to load backgound shader", var2);
       }
	   
	   
      this.status = TextFormatting.DARK_GRAY + "(" + TextFormatting.GRAY + AltManager.registry.size() + TextFormatting.DARK_GRAY + ")";
   }

   private void getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
      TextureManager textureManager = this.mc.getTextureManager();
      textureManager.getTexture(resourceLocationIn);
      ThreadDownloadImageData textureObject = new ThreadDownloadImageData((File)null, String.format("https://minotar.net/avatar/%s/64.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(username)), new ImageBufferDownload());
      textureManager.loadTexture(resourceLocationIn, textureObject);
   }
   

   public void actionPerformed(GuiButton button) {
      switch(button.id) {
      case 0:
      default:
         break;
      case 1:
         (this.loginThread = new AltLoginThread(this.selectedAlt)).start();
         break;
      case 2:
         if (this.loginThread != null) {
            this.loginThread = null;
         }

         AltManager.registry.remove(this.selectedAlt);
         this.status = TextFormatting.GREEN + "Removed.";
         this.selectedAlt = null;
         break;
      case 3:
         this.mc.displayGuiScreen(new GuiAddAlt(this));
         break;
      case 4:
         this.mc.displayGuiScreen(new GuiAltLogin(this));
         break;
      case 5:
         String randomName = "Rockstar" + RandomStringUtils.randomAlphabetic(3) + RandomStringUtils.randomNumeric(2);
         (this.loginThread = new AltLoginThread(new Alt(randomName, ""))).start();
         AltManager.registry.add(new Alt(randomName, ""));
         break;
      case 6:
         this.mc.displayGuiScreen(new GuiRenameAlt(this));
         break;
      case 7:
         this.mc.displayGuiScreen(new GuiMainMenu());
         break;
      case 8:
         this.status = TextFormatting.RED + "Refreshed!";
         try {
			Main.instance.fileManager.getFile(AltConfig.class).loadFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         break;
      case 4545:
         this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, new ServerData(I18n.format("selectServer.defaultName"), "play.hypixel.net", false)));
         break;
      case 8931:
         this.mc.displayGuiScreen(new GuiMultiplayer(this));
      }

   }

   public void drawScreen(int par1, int par2, float par3) {
	   
	   if (this.progress >= 1.0f) {
           this.progress = 1.0f;
       }
       else {
           this.progress = (System.currentTimeMillis() - this.lastMS) / 550.0f;
       } 
	   
	   
	   final ScaledResolution sr = new ScaledResolution(this.mc);
       Gui.drawRect(0,0,sr.getScaledWidth(), sr.getScaledHeight(), new Color(30,30,30).getRGB());
       
       
       
       
       
       
       
       
       mc.mntsb_25.drawString("Клиент", sr.getScaledWidth() - 228, sr.getScaledHeight() - 175, -1);
       
       DrawHelper.drawRect(sr.getScaledWidth() - 80, sr.getScaledHeight() - 80, sr.getScaledWidth() - 10, sr.getScaledHeight() - 10, new Color(59, 63, 65).getRGB());
      
       DrawHelper.drawRect(sr.getScaledWidth() - 155, sr.getScaledHeight() - 80, sr.getScaledWidth() - 85, sr.getScaledHeight() - 10, new Color(59, 63, 65).getRGB());
       
       DrawHelper.drawRect(sr.getScaledWidth() - 230, sr.getScaledHeight() - 80, sr.getScaledWidth() - 160, sr.getScaledHeight() - 10, new Color(59, 63, 65).getRGB());
       
       
       DrawHelper.drawRect(sr.getScaledWidth() - 80, sr.getScaledHeight() - 155, sr.getScaledWidth() - 10, sr.getScaledHeight() - 85, new Color(59, 63, 65).getRGB());
       
       DrawHelper.drawRect(sr.getScaledWidth() - 155, sr.getScaledHeight() - 155, sr.getScaledWidth() - 85, sr.getScaledHeight() - 85, new Color(59, 63, 65).getRGB());
       
       DrawHelper.drawRect(sr.getScaledWidth() - 230, sr.getScaledHeight() - 155, sr.getScaledWidth() - 160, sr.getScaledHeight() - 85, new Color(59, 63, 65).getRGB());
       
       
       

       
       
       DrawHelper.drawImage(new ResourceLocation("add.png"), sr.getScaledWidth() - 230, sr.getScaledHeight() - 330 + 175, 70, 70, new Color(-1));

       DrawHelper.drawImage(new ResourceLocation("login.png"), sr.getScaledWidth() - 155, sr.getScaledHeight() - 330 + 175, 70, 70, new Color(-1));

       DrawHelper.drawImage(new ResourceLocation("del.png"), sr.getScaledWidth() - 80, sr.getScaledHeight() - 330 + 175, 70, 70, new Color(-1));

       DrawHelper.drawImage(new ResourceLocation("random.png"), sr.getScaledWidth() - 230, sr.getScaledHeight() - 255 + 175, 70, 70, new Color(-1));

       DrawHelper.drawImage(new ResourceLocation("edit.png"),sr.getScaledWidth() - 154, sr.getScaledHeight() - 255 + 175, 70, 70, new Color(-1));

       DrawHelper.drawImage(new ResourceLocation("home.png"),sr.getScaledWidth() - 80, sr.getScaledHeight() - 255 + 175, 70, 70, new Color(-1));

       
       
       
       
       
       
       
       String altName = "Name: " + this.mc.session.getUsername();
       mc.mntsb_30.drawString("ALT MANAGER", currentValueAnimate * progress + 5, 5, -1);
       
       mc.mntsb_20.drawString(altName, currentValueAnimate * progress + 5, 20, -1);
       
       
       
       
      
      if (Mouse.hasWheel()) {
         int wheel = Mouse.getDWheel();
         if (wheel < 0) {
            this.offset += 26.0F;
            if (this.offset < 0.0F) {
               this.offset = 0.0F;
            }
         } else if (wheel > 0) {
            this.offset -= 26.0F;
            if (this.offset < 0.0F) {
               this.offset = 0.0F;
            }
         }
      }

      
      
      GlStateManager.pushMatrix();
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
      GlStateManager.pushMatrix();
      GlStateManager.enable(GL11.GL_SCISSOR_TEST);
      DrawHelper.scissorRect(0.0F, 50, (float)this.width, (double)((float)(this.height)));
      GL11.glEnable(3089);
      int y = 38;
      int number = 0;
      Iterator e = this.getAlts().iterator();
      
      drawPanel(par1, par2);
      
      while(true) {
         Alt alt;
         
            if (!e.hasNext()) {
               GL11.glDisable(3089);
               GL11.glPopMatrix();
               super.drawScreen(par1, par2, par3);
               /*
               if (this.selectedAlt == null) {
                  this.login.enabled = false;
                  this.remove.enabled = false;
                  this.rename.enabled = false;
               } else {
                  this.login.enabled = true;
                  this.remove.enabled = true;
                  this.rename.enabled = true;
               }
               */
              
          	 	
               	if (Keyboard.isKeyDown(200)) {
                   this.currentValue -= 26.0F;
                } else if (Keyboard.isKeyDown(208)) {
                   this.currentValue += 26.0F;
                }
          	 	
          	 	if (this.currentValue < 0) {
          	 		this.currentValue = 0;
          	 	}
               
               
                this.offset = (float)Interpolator.LINEAR.interpolate((double)this.offset, this.currentValue, 0.0);
                

                this.searchField.drawTextBox();
                if (this.searchField.getText().isEmpty() && !this.searchField.isFocused()) {
                   this.mc.mntsb_18.drawStringWithShadow("Поиск Аккаунта", this.width - 123, this.height - 175, DrawHelper.getColor(180));
                }

                return;
            }

            alt = (Alt)e.next();
         

         ++number;
         String name;
         if (alt.getMask().equals("")) {
            name = alt.getUsername();
         } else {
            name = alt.getMask();
         }

         String pass;
         if (alt.getPassword().equals("")) {
            pass = "Not License";
         } else {
            pass = alt.getPassword().replaceAll(".", "*");
         }
         DrawHelper.drawBorderedRect((double)((float)this.width - 235.0F), (double)((float)y - this.offset - 4.0F), (double)((float) currentValueAnimate + 5), (double)((float)y - this.offset + 30.0F), 1.0D, new Color(42, 42, 42).getRGB(), new Color(42, 42, 42).getRGB(), false);
         if (alt != this.selectedAlt) {
            if (this.isMouseOverAlt((double)par1, (double)par2, (double)y) && Mouse.isButtonDown(0)) {
               DrawHelper.drawBorderedRect((double)((float)this.width - 235.0F), (double)((float)y - this.offset - 4.0F), (double)((float) currentValueAnimate + 5), (double)((float)y - this.offset + 30.0F), 1.0D, new Color(42, 42, 42).getRGB(), Color.GRAY.getRGB(), false);
            } else if (this.isMouseOverAlt((double)par1, (double)par2, (double)((float)y - this.offset))) {
               DrawHelper.drawBorderedRect((double)((float)this.width - 235.0F), (double)((float)y - this.offset - 4.0F), (double)((float) currentValueAnimate + 5), (double)((float)y - this.offset + 30.0F), 1.0D, new Color(42, 42, 42).getRGB(), Color.GRAY.getRGB(), false);
            }
         } else if (this.isMouseOverAlt((double)par1, (double)par2, (double)y) && Mouse.isButtonDown(0)) {
            DrawHelper.drawBorderedRect((double)((float)this.width - 235.0F), (double)((float)y - this.offset - 4.0F), (double)((float) currentValueAnimate + 5), (double)((float)y - this.offset + 30.0F), 1.0D,  new Color(42, 42, 42).getRGB(), -1, false);
         } else if (this.isMouseOverAlt((double)par1, (double)par2, (double)((float)y - this.offset))) {
            DrawHelper.drawBorderedRect((double)((float)this.width - 235.0F), (double)((float)y - this.offset - 4.0F), (double)((float) currentValueAnimate + 5), (double)((float)y - this.offset + 30.0F), 1.0D, new Color(42, 42, 42).getRGB(), -1, false);
         } else {
            DrawHelper.drawBorderedRect((double)((float)this.width - 235.0F), (double)((float)y - this.offset - 4.0F), (double)((float) currentValueAnimate + 5), (double)((float)y - this.offset + 30.0F), 1.0D, new Color(42, 42, 42).getRGB(), -1, false);
         }

         String numberP = TextFormatting.GRAY + "" + number + ". " + TextFormatting.RESET;
         GlStateManager.pushMatrix();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         if (this.resourceLocation == null) {
            this.resourceLocation = AbstractClientPlayer.getLocationSkin(name);
            this.getDownloadImageSkin(this.resourceLocation, name);
         } else {
            this.mc.getTextureManager().bindTexture(this.resourceLocation);
            GlStateManager.enableTexture2D();
         //   Gui.drawScaledCustomSizeModalRect((float)this.width / 2.0F - 161.0F, (float)y - this.offset - 4.0F, 8.0F, 8.0F, 8, 8, 33, 33, 64.0F, 64.0F);
         }
         GlStateManager.disable(GL11.GL_SCISSOR_TEST);
         GlStateManager.popMatrix();
         this.mc.mntsb_18.drawString(name, (float) currentValueAnimate + 10, (float)y - this.offset + 5.0F, -1);
         this.mc.sfui18.drawString(pass, (float)currentValueAnimate + 10, (float)y - this.offset + 17.0F, DrawHelper.getColor(110));
         this.mc.mntsb_18.drawString(numberP, (float)this.width - 235 - mc.mntsb_18.getStringWidth(numberP), (float)y - this.offset + 5.0f - 5, -1);
         y += 40;
         
         Gui.drawRect(currentValueAnimate,0,sr.getScaledWidth(), 30, new Color(30,30,30).getRGB());
         
         mc.mntsb_30.drawString("ALT MANAGER", currentValueAnimate * progress + 5, 5, -1);
         
         mc.mntsb_20.drawString(altName, currentValueAnimate * progress + 5, 20, -1);
         
         
      }
   }
   
   
   public void drawPanel(int par1, int par2) {
   	final ScaledResolution sr = new ScaledResolution(this.mc);
   	
   	
   	if (par1 < 35) {
   		hovered = true;
   	} else if (par1 > 120) {
   		hovered = false;
   	}
   	
  	 	this.currentValueAnimate = (float)Interpolator.LINEAR.interpolate((double)this.currentValueAnimate, this.hovered ? 120 : 35, 0.1);

  	 	
   	//currentValueAnimate = hovered ? 120 : 35;
   	
   	
   	
  	 	GlStateManager.pushMatrix();
  	 	GlStateManager.enable(GL11.GL_SCISSOR_TEST);
	
		DrawHelper.scissorRect(0.0f, 0.0f, (currentValueAnimate), this.height);
	
   	
   	
   	DrawHelper.drawRect(0,0, currentValueAnimate, sr.getScaledWidth(), new Color(42, 42, 42).getRGB());
   	
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

   public void initGui() {
	   this.lastMS = System.currentTimeMillis();
   	
   	this.progress = 0.0f;
   	
   	
      this.searchField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width - 126, this.height - 178, 172, 16);
     
      
      
      //this.buttonList.add(this.login = new GuiAltButton(1,  - 122,- 48, 100, 20, "Login"));
      /*
      this.login.enabled = false;
      this.remove.enabled = false;
      this.rename.enabled = false;
      */
   }

   protected void keyTyped(char par1, int par2) {
      this.searchField.textboxKeyTyped(par1, par2);
      if ((par1 == '\t' || par1 == '\r') && this.searchField.isFocused()) {
         this.searchField.setFocused(!this.searchField.isFocused());
      }

      try {
         super.keyTyped(par1, par2);
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   private boolean isAltInArea(int y) {
      return (float)y - this.offset <= (float)(this.height - 50);
   }

   private boolean isMouseOverAlt(double x, double y, double y1) {
      return x >= (double)((float) currentValueAnimate + 5) && y >= y1 - 4.0D && x <= (double)this.width - 235.0F && y <= y1 + 30.0D && x >= 0.0D && y >= 33.0D && x <= (double)this.width && y <= (double)(this.height);
   }

   protected void mouseClicked(int par1, int par2, int par3) {
      this.searchField.mouseClicked(par1, par2, par3);
      if (this.offset < 0.0F) {
         this.offset = 0.0F;
      }

      double y = (double)(38.0F - this.offset);

      for(Iterator e = this.getAlts().iterator(); e.hasNext(); y += 40.0D) {
         Alt alt = (Alt)e.next();
         if (this.isMouseOverAlt((double)par1, (double)par2, y)) {
            if (alt == this.selectedAlt) {
               //this.actionPerformed(this.login);
               return;
            }

            this.selectedAlt = alt;
         }
      }

      try {
         super.mouseClicked(par1, par2, par3);
      } catch (IOException var8) {
         var8.printStackTrace();
      }
      
      
      final ScaledResolution sr = new ScaledResolution(this.mc);
      if (par1 < currentValueAnimate) {
  		if (par3 == 0 && par2 < 30) {
          	Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
          }
          
          if (par3 == 0 && par2 > sr.getScaledHeight() - 32*6 &&  par2 < sr.getScaledHeight() - 32*6 + 30) {
          	this.mc.displayGuiScreen(new GuiWorldSelection(this));
          }
          if (par3 == 0 && par2 > sr.getScaledHeight() - 32*5 &&  par2 < sr.getScaledHeight() - 32*5 + 30) {
          	this.mc.displayGuiScreen(new GuiMultiplayer(this));
          }
          if (par3 == 0 && par2 > sr.getScaledHeight() - 32*4 &&  par2 < sr.getScaledHeight() - 32*4 + 30) {
          	 Minecraft.getMinecraft().displayGuiScreen(new GuiAltManager());
          }
          if (par3 == 0 && par2 > sr.getScaledHeight() - 32*3 &&  par2 < sr.getScaledHeight() - 32*3 + 30) {
          	this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
          }
          if (par3 == 0 && par2 > sr.getScaledHeight() - 32*2 &&  par2 < sr.getScaledHeight() - 32*2 + 30) {
          	mc.shutdown();
          }
          if (par3 == 0 && par2 > sr.getScaledHeight() - 30) {
          }
  	}
      
      
      
      if (par3 == 0 && par2 > sr.getScaledHeight() - 330 + 175 && par1 > sr.getScaledWidth() - 230 && par2 < sr.getScaledHeight() - 330 + 70 + 175 && par1 < sr.getScaledWidth() - 230 + 70) {
    	  this.mc.displayGuiScreen(new GuiAddAlt(this));
      }
      if (par3 == 0 && par2 > sr.getScaledHeight() - 330 + 175 && par1 > sr.getScaledWidth() - 155 && par2 < sr.getScaledHeight() - 330 + 70 + 175 && par1 < sr.getScaledWidth() - 155 + 70) {
    	  if (this.selectedAlt == null) {
    		  this.mc.displayGuiScreen(new GuiAltLogin(this));
    	  } else {
    		  (this.loginThread = new AltLoginThread(this.selectedAlt)).start();
    	  }
      }
      if (par3 == 0 && par1 > sr.getScaledWidth() - 80 && par2 > sr.getScaledHeight() - 330 + 175 && par1 < sr.getScaledWidth() - 80 + 70 && par2 < sr.getScaledHeight() - 330 + 70 + 175) {
    	  if (this.loginThread != null) {
              this.loginThread = null;
           }

           AltManager.registry.remove(this.selectedAlt);
           this.status = TextFormatting.GREEN + "Removed.";
           this.selectedAlt = null;
      }
      if (par3 == 0 && par1 > sr.getScaledWidth() - 230 && par2 > sr.getScaledHeight() - 255 + 175 && par1 < sr.getScaledWidth() - 230 + 70 && par2 < sr.getScaledHeight() - 255 + 70 + 175) {
    	  String randomName = "Rockstar" + RandomStringUtils.randomAlphabetic(3) + RandomStringUtils.randomNumeric(2);
          (this.loginThread = new AltLoginThread(new Alt(randomName, ""))).start();
          AltManager.registry.add(new Alt(randomName, ""));
    	 
      }
      if (par3 == 0 && par1 > sr.getScaledWidth() - 154 && par2 > sr.getScaledHeight() - 255 + 175 && par1 < sr.getScaledWidth() - 154 + 70 && par2 < sr.getScaledHeight() - 255 + 70 + 175) {
    	  this.mc.displayGuiScreen(new GuiRenameAlt(this));
      }
      if (par3 == 0 && par1 > sr.getScaledWidth() - 80 && par2 > sr.getScaledHeight() - 255 + 175 && par1 < sr.getScaledWidth() - 80 + 70 && par2 < sr.getScaledHeight() - 255 + 70 + 175) {
    	  Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
      }
      
      
      
      

   }

   private List<Alt> getAlts() {
      List<Alt> altList = new ArrayList();
      Iterator iterator = AltManager.registry.iterator();

      while(true) {
         Alt alt;
         do {
            if (!iterator.hasNext()) {
               return altList;
            }

            alt = (Alt)iterator.next();
         } while(!this.searchField.getText().isEmpty() && !alt.getMask().toLowerCase().contains(this.searchField.getText().toLowerCase()) && !alt.getUsername().toLowerCase().contains(this.searchField.getText().toLowerCase()));

         altList.add(alt);
      }
   }
}
