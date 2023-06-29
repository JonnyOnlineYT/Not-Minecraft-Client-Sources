package ru.rockstar.client.features.impl.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event2D;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Hotbar extends Feature {
    public Hotbar() {
        super("Hotbar", "Заменяет обычный хотбар майнкрафта , на более красивый", 0, Category.DISPLAY);
    }

    @EventTarget
    public void hotbar(Event2D event2D) {

     //   ScaledResolution scaledResolution = new ScaledResolution(mc);

     //   if(!(mc.currentScreen instanceof GuiChat)) {
         //   String fpsandping = "FPS: " + mc.getDebugFPS() + " Ping: " + Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.player.getUniqueID()).getResponseTime();
         //   String cords = "X:" + (int) mc.player.posX + " Y:" + (int) mc.player.posY + " Z:" + (int) mc.player.posZ;
          //  String time = (new SimpleDateFormat("HH:mm")).format(Calendar.getInstance().getTime());
          //  String date = (new SimpleDateFormat("dd/MM/yyyy")).format(Calendar.getInstance().getTime());

          //  Minecraft.getMinecraft().sfui16.drawStringWithShadow(fpsandping, 3, scaledResolution.getScaledHeight() - 17, -1);
          //  Minecraft.getMinecraft().sfui16.drawStringWithShadow(cords, 3, scaledResolution.getScaledHeight() - 8, -1);
         //   Minecraft.getMinecraft().sfui16.drawStringWithShadow(time, scaledResolution.getScaledWidth() - Minecraft.getMinecraft().sfui16.getStringWidth(time) - 18, scaledResolution.getScaledHeight() - 17.5,  -1);
           // Minecraft.getMinecraft().sfui16.drawStringWithShadow(date, scaledResolution.getScaledWidth() - Minecraft.getMinecraft().sfui16.getStringWidth(date) - 7, scaledResolution.getScaledHeight() - 8.5,  -1);
        }
    }
