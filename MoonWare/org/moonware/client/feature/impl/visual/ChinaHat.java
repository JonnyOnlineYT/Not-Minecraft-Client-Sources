package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;
/*     */ public class ChinaHat
        /*     */   extends Feature {
    /*  23 */   final ListSetting colorMode = new ListSetting("Hat Color", "Astolfo", () -> Boolean.valueOf(true), "Astolfo", "Pulse", "China", "Custom", "Client", "Static");
    /*  24 */   final ColorSetting onecolor = new ColorSetting("One Color", (new Color(255, 255, 255)).getRGB(), () -> Boolean.valueOf((colorMode.currentMode.equalsIgnoreCase("Static") || colorMode.currentMode.equalsIgnoreCase("Custom"))));
    /*  25 */   final ColorSetting twocolor = new ColorSetting("Two Color", (new Color(255, 255, 255)).getRGB(), () -> Boolean.valueOf(colorMode.currentMode.equalsIgnoreCase("Custom")));
    /*  26 */   final NumberSetting saturation = new NumberSetting("Saturation", 0.7F, 0.1F, 1.0F, 0.1F, () -> Boolean.valueOf(colorMode.currentMode.equalsIgnoreCase("Astolfo")));
    /*  27 */   final BooleanSetting hideInFirstPerson = new BooleanSetting("Hide In First Person", true, () -> Boolean.valueOf(true));
    /*     */
    /*     */   public ChinaHat() {
        /*  30 */     super("ChinaHat", "Показывает китайскую шляпу", Type.Visuals);
        /*  31 */     addSettings(colorMode, onecolor, twocolor, saturation, hideInFirstPerson);
        /*     */   }
    /*     */
    /*     */   @EventTarget
    /*     */   public void asf(EventRender3D event) {
        /*  36 */     if (Minecraft.gameSettings.thirdPersonView == 0 && hideInFirstPerson.getBoolValue()) {
            /*     */       return;
            /*     */     }
        /*  39 */     ItemStack stack = Minecraft.player.getEquipmentInSlot(4);
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*  52 */     double height = (stack.getItem() instanceof net.minecraft.item.ItemArmor) ? (Minecraft.player.isSneaking() ? -0.1D : 0.12D) : ((MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equalsIgnoreCase("Amogus") && Minecraft.player.isSneaking()) ? -0.41999998688697815D : ((MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equalsIgnoreCase("Demon") && Minecraft.player.isSneaking()) ? 0.10000000149011612D : ((CustomModel.modelMode.currentMode.equalsIgnoreCase("Jeff Killer") && Minecraft.player.isSneaking()) ? 0.09000000357627869D : ((CustomModel.modelMode.currentMode.equalsIgnoreCase("Rabbit") && Minecraft.player.isSneaking()) ? -0.01D : (Minecraft.player.isSneaking() ? -0.22D : ((MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equalsIgnoreCase("Amogus")) ? -0.41999998688697815D : ((MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equalsIgnoreCase("Demon")) ? 0.20000000298023224D : (CustomModel.modelMode.currentMode.equalsIgnoreCase("Jeff Killer") ? 0.09000000357627869D : 0.0D))))))));
        /*  53 */     GlStateManager.pushMatrix();
        /*  54 */     GL11.glDisable(2884);
        /*  55 */     GL11.glEnable(3042);
        /*  56 */     GL11.glDisable(3553);
        /*  57 */     GL11.glDisable(3008);
        /*  58 */     GL11.glBlendFunc(770, 771); // 770
        /*  59 */     GL11.glShadeModel(7425); //7425
        /*  60 */     GL11.glTranslatef(0.0F, (float)(Minecraft.player.height + height), 0.0F);
        /*  61 */     GL11.glRotatef(-Minecraft.player.rotationYaw, 0.0F, 1.0F, 0.0F);
        /*  62 */     Color color2 = Color.WHITE;
        /*  63 */     Color firstcolor2 = new Color(onecolor.getColorValue());
        /*  64 */     switch (colorMode.currentMode) {
            /*     */       case "Client":
                /*  66 */         color2 = ClientHelper.getClientColor();
                /*     */         break;
            /*     */       case "Astolfo":
                /*  69 */         color2 = PaletteHelper.astolfo(5.0F, 5.0F, saturation.getNumberValue(), 10.0F);
                /*     */         break;
            /*     */       case "Pulse":
                /*  72 */         color2 = DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0D + 0.0D);
                /*     */         break;
            /*     */       case "China":
                /*  75 */         color2 = DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0D + 0.0D);
                /*     */         break;
            /*     */       case "Custom":
                /*  78 */         color2 = DrawHelper.TwoColoreffect(new Color(onecolor.getColorValue()), new Color(twocolor.getColorValue()), Math.abs(System.currentTimeMillis() / 10L) / 100.0D + 0.0D);
                /*     */         break;
            /*     */       case "Static":
                /*  81 */         color2 = firstcolor2;
                /*     */         break;
            /*     */     }
        /*     */
        /*  85 */     GL11.glBegin(9);
        /*  86 */     DrawHelper.glColor(color2, 255);
        /*  87 */     GL11.glVertex3d(0.0D, 0.3D, 0.1D);
        /*     */     float i;
        /*  89 */     for (i = 0.0F; i < 360.5D; i++) {
            /*  90 */       Color color = Color.WHITE;
            /*  91 */       Color firstcolor = new Color(onecolor.getColorValue());
            /*  92 */       switch (colorMode.currentMode) {
                /*     */         case "Client":
                    /*  94 */           color = ClientHelper.getClientColor();
                    /*     */           break;
                /*     */         case "Astolfo":
                    /*  97 */           color = PaletteHelper.astolfo(i - i + 1.0F, i, saturation.getNumberValue(), 10.0F);
                    /*     */           break;
                /*     */         case "Pulse":
                    /* 100 */           color = DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0D + (6.0F * i / 16.0F / 60.0F));
                    /*     */           break;
                /*     */         case "China":
                    /* 103 */           color = DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0D + (6.0F * (i - i / 16.0F) / 60.0F));
                    /*     */           break;
                /*     */         case "Custom":
                    /* 106 */           color = DrawHelper.TwoColoreffect(new Color(onecolor.getColorValue()), new Color(twocolor.getColorValue()), Math.abs(System.currentTimeMillis() / 10L) / 100.0D + (3.0F * i / 16.0F / 60.0F));
                    /*     */           break;
                /*     */         case "Static":
                    /* 109 */           color = firstcolor;
                    /*     */           break;
                /*     */       }
            /*     */
            /* 113 */       DrawHelper.glColor(color, 180);
            /* 114 */       GL11.glVertex3d(Math.cos(i * Math.PI / 180.0D) * 0.66D, 0.0D, Math.sin(i * Math.PI / 180.0D) * 0.66D);
            /*     */     }
        /*     */
        /* 117 */     GL11.glEnd();
        /* 118 */     GL11.glEnable(3553);
        /* 119 */     GL11.glDisable(3042);
        /* 120 */     GL11.glShadeModel(7424);
        /* 121 */     GL11.glEnable(2884);
        /* 122 */     GL11.glEnable(3008);
        /* 123 */     GlStateManager.resetColor();
        /* 124 */     GlStateManager.popMatrix();
        /*     */   }
    /*     */ }
