package org.moonware.client.feature.impl.combat;

import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventAttackSilent;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class HitSounds
        extends Feature {
    private final ListSetting soundMode = new ListSetting("Sound Mode", "NeverLose",() -> true, "NeverLose", "Moan");
    private final NumberSetting volume = new NumberSetting("Volume", 50.0f, 1.0f, 100.0f, 1.0f, () -> true);

    public HitSounds() {
        super("HitSounds", "\u0412\u043e\u0441\u043f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442 \u0437\u0432\u0443\u043a \u043f\u0440\u0438 \u0443\u0434\u0430\u0440\u0435", Type.Combat);
        addSettings(soundMode, volume);
    }

    @EventTarget
    public void onSuffixUpdate(EventUpdate event) {
        setSuffix(soundMode.getCurrentMode());
    }

    @EventTarget
    public void onAttack(EventAttackSilent event) {
        float volume = this.volume.getCurrentValue() / 10.0f;
        if (soundMode.currentMode.equals("NeverLose")) {
            MWUtils.playSound("neverlose.wav", -30.0f + volume * 3.0f);
        } else if (soundMode.currentMode.equals("Moan")) {
            String randomCount = "moan" + (int) MWUtils.randomFloat(1.0f, 6.0f);
            MWUtils.playSound("moan/" + randomCount + ".wav", -30.0f + volume * 3.0f);
        }
    }
}
