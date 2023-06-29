package org.spray.heaven.features.module.modules.misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@ModuleInfo(name = "AutoMath", category = Category.MISC, visible = true, key = Keyboard.KEY_NONE)
public class AutoMath extends Module {
    @EventTarget
    public void onPacket(PacketEvent event) throws ScriptException {
        if (event.getPacket() instanceof SPacketChat) {
            String unformattedText = ((SPacketChat) event.getPacket()).getChatComponent().getUnformattedText();

            if (unformattedText.contains("������ ������: ")) {
                ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
                ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
                String foo = unformattedText.substring(unformattedText.lastIndexOf(": ") + 1);
                mc.player.sendChatMessage(scriptEngine.eval(foo).toString());
            }
            if (unformattedText.contains("����� ������ �������� ����")) {
                for (ITextComponent component : ((SPacketChat) event.getPacket()).getChatComponent()) {
                    if (component.getStyle().getClickEvent() != null && component.getStyle().getClickEvent().getAction() == ClickEvent.Action.RUN_COMMAND) {
                        mc.player.sendChatMessage(component.getStyle().getClickEvent().getValue());
                    }
                }
            }
        }
    }
}